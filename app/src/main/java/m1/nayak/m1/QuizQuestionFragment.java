package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import m1.nayak.m1.objects.FlashCard;
import m1.nayak.m1.objects.MultipleChoice;
import m1.nayak.m1.objects.Question;

public class QuizQuestionFragment extends Fragment {
    // Parameters
    private boolean multiAnswer;
    private int numQuestions;
    private int currQuestion;

    private OnFragmentInteractionListener mListener;

    // question and answers
    Question q;
    TextView question, answer;
    ListView answerChoices;
    ArrayAdapter<String> answerChoicesAdapter;

    // feedback
    TextView feedback;
    ImageView feedbackImage;
    LinearLayout ratingRow;
    Button one, two, three, four, five;

    // FC feedback
//    ImageView thumbsUp, thumbsDown;
//    TextView correct;

    // progress
    ProgressBar quizProgress;

    // navigation
    ImageButton prev;

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
        prev = (ImageButton) rootView.findViewById(R.id.Button_quizPrev);

        // Get question
        q = Control.questions.get(currQuestion);

        // Question
        question = (TextView) rootView.findViewById(R.id.TextView_question);
        question.setText(q.question);

        // feedback
        feedback = (TextView) rootView.findViewById(R.id.TextView_feedback);
        feedbackImage = (ImageView) rootView.findViewById(R.id.ImageView_feedback);
        ratingRow = (LinearLayout) rootView.findViewById(R.id.RatingRow);
        one = (Button) rootView.findViewById(R.id.Button_quizOne);
        two = (Button) rootView.findViewById(R.id.Button_quizTwo);
        three = (Button) rootView.findViewById(R.id.Button_quizThree);
        four = (Button) rootView.findViewById(R.id.Button_quizFour);
        five = (Button) rootView.findViewById(R.id.Button_quizFive);

        answerChoices = (ListView) rootView.findViewById(R.id.listView_quizQuestion);
        answerChoices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // If multiple choice
        if (q instanceof MultipleChoice) {
            answerChoicesAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, android.R.id.text1, ((MultipleChoice) q).choices);
            answerChoices.setAdapter(answerChoicesAdapter);
            answerChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    // correct answer
                    if (q.answer.equals(((MultipleChoice) q).choices.get(position))) {
                        feedback.setText("Correct! How well did you know it?");
                        feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));
                        feedbackImage.setVisibility(View.VISIBLE);
                        feedback.setVisibility(View.VISIBLE);
                        ratingRow.setVisibility(View.VISIBLE);

                        if (!q.answered) {
                            q.answered = true;

//                            // make navigation visible
//                            next.setEnabled(true);
//                            if (currQuestion < Control.questions.size() - 1) {
//                                next.setBackgroundResource(R.drawable.button_selector_next);
//                            } else {
//                                next.setBackgroundResource(R.drawable.button_selector_report);
//                            }
                        }
                    } else {
                        feedback.setText("Incorrect!");
                        feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_incorrect));
                        feedbackImage.setVisibility(View.VISIBLE);
                        feedback.setVisibility(View.VISIBLE);

                        if (!q.answered)
                            q.answeredCorrectly = false;
                    }
                }
            });
        } else if (q instanceof FlashCard) {
//            thumbsUp = (ImageView) rootView.findViewById(R.id.ImageView_thumbsUp);
//            thumbsDown = (ImageView) rootView.findViewById(R.id.ImageView_thumbsDown);
//            correct = (TextView) rootView.findViewById(R.id.TextView_getItRight);
            answer = (TextView) rootView.findViewById(R.id.TextView_answer);

            answer.setVisibility(View.VISIBLE);
            answerChoices.setVisibility(View.INVISIBLE);
            answer.setText("Click for answer");

            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer.setText(q.answer);
                    q.answered = true;

                    feedback.setText("How well did you know it?");
                    feedback.setVisibility(View.VISIBLE);
                    ratingRow.setVisibility(View.VISIBLE);
//                    // Make thumbs up and down visible
//                    thumbsUp.setVisibility(View.VISIBLE);
//                    thumbsDown.setVisibility(View.VISIBLE);
//                    correct.setVisibility(View.VISIBLE);
                }
            });

