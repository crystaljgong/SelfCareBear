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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        Log.i("postEvent", "created a new makeAddEvetTask");
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
            cal.set(java.util.Calendar.HOUR_OF_DAY, 6);
        } else if (task.getTimeOfDay().equals("Afternoon")) {
            cal.set(java.util.Calendar.HOUR_OF_DAY, 10);
        } else if (task.getTimeOfDay().equals("Evening")) {
            cal.set(java.util.Calendar.HOUR_OF_DAY, 18);
        }
        millis = cal.getTimeInMillis();
        DateTime taskStartTime = new DateTime(millis);
        DateTime taskEndTime = new DateTime(millis + 60000 * 60); //end time is 1 hour later


//        String pageToken = null;
//
//        do {
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
        event = mService.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s \n", event.getHtmlLink());

//            CalendarList calendarList = mService.calendarList().list().setPageToken(pageToken).execute();
//            List<CalendarListEntry> gotItems = calendarList.getItems();
//
//            pageToken = calendarList.getNextPageToken();
//        } while (pageToken != null);

//        DateFormat df = new SimpleDateFormat("HH:mm MM/dd");
//        String startTime = df.format(event.getStart().getDateTime());
//        String endTime = df.format(event.getEnd().getDateTime());

        return "Event created:\n" + event.getSummary() + "\nat " + event.getLocation();
//                "\n" +
//               startTime +  " - " + endTime + "\n" +
//                event.getLocation();
    }


    @Override
    protected void onPreExecute() {
        Log.i("postEvent", "called preexecute");
//        calendarText.setText("");
        Toast.makeText(mContext, "Calling Google Calendar API...", Toast.LENGTH_SHORT).show();
//        // mProgress.show();
    }

    @Override
    protected void onPostExecute(String output) {
        Log.i("postEvent", "called postexecute");
        if (output == null) {
            Toast.makeText(mContext, "Failed to add event.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
        }
//        // mProgress.hide();
//        if (output == null || output.size() == 0) {
//            Toast.makeText(getBaseContext(), "No results returned.", Toast.LENGTH_SHORT).show();
//        } else {
//            output.add(0, "Data retrieved using the Google Calendar API:");
//            calendarText.setText(TextUtils.join("\n", output));
//            addCalTasks();
//        }
    }

    @Override
    protected void onCancelled() {
        Log.i("postEvent", "cancelled postevent");
        //TODO do something here? somehow connect?? idK????
        if (mLastError != null) {
            Toast.makeText(mContext, mLastError.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // mProgress.hide();
//        if (mLastError != null) {
//            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                showGooglePlayServicesAvailabilityErrorDialog(
//                        ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                .getConnectionStatusCode());
//            } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                startActivityForResult(
//                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
//                        REQUEST_AUTHORIZATION);
//            } else {
//                Toast.makeText(getBaseContext(), "The following error occurred:\n"
//                        + mLastError.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Toast.makeText(getBaseContext(), "Request cancelled.", Toast.LENGTH_SHORT).show();
//        }
    }
}
