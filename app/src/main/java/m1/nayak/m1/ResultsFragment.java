package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import m1.nayak.m1.custom.ResultsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView results;
    ResultsAdapter adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultsFragment newInstance(String param1, String param2) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        results = (ListView) rootView.findViewById(R.id.ListView_results);
        adapter = new ResultsAdapter(getActivity());
        results.setAdapter(adapter);

        return rootView;
    }

    public void getResults() {
        HashMap<String, Integer> correct = new HashMap<String, Integer>();
        HashMap<String, Integer> total = new HashMap<String, Integer>();
        HashMap<String, Integer> score = new HashMap<String, Integer>();

        for (int i = 0; i < Control.questions.size(); i++) {
            Log.d("ASH", "-----");
            Log.d("ASH", "Subject = " + Control.questions.get(i).subject);
            Log.d("ASH", "Topic = " + Control.questions.get(i).topic);
            Log.d("ASH", "score = " + Control.questions.get(i).score);
            Log.d("ASH", "answered correctly = " + Control.questions.get(i).answeredCorrectly);

            if (total.containsKey(Control.questions.get(i).topic)) {
                total.put(Control.questions.get(i).topic, (total.get(Control.questions.get(i).topic) + 1));
                score.put(Control.questions.get(i).topic, (score.get(Control.questions.get(i).topic) + Control.questions.get(i).score));
            } else {
                total.put(Control.questions.get(i).topic, 1);
                score.put(Control.questions.get(i).topic, Control.questions.get(i).score);
            }

            if (correct.containsKey(Control.questions.get(i).topic)) {
                if (Control.questions.get(i).answeredCorrectly)
                    correct.put(Control.questions.get(i).topic, (correct.get(Control.questions.get(i).topic) + 1));
            } else {
                if (Control.questions.get(i).answeredCorrectly)
                    correct.put(Control.questions.get(i).topic, 1);
                else
                    correct.put(Control.questions.get(i).topic, 0);
            }
        }

        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            Log.d("ASH", "Topic = " + entry.getKey() + ", Result = " + correct.get(entry.getKey()) + "/" + entry.getValue() + ",  Average score = " + (double) (score.get(entry.getKey()) / entry.getValue()));
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onResultsClosed(boolean save);
    }

}