//            ArrayList<String> asdf = new ArrayList<String>();
//            asdf.add("Click for answer");
//
//            answerChoicesAdapter = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_activated_1, android.R.id.text1, asdf);
//            answerChoices.setAdapter(answerChoicesAdapter);
//
//            answerChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    ArrayList<String> asdf = new ArrayList<String>();
//                    asdf.add(q.answer);
//                    answerChoicesAdapter = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_list_item_activated_1, android.R.id.text1, asdf);
//                    answerChoices.setAdapter(answerChoicesAdapter);
//                    q.answered = true;
//
//                    // Make thumbs up and down visible
//                    thumbsUp.setVisibility(View.VISIBLE);
//                    thumbsDown.setVisibility(View.VISIBLE);
//                    correct.setVisibility(View.VISIBLE);
//                    feedbackRL.setVisibility(View.INVISIBLE);
//                }
//            });

//            thumbsUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    thumbsUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_up_pressed));
//                    thumbsDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_down));
//                    q.answeredCorrectly = true;
//
//                    next.setEnabled(true);
//                    if (currQuestion < Control.questions.size() - 1) {
//                        next.setBackgroundResource(R.drawable.button_selector_next);
//                    } else {
//                        next.setBackgroundResource(R.drawable.button_selector_report);
//                    }
//                }
//            });
//
//            thumbsDown.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    thumbsDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_down_pressed));
//                    thumbsUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_up));
//                    q.answeredCorrectly = false;
//
//                    next.setEnabled(true);
//                    if (currQuestion < Control.questions.size() - 1) {
//                        next.setBackgroundResource(R.drawable.button_selector_next);
//                    } else {
//                        next.setBackgroundResource(R.drawable.button_selector_report);
//                    }
//                }
//            });
        }


        if (q.answered) {
            if (q instanceof MultipleChoice) {
                int index = ((MultipleChoice) q).choices.indexOf(q.answer);
                answerChoices.setItemChecked(index, true);

                feedback.setText("Correct! How well did you know it?");
                feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));
                feedbackImage.setVisibility(View.VISIBLE);
                feedback.setVisibility(View.VISIBLE);
                ratingRow.setVisibility(View.VISIBLE);
            } else if (q instanceof FlashCard) {
                answerChoices.setItemChecked(0, true);

//                thumbsUp.setVisibility(View.VISIBLE);
//                thumbsDown.setVisibility(View.VISIBLE);
//                correct.setVisibility(View.VISIBLE);
                feedback.setText("How well did you know it?");
                feedback.setVisibility(View.VISIBLE);
                ratingRow.setVisibility(View.VISIBLE);

                if (q.answeredCorrectly) {
//                    thumbsUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_up_pressed));
                } else {
//                    thumbsDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_thumbs_down_pressed));
                }
            }

        }

        // Set up next and prev arrows
//        if (!q.answered) {
//            next.setEnabled(false);
//
//            if (currQuestion < Control.questions.size() - 1)
//                next.setBackgroundResource(R.drawable.ic_action_next_disabled);
//            else
//                next.setBackgroundResource(R.drawable.ic_action_report_disabled);
//
//        } else if (currQuestion == Control.questions.size() - 1) {
//            next.setEnabled(true);
//            next.setBackgroundResource(R.drawable.button_selector_report);
//        } else {
//            next.setEnabled(true);
//            next.setBackgroundResource(R.drawable.button_selector_next);
//        }
        if (currQuestion == 0) {
            prev.setEnabled(false);
            prev.setBackgroundResource(R.drawable.ic_action_prev_disabled);
        } else {
            prev.setEnabled(true);
            prev.setBackgroundResource(R.drawable.button_selector_prev);
        }

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
//                mListener.onNextPressed(currQuestion, lastQuestion, 5);
//            }
//        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPrevPressed(currQuestion);
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.score = 1;
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 1);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.score = 2;
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 2);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.score = 3;
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 3);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.score = 4;
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 4);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.score = 5;
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 5);
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
        public void onNextPressed(int curr, boolean lastQuestion, int rating);

        public void onPrevPressed(int curr);
    }

}
