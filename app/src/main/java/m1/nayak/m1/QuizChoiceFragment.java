package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class QuizChoiceFragment extends ListFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean multipleChoice;
    private String level;

    private OnFragmentInteractionListener mListener;

    public static QuizChoiceFragment newInstance(boolean param1, String param2) {
        QuizChoiceFragment fragment = new QuizChoiceFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuizChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            multipleChoice = getArguments().getBoolean(ARG_PARAM1);
            level = getArguments().getString(ARG_PARAM2);
        }

        ArrayList<String> choices = new ArrayList<String>();

        switch (level) {
            // Top level
            case "main":
                choices.add(0, "General Knowledge");
                choices.add(1, "Diseases");
                choices.add(2, "Enzymes");
                choices.add(3, "Drugs");
                choices.add(4, "Hormones");
                break;
            case "class":
                choices.add(0, "Anatomy");
                choices.add(1, "Biochemistry");
                choices.add(2, "Histology");
                choices.add(3, "Physiology");

                break;
            case "subclass":
                break;
            case "topic":
                break;
            case "diseaseTypes":
                break;
            case "pathways":
                break;
        }


        if (multipleChoice)
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, android.R.id.text1, choices));
        else
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_expandable_list_item_1, android.R.id.text1, choices));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (multipleChoice)
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.v("POOP", "numItems = " + getListView().getCheckedItemCount());
//        getListView().setSelection(position);
//        v.setSelected(true);

//        v.setBackgroundResource(android.R.drawable.list_selector_background);
//        if(multipleChoice) {
//            if (Control.selectedIndices.contains(position)) {
//                int pos = Control.selectedIndices.indexOf(position);
//                Control.selectedIndices.remove(pos);
//                v.setBackgroundColor(Color.parseColor("#D3D3D3"));
//
//            } else {
//                Control.selectedIndices.add(position);
//                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
//
//            }
//        }

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onChoiceSelected(position);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onChoiceSelected(int position);
    }

}
