package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class QuizConfigureFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // Choose major topic
//    CheckBox generalKnowledge, diseases, enzymes, drugs, hormones;
    // Choose smart quiz generation
//    CheckBox smartQuiz;
    // Start quiz

    Button startQuiz;
    RadioGroup rGroup;
    RadioButton learning, review, focused;
    EditText minScore;

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

//        generalKnowledge = (CheckBox) rootView.findViewById(R.id.CheckBox_GeneralKnowledge);
//        diseases = (CheckBox) rootView.findViewById(R.id.CheckBox_Diseases);
//        enzymes = (CheckBox) rootView.findViewById(R.id.CheckBox_Enzymes);
//        drugs = (CheckBox) rootView.findViewById(R.id.CheckBox_Drugs);
//        hormones = (CheckBox) rootView.findViewById(R.id.CheckBox_Hormones);
//        smartQuiz = (CheckBox) rootView.findViewById(R.id.CheckBox_smartQuizzing);
        minScore = (EditText) rootView.findViewById(R.id.EditText_minimumScore);
        startQuiz = (Button) rootView.findViewById(R.id.Button_startQuiz);
        learning = (RadioButton) rootView.findViewById(R.id.RB_learning);
        review = (RadioButton) rootView.findViewById(R.id.RB_review);
        focused = (RadioButton) rootView.findViewById(R.id.RB_focused);

        rGroup = (RadioGroup) rootView.findViewById(R.id.RG_options);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!generalKnowledge.isChecked() && !diseases.isChecked() && !enzymes.isChecked() && !drugs.isChecked() && !hormones.isChecked()) {
//                    Toast.makeText(getActivity(), "Please choose a category", Toast.LENGTH_LONG).show();
//                } else {
//                    ArrayList<String> categories = new ArrayList<String>();
//                    if (generalKnowledge.isChecked())
//                        categories.add("General Knowledge");
//                    if (diseases.isChecked())
//                        categories.add("Diseases");
//                    if (enzymes.isChecked())
//                        categories.add("Enzymes");
//                    if (drugs.isChecked())
//                        categories.add("Drugs");
//                    if (hormones.isChecked())
//                        categories.add("Hormones"); }

                int selectedID = rGroup.getCheckedRadioButtonId();
                int option = 0;
                if (learning.getId() == selectedID) {
                    option = 1;
                } else if (review.getId() == selectedID) {
                    option = 2;
                } else if (focused.getId() == selectedID) {
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
                    Control.quizMode = option;
                    mListener.onQuizConfigured(option);
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
        public void onQuizConfigured(int option);
    }
}
