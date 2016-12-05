package cs4720.self_care_bear;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import static cs4720.self_care_bear.MainScreen.EVEN_START_TIME;
import static cs4720.self_care_bear.MainScreen.MORN_END_TIME;

/**
 * Created by crystalgong on 11/29/16.
 */

public class MakeAddEventTask extends AsyncTask<TaskItem, Void, String> { //<params, progress, result>
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private Context mContext;

    public MakeAddEventTask(GoogleAccountCredential credential, Context context) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Self_Care_Bear")
                .build();
        Log.i("postEvent", "created a new makeAddEventTask");
        mContext = context;
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected String doInBackground(TaskItem... params) {
        try {
            return postEventToCalApi((TaskItem) params[0]); //passes task to postEventToCalApi
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     *
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private String postEventToCalApi(TaskItem task) throws IOException {

        Log.i("postEvent", "calling postEventToCalApi!");

        //get time from task in dateTimeformat
        long millis;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, 1);

        if (task.getTimeOfDay().equals("Morning")) {
            int randomMorn = ThreadLocalRandom.current().nextInt(4, MORN_END_TIME);
            cal.set(java.util.Calendar.HOUR_OF_DAY, randomMorn);
        } else if (task.getTimeOfDay().equals("Afternoon")) {
            int randomAft = ThreadLocalRandom.current().nextInt(MORN_END_TIME, EVEN_START_TIME);
            cal.set(java.util.Calendar.HOUR_OF_DAY, randomAft);
        } else if (task.getTimeOfDay().equals("Evening")) {
            int randomEven = ThreadLocalRandom.current().nextInt(EVEN_START_TIME, 24);
            cal.set(java.util.Calendar.HOUR_OF_DAY, randomEven);
        }
        millis = cal.getTimeInMillis();
        DateTime taskStartTime = new DateTime(millis);
        DateTime taskEndTime = new DateTime(millis + 60000 * 10); //end time is 10 minutes later

        Event event = new Event()
                .setSummary(task.getName() + " (from Self-Care-Bear)")
                .setLocation(task.getLocation());

        EventDateTime start = new EventDateTime()
                .setDateTime(taskStartTime)
                .setTimeZone("America/Cancun"); // I think this is east coast time
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(taskEndTime)
                .setTimeZone("America/Cancun"); // east coast time???
        event.setEnd(end);


        String calendarId = "primary";
        if (!task.getName().contains("(from Calendar)")) {
            event = mService.events().insert(calendarId, event).execute();
            return "Event created:\n" + event.getSummary() + "\nat " + event.getLocation();
        }
       else return "This event is already in your calendar.";

    }


    @Override
    protected void onPreExecute() {
        Log.i("postEvent", "called preexecute");
    }

    @Override
    protected void onPostExecute(String output) {
        if (output == null) {
            Toast.makeText(mContext, "Failed to add event.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        Log.i("postEvent", "cancelled postevent");
        if (mLastError != null) {
            Toast.makeText(mContext, "Error: " +  mLastError.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
