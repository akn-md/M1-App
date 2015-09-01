package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import m1.nayak.m1.custom.ResultsAdapter;


public class ResultsFragment extends Fragment {

    // quiz results
    ListView results;
    ResultsAdapter adapter;

    // buttons
    ImageButton discard, save;

    private OnFragmentInteractionListener mListener;

    public static ResultsFragment newInstance(String param1, String param2) {
        ResultsFragment fragment = new ResultsFragment();
        return fragment;
    }

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        // set up list view
        results = (ListView) rootView.findViewById(R.id.ListView_results);
        adapter = new ResultsAdapter(getActivity());
        results.setAdapter(adapter);

        // set up buttons
        discard = (ImageButton) rootView.findViewById(R.id.Button_discard);
        save = (ImageButton) rootView.findViewById(R.id.Button_save);

        // goes back to main menu, discards all changes
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResultsClosed(false);
            }
        });

        // goes back to main menu, uploads score updates
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResultsClosed(true);
            }
        });

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
            Log.d("ASH", "Topic = " + entry.getKey() + ", Result = " + correct.get(entry.getKey()) + "/" + entry.getValue() + ",  Average score = " + (double) (score.get(entry.getKey()) / ((double) entry.getValue())));
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
        public void onResultsClosed(boolean save);
    }

}
