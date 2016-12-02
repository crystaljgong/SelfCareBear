package cs4720.self_care_bear;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.services.calendar.model.Setting;

import static android.app.Activity.RESULT_OK;
import static cs4720.self_care_bear.SettingsMenu.mLastLocation;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link AddTaskFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link AddTaskFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AddTaskFragment extends DialogFragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    DataListener dListener;

    EditText name;
    RadioGroup timeOfDay;
    RadioGroup points;
//    Button locationButton;
//    TextView locationSelected;
    Button cancel;
    Button confirmAdd;
    PlaceAutocompleteFragment autocompleteFragment;

    //for getting location
   // int PLACE_PICKER_REQUEST = 1;
    Place placeSelected;


    public AddTaskFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddTaskFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddTaskFragment newInstance(String taskName, ) {
//        AddTaskFragment fragment = new AddTaskFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public interface DataListener {
        public void onDataRecieved(String name, String timeOfDay, int pandaPoints, String location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        getDialog().setTitle("Add Task");


        name = (EditText) rootView.findViewById(R.id.enterTaskName);
        timeOfDay = (RadioGroup) rootView.findViewById(R.id.radioTimeOfDay);
        points = (RadioGroup) rootView.findViewById(R.id.radioPoints);
//        locationButton = (Button) rootView.findViewById(R.id.buttonLocation);
//        locationSelected = (TextView) rootView.findViewById(R.id.selectedLocation);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        confirmAdd = (Button) rootView.findViewById(R.id.confirm);

        //make Place autocomplete fragment
        //autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment = new PlaceAutocompleteFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.place_autocomplete_fragment_container, autocompleteFragment);
        ft.commit();

        //autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // cancel adding new task with cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autocompleteFragment != null) {
                    autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_container);
                    if (autocompleteFragment != null) {
                        getChildFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                    }
                }

                dismiss();
            }
        });


//        //add location using Places api
//        locationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startPlacePicker();
//            }
//        });



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("place", "Place: " + place.getName());
                placeSelected = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("error", "An error occurred: " + status);
            }
        });

        // confirm add task with confirm button
        confirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save task name string, and point and time of day from buttons
                String time = "";
                String pPoints = "";

                int point = 0;
                if (timeOfDay.getCheckedRadioButtonId() != -1) {
                    int id = timeOfDay.getCheckedRadioButtonId();
                    View radButton = timeOfDay.findViewById(id);
                    int radioId = timeOfDay.indexOfChild(radButton);
                    RadioButton but = (RadioButton) timeOfDay.getChildAt(radioId);
                    time = (String) but.getText();
                }

                if (points.getCheckedRadioButtonId() != -1) {
                    int id = points.getCheckedRadioButtonId();
                    View radButton = points.findViewById(id);
                    int radioId = points.indexOfChild(radButton);
                    RadioButton but = (RadioButton) points.getChildAt(radioId);
                    pPoints = (String) but.getText();
                    point = Integer.parseInt(pPoints);
                }

                String theName = name.getText().toString();

                if(SettingsMenu.mLastLocation != null) {
                    Log.i("clicky", "" + SettingsMenu.mLastLocation.getLatitude());

                    double currLoc = SettingsMenu.mLastLocation.getLatitude();
                    double curLocLong = SettingsMenu.mLastLocation.getLongitude();
                    Location start = new Location("start");
                    start.setLatitude(currLoc);
                    start.setLongitude(curLocLong);

                    LatLng newLoc = placeSelected.getLatLng();

                    double newLat = newLoc.latitude;
                    double newLon = newLoc.longitude;
                    Location end = new Location("end");
                    end.setLatitude(newLat);
                    end.setLongitude(newLon);

                    float dist = start.distanceTo(end);
                    boolean farAway = dist > 2000;
                    if (farAway) {
                        point = point * 2;
                        Toast.makeText(getActivity(), "That's far away! You get double points if you complete it. ", Toast.LENGTH_LONG).show();
                        theName = theName + " (x2 pts)";
                    }
                }
                dListener = (DataListener) getActivity();
                //TODO: DISABLE CONFIRM BUTTON IF NO NAME OR LOCATION
                dListener.onDataRecieved(theName, time, point, (String) placeSelected.getName());

                //delete this frag
                if (autocompleteFragment != null) {
                    autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_container);
                    if (autocompleteFragment != null) {
                        getChildFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                    }
                }
                dismiss();
            }
        });

        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        //super.onAttach(activity);
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            dListener = (DataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
