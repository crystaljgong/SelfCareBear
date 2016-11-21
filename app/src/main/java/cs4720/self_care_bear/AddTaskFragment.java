package cs4720.self_care_bear;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


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
    Button cancel;
    Button confirmAdd;

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
        public void onDataRecieved(String name, String timeOfDay, int pandaPoints);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_add_task, container, false);
        getDialog().setTitle("Add Task");

        name = (EditText) rootView.findViewById(R.id.enterTaskName);
        timeOfDay = (RadioGroup) rootView.findViewById(R.id.radioTimeOfDay);
        points = (RadioGroup) rootView.findViewById(R.id.radioPoints);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        confirmAdd = (Button) rootView.findViewById(R.id.confirm);

        // cancel adding new task with cancel button
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // confirm add task with confirm button

        confirmAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // save task name string, and point and time of day from buttons
                String time = "";
                String pPoints = "";
                int point = 0;
                if(timeOfDay.getCheckedRadioButtonId()!= -1) {
                    int id = timeOfDay.getCheckedRadioButtonId();
                    View radButton = timeOfDay.findViewById(id);
                    int radioId = timeOfDay.indexOfChild(radButton);
                    RadioButton but = (RadioButton) timeOfDay.getChildAt(radioId);
                    time = (String) but.getText();
                }

                if(points.getCheckedRadioButtonId()!= -1) {
                    int id = points.getCheckedRadioButtonId();
                    View radButton = points.findViewById(id);
                    int radioId = points.indexOfChild(radButton);
                    RadioButton but = (RadioButton) points.getChildAt(radioId);
                    pPoints = (String) but.getText();
                    point = Integer.parseInt(pPoints);
                }
                dListener = (DataListener) getActivity();
                dListener.onDataRecieved(name.getText().toString(), time, point);
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
