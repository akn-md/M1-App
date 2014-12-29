package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuizConfigureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuizConfigureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizConfigureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Choose major topic
    CheckBox generalKnowledge, diseases, enzymes, drugs, hormones;
    // Choose smart quiz generation
    CheckBox smartQuiz;
    // Start quiz
    Button startQuiz;

    public static QuizConfigureFragment newInstance(String param1, String param2) {
        QuizConfigureFragment fragment = new QuizConfigureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuizConfigureFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_quiz_configure, container, false);

        generalKnowledge = (CheckBox) rootView.findViewById(R.id.CheckBox_GeneralKnowledge);
        diseases = (CheckBox) rootView.findViewById(R.id.CheckBox_Diseases);
        enzymes = (CheckBox) rootView.findViewById(R.id.CheckBox_Enzymes);
        drugs = (CheckBox) rootView.findViewById(R.id.CheckBox_Drugs);
        hormones = (CheckBox) rootView.findViewById(R.id.CheckBox_Hormones);
        smartQuiz = (CheckBox) rootView.findViewById(R.id.CheckBox_smartQuizzing);
        startQuiz = (Button) rootView.findViewById(R.id.Button_startQuiz);

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!generalKnowledge.isChecked() && !diseases.isChecked() && !enzymes.isChecked() && !drugs.isChecked() && !hormones.isChecked()) {
                    Toast.makeText(getActivity(), "Please choose a category", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<String> categories = new ArrayList<String>();
                    if (generalKnowledge.isChecked())
                        categories.add("General Knowledge");
                    if (diseases.isChecked())
                        categories.add("Diseases");
                    if (enzymes.isChecked())
                        categories.add("Enzymes");
                    if (drugs.isChecked())
                        categories.add("Drugs");
                    if (hormones.isChecked())
                        categories.add("Hormones");

                    mListener.onQuizConfigured(categories, smartQuiz.isChecked());
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
    public interface OnFragmentInteractionListener {
        public void onQuizConfigured(ArrayList<String> categories, boolean smartQuiz);
    }
}
