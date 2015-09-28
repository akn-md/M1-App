package m1.nayak.m1;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    // Edit question
    ImageView edit;
    Dialog dialog;
    EditText editQuestion, editAnswer, editType, editScore, editAuthor;
    Spinner editClass, editSubclass, editTopic;
    ArrayAdapter<CharSequence> classAdapter, subClassAdapter, topicAdapter;
    Button saveChanges;

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

        // Edit question
        edit = (ImageView) rootView.findViewById(R.id.ImageView_editQuestion);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_question);
        dialog.setCancelable(true);
        dialog.setTitle("Edit question");
        editQuestion = (EditText) dialog.findViewById(R.id.EditText_question);
        editAnswer = (EditText) dialog.findViewById(R.id.EditText_answer);
        editType = (EditText) dialog.findViewById(R.id.EditText_type);
        editScore = (EditText) dialog.findViewById(R.id.EditText_score);
        editAuthor = (EditText) dialog.findViewById(R.id.EditText_author);
        editClass = (Spinner) dialog.findViewById(R.id.Spinner_class);
        editSubclass = (Spinner) dialog.findViewById(R.id.Spinner_subclass);
        editTopic = (Spinner) dialog.findViewById(R.id.Spinner_topic);
        saveChanges = (Button) dialog.findViewById(R.id.Button_saveChanges);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuestion.setText(q.question);
                editAnswer.setText(q.answer);
                editType.setText(q.type);
                editScore.setText("" + q.score);
                editAuthor.setText(q.author);

                String[] classes = new String[Control.classes.size()];
                int index = 0;
                for (int i = 0; i < Control.classes.size(); i++) {
                    classes[i] = Control.classes.get(i).className;
                    if (classes[i].equals(q.className))
                        index = i;
                }
                classAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, classes);
                editClass.setAdapter(classAdapter);
                editClass.setSelection(index);

                ArrayList<String> topics = new ArrayList<String>();
                String[] subs = new String[Control.subclasses.size()];
                index = 0;
                for (int i = 0; i < Control.subclasses.size(); i++) {
                    subs[i] = Control.subclasses.get(i).className;
                    for (int j = 0; j < Control.subclasses.get(i).subclasses.size(); j++) {
                        topics.add(Control.subclasses.get(i).subclasses.get(j));
                    }
                    if (subs[i].equals(q.subject))
                        index = i;
                }
                subClassAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subs);
                editSubclass.setAdapter(subClassAdapter);
                editSubclass.setSelection(index);

                String[] tops = new String[topics.size()];
                index = 0;
                for (int i = 0; i < topics.size(); i++) {
                    tops[i] = topics.get(i);
                    if (tops[i].equals(q.topic))
                        index = i;
                }
                topicAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tops);
                editTopic.setAdapter(topicAdapter);
                editTopic.setSelection(index);

                dialog.show();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.question = editQuestion.getText().toString();
                q.answer = editAnswer.getText().toString();
                q.type = editType.getText().toString();
                q.score = Double.parseDouble(editScore.getText().toString());
                q.className = editClass.getSelectedItem().toString();
                q.subject = editSubclass.getSelectedItem().toString();
                q.topic = editTopic.getSelectedItem().toString();
                q.author = editAuthor.getText().toString();

                Log.d("ASH", q.question + "," + q.answer + "," + q.type + "," + q.score + "," + q.className + "," + q.subject + "," + q.topic + "," + q.author);
                Control.update = q;
                mListener.onQuestionEdited();
                dialog.dismiss();
            }
        });

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
                    // add bullet points
                    if (q.answer.contains(";")) {
                        String answer = "";
                        String[] parts = q.answer.split(";");
                        for (String s : parts) {
                            answer += "• " + s + "\n";
                        }
                        q.answer = answer;
                    }
                    // add boldness
                    if (q.answer.contains("*")) {
                        String a = "";

                        String[] parts = q.answer.split("\\*");

                        for (int i = 0; i < parts.length; i++) {
                            a += parts[i];
                            i++;
                            if (i < parts.length) a += "<b>" + parts[i] + "</b>";
                        }

                        answer.setText(Html.fromHtml(a));
                    } else {
                        answer.setText(q.answer);
                    }

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
                updateScore(1);
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 1);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(2);
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 2);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(3);
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 3);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(4);
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 4);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(5);
                boolean lastQuestion = (currQuestion == Control.questions.size() - 1) ? true : false;
                mListener.onNextPressed(currQuestion, lastQuestion, 5);
            }
        });

        return rootView;
    }

    public void updateScore(double update) {

        double score = q.score;

        if (update < score) {
            q.score = update;
        } else {
            double diff = update - score;
            double increment = diff * Control.updateIncrement;
            score += increment;
            q.score = score;
        }

        Toast.makeText(getActivity(), "Updated score = " + q.score, Toast.LENGTH_SHORT).show();
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

        public void onQuestionEdited();
    }

}
