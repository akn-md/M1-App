package m1.nayak.m1.backend;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import m1.nayak.m1.Control;
import m1.nayak.m1.objects.MultipleChoice;
import m1.nayak.m1.objects.Subject;

/**
 * Created by Ashwin on 12/17/14.
 */
public class Query {
    public static void getData(ArrayList<String> subClasses, ArrayList<String> categories, boolean smart) throws ParseException {
//        String entity = "EnzymeTest";
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);
//        query.addAscendingOrder(entity);
//
//        try {
//            List<ParseObject> ret = query.find();
//            Log.d("Parse", "Retrieved " + ret.size() + " rows for " + entity + " entity.");
//
//            String[] asdf = new String[ret.size()];
//            for (int i = 0; i < ret.size(); i++) {
//                String s = ret.get(i).getString(entity);
//                asdf[i] = s;
//            }
//
////            if (entity.equals("Class"))
////                Control.classes = categories;
////            else
////                Control.tags = categories;
//
////            for (String s : asdf) {
////                Log.v("POOP", s);
////            }
//
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }

        getEnzymeQuestions(true, 2);
    }

    public static void getEnzymeQuestions(boolean smart, int count) {
        String entity = "EnzymeTest";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);

        query.orderByAscending("Score");
        query.setLimit(count * 3); // multiply by 3 to ensure that every question will have 3 other choices with little repetition

        try {
            List<ParseObject> ret = query.find();
            Log.d("ash", "Retrieved " + ret.size() + " rows for " + entity + " entity.");

            // separate question and answer sources
            if (count > ret.size())
                count = ret.size();

            for (int i = 0; i < count; i++) {
                // get id, score and date
                String id = ret.get(i).getObjectId();
                int score = ret.get(i).getInt("Score");
                Date date = ret.get(i).getUpdatedAt();

                // choose Enzyme, Reaction, or Pathology question stem
                ArrayList<String> EnzymeCols = new ArrayList<>(Arrays.asList("Enzyme", "Pathway", "Reaction", "Activators", "Inhibitors", "Pathology", "Cofactors"));
                String stem = null;
                double choice = Math.random() * 3;

                if (choice < 1) {
                    stem = "Enzyme";
                    EnzymeCols.remove("Enzyme");
                } else if (choice < 2) {
                    stem = "Reaction";
                    EnzymeCols.remove("Reaction");
                } else {
                    stem = "Pathology";
                    EnzymeCols.remove("Pathology");
                }

                String qStem = ret.get(i).getString(stem);
                Log.d("ash", "------");
                Log.d("ash", "q stem = " + stem);

                // choose answer stem
                String stem2 = null;
                choice = Math.random() * 6;

                for (int s = 0; s < 6; s++) {
                    if (choice < (s + 1)) {
                        stem2 = EnzymeCols.get(s);
                        s = 5;
                    }
                }

                Log.d("ash", "a stem = " + stem2);

                String answer = ret.get(i).getString(stem2);

                String question = null;
                if (stem2.endsWith("s")) {
                    question = stem + ":\n" + qStem + "\n\nWhat are the associated " + stem2 + "?";
                } else {
                    question = stem + ":\n" + qStem + "\n\nWhat is the associated " + stem2 + "?";
                }

                Log.d("ash", "Q = " + question);
                Log.d("ash", "A = " + answer);

                // get the 3 other answer choices
                if(answer == null)
                    answer = "NULL";
                ArrayList<String> answerChoices = new ArrayList<String>(4);
                answerChoices.add(answer);

                for (int index = 1; index < 4; index++) {
                    int val = -1;
                    int searchCount = 0;
                    do {
                        if (searchCount > 100)
                            break;
                        val = (int) (Math.random() * ret.size());
                        searchCount++;
                    } while (answerChoices.contains(ret.get(val).getString(stem2)));
                    answerChoices.add(ret.get(val).getString(stem2));
                }

                for (int c = 0; c < answerChoices.size(); c++) {
                    if (answerChoices.get(c) == null)
                        answerChoices.remove(c);
                }

                // shuffle answer choices (x2 or correct answer will never be the 1st choice..))
                Collections.shuffle(answerChoices);
                Collections.shuffle(answerChoices);

                // create Question object and add to array list
                MultipleChoice mc = new MultipleChoice(id, question, answer, answerChoices, score, date, "Enzymes", stem2);
                Control.questions.add(mc);

                Log.d("ash", "Choices:");
                for (String s : answerChoices)
                    Log.d("ash", "\t" + s);
                Log.d("ash", "Score = " + score);
                Log.d("ash", "Last asked = " + date);
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        // shuffle questions
        Collections.shuffle(Control.questions);
    }

    // TODO: Null check for subclasses JSONArray
    public static void loadClasses() {
        String entity = "Class";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);
        query.addAscendingOrder(entity);

        try {
            List<ParseObject> ret = query.find();
            Log.d("Parse", "Retrieved " + ret.size() + " rows for " + entity + " entity.");

            for (int i = 0; i < ret.size(); i++) {
                String s = ret.get(i).getString(entity);
                JSONArray subs = ret.get(i).getJSONArray("Subclasses");

                ArrayList<String> subclasses = new ArrayList<String>(subs.length());
                for(int j = 0; j < subs.length(); j++) {
                    subclasses.add(subs.get(j).toString());
                }
                Collections.sort(subclasses);
                Subject sub = new Subject(s, subclasses);

                Control.classes.add(sub);
            }

//            for(int i = 0; i < Control.classes.size(); i++) {
//                Log.d("ASH", "Class = " + Control.classes.get(i).className);
//                for(int j = 0; j < Control.classes.get(i).subclasses.size(); j++) {
//                    Log.d("ASH", "Subclass = " + Control.classes.get(i).subclasses.get(j));
//                }
//            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
