package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;


public class QuizConfigureFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Button startQuiz;
    RadioGroup rGroup;
    RadioButton learning, review, focused;
    EditText minScore;
    ToggleButton random, spacedRepetition, highYield;

    public static QuizConfigureFragment newInstance(String param1, String param2) {
        QuizConfigureFragment fragment = new QuizConfigureFragment();
        return fragment;
    }

    public QuizConfigureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_quiz_configure, container, false);

        minScore = (EditText) rootView.findViewById(R.id.EditText_minimumScore);
        startQuiz = (Button) rootView.findViewById(R.id.Button_startQuiz);
        learning = (RadioButton) rootView.findViewById(R.id.RB_learning);
        review = (RadioButton) rootView.findViewById(R.id.RB_review);
        focused = (RadioButton) rootView.findViewById(R.id.RB_focused);
        random = (ToggleButton) rootView.findViewById(R.id.Switch_random);
        spacedRepetition = (ToggleButton) rootView.findViewById(R.id.Switch_spacedRepetition);
        highYield = (ToggleButton) rootView.findViewById(R.id.Switch_highYieldOnly);

        rGroup = (RadioGroup) rootView.findViewById(R.id.RG_options);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.RB_learning == checkedId) {
                    spacedRepetition.setChecked(false);
                    random.setChecked(false);
                    highYield.setChecked(false);
                } else if (R.id.RB_review == checkedId) {
                    spacedRepetition.setChecked(false);
                    random.setChecked(true);
                    highYield.setChecked(false);
                } else if (R.id.RB_focused == checkedId) {
                    spacedRepetition.setChecked(true);
                    random.setChecked(true);
                    highYield.setChecked(false);
                    minScore.setText("3.0");
                }
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Control.isRandomQuiz = (random.isChecked());
                Control.spacedRepetitionOn = (spacedRepetition.isChecked());
                Control.highYield = (highYield.isChecked());

                int selectedID = rGroup.getCheckedRadioButtonId();
                int option = 0;
                if (learning.getId() == selectedID) {
                    Control.rankingsOn = false;
                    Control.isFocusedReview = false;
                    option = 1;
                } else if (review.getId() == selectedID) {
                    Control.rankingsOn = true;
                    Control.isFocusedReview = false;
                    option = 2;
                } else if (focused.getId() == selectedID) {
                    Control.rankingsOn = true;
                    Control.isFocusedReview = true;
                    option = 3;
                    if(minScore.getText().toString().equals("")) {
                        Control.minScore = -1;
                    } else {
                        Control.minScore = Double.parseDouble(minScore.getText().toString());
                    }
                }

                if(option == 0) {
                    Toast.makeText(getActivity(), "Please select a mode", Toast.LENGTH_SHORT).show();
                } else if (Control.minScore < 0) {
                    Toast.makeText(getActivity(), "Please enter a valid minimum score", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("ASH", "isRandomQuiz=" + Control.isRandomQuiz);
                    Log.d("ASH","spaced=" + Control.spacedRepetitionOn);
                    Log.d("ASH","highyield=" + Control.highYield);
                    Log.d("ASH", "rankingsOn=" + Control.rankingsOn);

                    mListener.onQuizConfigured();
                }


            }
        });

        return rootView;
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
        public void onQuizConfigured();
    }
}
