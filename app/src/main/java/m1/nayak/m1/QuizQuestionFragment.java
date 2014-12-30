package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import m1.nayak.m1.objects.MultipleChoice;
import m1.nayak.m1.objects.Question;

// TODO: Handle flash card questions by changing layout dynamically and using flip animation?
// TODO: For flash card questions, add thumbs up/down to layout for user input of correct/incorrect answer

public class QuizQuestionFragment extends Fragment {
    // Parameters
    private boolean multiAnswer;
    private int numQuestions;
    private int currQuestion;

    private OnFragmentInteractionListener mListener;

    // question and answers
    Question q;
    TextView question;
    ListView answerChoices;
    ArrayAdapter<String> answerChoicesAdapter;

    // feedback
    TextView feedback;
    ImageView feedbackImage;

    // progress
    ProgressBar quizProgress;

    // navigation
    ImageButton next, prev;

    public static QuizQuestionFragment newInstance(boolean multi, int curr, int total) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putBoolean("MULTI", multi);
        args.putInt("CURR", curr);
        args.putInt("TOTAL", total);
        fragment.setArguments(args);
        return fragment;
    }

    public QuizQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            multiAnswer = getArguments().getBoolean("MULTI");
            numQuestions = getArguments().getInt("TOTAL");
            currQuestion = getArguments().getInt("CURR");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz_question, container, false);

        // progress
        quizProgress = (ProgressBar) rootView.findViewById(R.id.progressBar_quizQuestion);
        quizProgress.setMax(numQuestions);
        quizProgress.setProgress(currQuestion + 1);

        // navigation
        next = (ImageButton) rootView.findViewById(R.id.Button_quizNext);
        prev = (ImageButton) rootView.findViewById(R.id.Button_quizPrev);

        // Get question
        q = Control.questions.get(currQuestion);

        // Question
        question = (TextView) rootView.findViewById(R.id.TextView_question);
        question.setText(q.question);

        // If multiple choice
        if (q instanceof MultipleChoice) {
            // feedback
            feedback = (TextView) rootView.findViewById(R.id.TextView_feedback);
            feedbackImage = (ImageView) rootView.findViewById(R.id.ImageView_feedback);

            answerChoices = (ListView) rootView.findViewById(R.id.listView_quizQuestion);
            answerChoicesAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, android.R.id.text1, ((MultipleChoice) q).choices);
            answerChoices.setAdapter(answerChoicesAdapter);
            answerChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    // correct answer
                    if (q.answer.equals(((MultipleChoice) q).choices.get(position))) {
                        feedback.setText("Correct!");
                        feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));
                        feedbackImage.setVisibility(View.VISIBLE);
                        feedback.setVisibility(View.VISIBLE);

                        if (!q.answered) {
                            q.answered = true;

                            // update score
                            if(q.answeredCorrectly) {
                                int score = q.score;
                                score += 20;
                                if (score > 100)
                                    score = 100;
                                q.score = score;
                            }

                            // make navigation visible
                            next.setEnabled(true);
                            if (currQuestion < Control.questions.size() - 1) {
                                next.setBackgroundResource(R.drawable.button_selector_next);
                            } else {
                                next.setBackgroundResource(R.drawable.button_selector_report);
                            }
                        }
                    } else {
                        feedback.setText("Incorrect!");
                        feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_incorrect));
                        feedbackImage.setVisibility(View.VISIBLE);
                        feedback.setVisibility(View.VISIBLE);

                        if (!q.answered) {
                            // update score
                            int score = q.score;
                            score -= 20;
                            if (score < 0)
                                score = 0;
                            q.score = score;
                            q.answeredCorrectly = false;
                        }
                    }
                }
            });

            if (q.answered) {
                int index = ((MultipleChoice) q).choices.indexOf(q.answer);
                answerChoices.setItemChecked(index, true);

                feedback.setText("Correct!");
                feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));
                feedbackImage.setVisibility(View.VISIBLE);
                feedback.setVisibility(View.VISIBLE);
            }
        }

        // Set up next and prev arrows
        if (!q.answered) {
            next.setEnabled(false);

            if(currQuestion < Control.questions.size() - 1)
                next.setBackgroundResource(R.drawable.ic_action_next_disabled);
            else
                next.setBackgroundResource(R.drawable.ic_action_report_disabled);

        } else if (currQuestion == Control.questions.size() - 1) {
            next.setEnabled(true);
            next.setBackgroundResource(R.drawable.button_selector_report);
        } else {
            next.setEnabled(true);
            next.setBackgroundResource(R.drawable.button_selector_next);
        }
        if (currQuestion == 0) {
            prev.setEnabled(false);
            prev.setBackgroundResource(R.drawable.ic_action_prev_disabled);
        } else {
            prev.setEnabled(true);
            prev.setBackgroundResource(R.drawable.button_selector_prev);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true:false;
                mListener.onNextPressed(currQuestion, lastQuestion);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPrevPressed(currQuestion);
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
        public void onNextPressed(int curr, boolean lastQuestion);

        public void onPrevPressed(int curr);
    }

}
