package cs4720.self_care_bear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GiftItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private RecyclerView rv;
    private ArrayList<GiftItem> items;
    public GiftItemRecyclerViewAdapter adapter;
//    private RecyclerView.LayoutManager manager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    @SuppressLint("ValidFragment")
    public GiftItemFragment(ArrayList<GiftItem> items) {
        this.items = items;
    }

    public GiftItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GiftItemFragment newInstance(ArrayList<GiftItem> items) {
        GiftItemFragment fragment = new GiftItemFragment(items);
        Bundle args = new Bundle();
        args.putParcelableArrayList("gift items", items);
        fragment.setArguments(args);
        Log.i("constructor", "successful");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        Log.i("onCreate", "successful");
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy was called");
    }

    public void setData(ArrayList<GiftItem> gifts) {
        this.items = gifts;
    }

    public ArrayList<GiftItem> getData() {
        return items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giftitem_list, container, false);

        // Set the adapter
        mContext = this.getContext();
        rv = (RecyclerView) view.findViewById(R.id.giftList);
        rv.setLayoutManager(new GridLayoutManager(mContext, 2));
        rv.setAdapter(new GiftItemRecyclerViewAdapter(mContext, items, mListener));
        Log.i("onCreateView", "successful");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(GiftItem item);
    }
}
