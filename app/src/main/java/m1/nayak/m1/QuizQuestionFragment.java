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
    private int indicesIndex;
    private int currQuestionIndex;

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
    ImageButton prev, next;

    public static QuizQuestionFragment newInstance(boolean multi, int curr) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putBoolean("MULTI", multi);
        args.putInt("CURR", curr);
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
            indicesIndex = getArguments().getInt("CURR");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz_question, container, false);

        // progress
        quizProgress = (ProgressBar) rootView.findViewById(R.id.progressBar_quizQuestion);
        quizProgress.setMax(Control.questionIndices.size());
        quizProgress.setProgress(indicesIndex + 1);

        // navigation
        prev = (ImageButton) rootView.findViewById(R.id.Button_quizPrev);
        next = (ImageButton) rootView.findViewById(R.id.Button_quizNext);

        // Get question
        currQuestionIndex = Control.questionIndices.get(indicesIndex);
        q = Control.questions.get(currQuestionIndex);

        Log.d("ASH", "indicesIndex = " + indicesIndex);
        Log.d("ASH", "currQuestionIndex = " + currQuestionIndex);


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

        if (Control.rankingsOn) {
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);

            next.setVisibility(View.INVISIBLE);
        } else {
            if (Control.spacedRepetitionOn) {
                // No rankings but spaced repetition is ON
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.VISIBLE);

                one.setText("Soon");
                three.setText("Later");
                five.setText("Never");

                next.setVisibility(View.INVISIBLE);
            } else {
                // No rankings or spaced repetition
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);

                next.setVisibility(View.VISIBLE);
                if (indicesIndex < Control.questionIndices.size() - 1) {
                    next.setBackgroundResource(R.drawable.button_selector_next);
                } else {
                    next.setBackgroundResource(R.drawable.button_selector_report);
                }
            }
        }

        next.setEnabled(false);

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
                        if (Control.rankingsOn) {
                            feedback.setText("Correct! How well did you know it?");
                        } else {
                            if (Control.spacedRepetitionOn) {
                                feedback.setText("Correct! When do you want to see this question again?");
                            } else {
                                feedback.setText("Correct!");
                            }
                        }
                        feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));

                        feedback.setVisibility(View.VISIBLE);
                        feedbackImage.setVisibility(View.VISIBLE);

                        // view navigation
                        ratingRow.setVisibility(View.VISIBLE);
                        next.setEnabled(true);

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
                    String a = "";
                    // add boldness
                    if (q.answer.contains("*")) {
                        String[] parts = q.answer.split("\\*");

                        for (int i = 0; i < parts.length; i++) {
                            a += parts[i];
                            i++;
                            if (i < parts.length) a += "<b>" + parts[i] + "</b>";
                        }
                    } else {
                        a = q.answer;
                    }

                    // add bullet points
                    if (a.contains(";")) {
                        String answer = "";
                        String[] parts = a.split(";");
                        for (String s : parts) {
                            answer += "• " + s.trim() + "\n";
                        }
                        a = answer;
                    }

                    // set final answer
                    answer.setText(Html.fromHtml(a));

                    if (Control.rankingsOn) {
                        feedback.setText("How well did you know it?");
                    } else {
                        if (Control.spacedRepetitionOn) {
                            feedback.setText("When do you want to see this question again?");
                        } else {
                            feedback.setText("");
                        }
                    }

                    feedback.setVisibility(View.VISIBLE);
                    ratingRow.setVisibility(View.VISIBLE);
                    next.setEnabled(true);
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

        }


        if (indicesIndex < Control.lastUnansweredQuestionIndex) {
            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            next.setEnabled(true);
            next.setVisibility(View.VISIBLE);


            if (q instanceof MultipleChoice) {
                int index = ((MultipleChoice) q).choices.indexOf(q.answer);
                answerChoices.setItemChecked(index, true);

                if (Control.rankingsOn) {
                    feedback.setText("Correct! How well did you know it?");
                } else {
                    if (Control.spacedRepetitionOn) {
                        feedback.setText("Correct! When do you want to see this question again?");
                    } else {
                        feedback.setText("Correct!");
                    }
                }
                feedbackImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_correct));

                feedback.setVisibility(View.VISIBLE);
                feedbackImage.setVisibility(View.VISIBLE);
                ratingRow.setVisibility(View.VISIBLE);
            } else if (q instanceof FlashCard) {
                answerChoices.setItemChecked(0, true);

                if (Control.rankingsOn) {
                    feedback.setText("How well did you know it?");
                } else {
                    if (Control.spacedRepetitionOn) {
                        feedback.setText("When do you want to see this question again?");
                    } else {
                        feedback.setText("");
                    }
                }

                feedback.setVisibility(View.VISIBLE);
                ratingRow.setVisibility(View.VISIBLE);
            }
        }

        if (indicesIndex == 0) {
            prev.setEnabled(false);
            prev.setBackgroundResource(R.drawable.ic_action_prev_disabled);
        } else {
            prev.setEnabled(true);
            prev.setBackgroundResource(R.drawable.button_selector_prev);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.answered = true;
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPrevPressed(indicesIndex);
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(1);
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(2);
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(3);
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(4);
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(5);
                boolean lastQuestion = (indicesIndex == Control.questionIndices.size() - 1) ? true : false;
                mListener.onNextPressed(indicesIndex, lastQuestion);
            }
        });

        return rootView;
    }

    public void updateScore(double update) {

        if (Control.rankingsOn) {
            double score = q.score;

            if (update < score) {
                q.score = update;
            } else {
                double diff = update - score;
                double increment = diff * Control.updateIncrement;
                score += increment;
                q.score = score;
            }

            q.answered = true;
            Toast.makeText(getActivity(), "Updated score = " + q.score, Toast.LENGTH_SHORT).show();
        }

        if (Control.spacedRepetitionOn) {
            int calculatedIndex = 0;

            if (Control.rankingsOn) {
                if (q.score < 4.0) {
                    if (q.score < 1.0) {
                        calculatedIndex = indicesIndex + 3;
                    } else if (q.score < 2.0) {
                        calculatedIndex = indicesIndex + 4;
                    } else if (q.score < 3.0) {
                        calculatedIndex = indicesIndex + 5;
                    } else {
                        calculatedIndex = Control.questionIndices.size();
                    }

                    Log.d("ASH", "Calculated Index = " + calculatedIndex);

                    if (calculatedIndex > Control.questionIndices.size() - 1) {
                        Control.questionIndices.add(currQuestionIndex);
                        Log.d("ASH", "Added currQuestionIndex " + currQuestionIndex + " to end");
                    } else {
                        Control.questionIndices.add(calculatedIndex, currQuestionIndex);
                        Log.d("ASH", "Added currQuestionIndex " + currQuestionIndex + " to indicesIndex " + calculatedIndex);
                    }
                }
            } else {
                // 1 = soon
                // 2 = later
                // 3 = never
                if (update != 5.0) {

                    if (update == 1.0) {
                        calculatedIndex = indicesIndex + 3;
                    } else if (update == 3.0) {
                        calculatedIndex = Control.questionIndices.size();
                    }

                    Log.d("ASH", "Calculated Index = " + calculatedIndex);

                    if (calculatedIndex > Control.questionIndices.size() - 1) {
                        Control.questionIndices.add(currQuestionIndex);
                        Log.d("ASH", "Added currQuestionIndex " + currQuestionIndex + " to end");
                    } else {
                        Control.questionIndices.add(calculatedIndex, currQuestionIndex);
                        Log.d("ASH", "Added currQuestionIndex " + currQuestionIndex + " to indicesIndex " + calculatedIndex);
                    }
                }
            }

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
        public void onNextPressed(int curr, boolean lastQuestion);

        public void onPrevPressed(int curr);

        public void onQuestionEdited();
    }

}
