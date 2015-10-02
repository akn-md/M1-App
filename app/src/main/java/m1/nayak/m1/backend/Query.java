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
import java.util.HashMap;
import java.util.List;

import m1.nayak.m1.Control;
import m1.nayak.m1.objects.FlashCard;
import m1.nayak.m1.objects.MultipleChoice;
import m1.nayak.m1.objects.Subject;

/**
 * Created by Ashwin on 12/17/14.
 */

// TODO: getDiseasesQuestions
// TODO: getDrugsQuestions
// TODO: getHormonesQuestions

public class Query {

    // TODO: Subclasses will be null if GK is not a category. Determine how many questions to draw from each category and filter accordingly.
    // TODO: Actually filter by category
    public static void getData(ArrayList<String> subClasses, ArrayList<String> topics, ArrayList<String> categories, int mode) throws ParseException {
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

//        getGKQuestions(20, subClasses, topics);
//        getEnzymeQuestions(true, 2);

        // shuffle questions
//        Collections.shuffle(Control.questions);
    }

    public static void getGKQuestions(ArrayList<String> subClasses, ArrayList<String> topics, int mode) throws ParseException {
        Control.questions.clear();
        HashMap<String, ArrayList<String>> allAnswerChoices = new HashMap<String, ArrayList<String>>();

        String entity = "GeneralKnowledge";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);

        query.whereContainedIn("Subclass", subClasses);
        query.whereContainedIn("Topic", topics);
        query.orderByAscending("createdAt");

        if (mode == 3) {
            query.whereLessThan("Score_" + Control.user, Control.minScore);
        }

//        query.orderByAscending("Score");
//        query.setLimit(count);
        query.setLimit(1000);

        List<ParseObject> ret = null;
        try {
            ret = query.find();
            Log.d("ASH", "Retrieved " + ret.size() + " rows for " + entity + " entity.");

            // separate question and answer sources
//            if (count > ret.size())
//                count = ret.size();

            for (int i = 0; i < ret.size(); i++) {
                createQuestion(ret, i, allAnswerChoices);
            }

            if (mode != 1) {
                Collections.shuffle(Control.questions);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // TODO: if question or answer is null, try different random combo
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
                if (answer == null)
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
                MultipleChoice mc = new MultipleChoice("","",entity, id, question, answer, answerChoices, score, date, "Enzymes", stem2, "");
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
                for (int j = 0; j < subs.length(); j++) {
                    subclasses.add(subs.get(j).toString());
                }
                Collections.sort(subclasses);
                Subject sub = new Subject(s, subclasses);

                String getCount = "GeneralKnowledge";
                ParseQuery<ParseObject> gcQuery = ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Class", s);
                sub.count = gcQuery.count();
                gcQuery.whereEqualTo("Score_" + Control.user, 1);
                double ones = gcQuery.count();
//                Log.d("Parse", sub.count + " questions in Class " + s);
//                Log.d("Parse", ones + " with score=1 in Class " + s);

                ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Class", s);
                gcQuery.whereEqualTo("Score_" + Control.user, 2);
                double twos = gcQuery.count();
//                Log.d("Parse", twos + " with score=2 in Class " + s);

                ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Class", s);
                gcQuery.whereEqualTo("Score_" + Control.user, 3);
                double threes = gcQuery.count();
//                Log.d("Parse", threes + " with score=3 in Class " + s);

                ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Class", s);
                gcQuery.whereEqualTo("Score_" + Control.user, 4);
                double fours = gcQuery.count();
//                Log.d("Parse", fours + " with score=4 in Class " + s);

                ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Class", s);
                gcQuery.whereEqualTo("Score_" + Control.user, 5);
                double fives = gcQuery.count();
//                Log.d("Parse", fives + " with score=5 in Class " + s);

                double classScore = (ones + 2 * twos + 3 * threes + 4 * fours + 5 * fives) / (double) sub.count;
                Log.d("Parse", "Average Score for Class " + s + " = " + classScore);

                Control.classScores.put(sub.className, classScore);
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

    public static void loadSubClasses() {
        String entity = "Subclass";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);
        query.addAscendingOrder(entity);

        try {
            List<ParseObject> ret = query.find();
            Log.d("Parse", "Retrieved " + ret.size() + " rows for " + entity + " entity.");

            for (int i = 0; i < ret.size(); i++) {
                String s = ret.get(i).getString(entity);
                JSONArray tops = ret.get(i).getJSONArray("Topics");
                boolean current = ret.get(i).getBoolean("isCurrentMaterial");

                ArrayList<String> topics = new ArrayList<String>(tops.length());
                for (int j = 0; j < tops.length(); j++) {
                    topics.add(tops.get(j).toString());
                }
                Collections.sort(topics);
                Subject sub = new Subject(s, topics);
                sub.current = current;

                String getCount = "GeneralKnowledge";
                ParseQuery<ParseObject> gcQuery = ParseQuery.getQuery(getCount);
                gcQuery.whereEqualTo("Subclass", s);
                sub.count = gcQuery.count();
//                gcQuery.whereEqualTo("Score", 1);
//                double ones = gcQuery.count();
//                Log.d("Parse", ones + " with score=1");
//
//                ParseQuery.getQuery(getCount);
//                gcQuery.whereEqualTo("Subclass", s);
//                gcQuery.whereEqualTo("Score", 2);
//                double twos = gcQuery.count();
//                Log.d("Parse", twos + " with score=2");
//
//
//                ParseQuery.getQuery(getCount);
//                gcQuery.whereEqualTo("Subclass", s);
//                gcQuery.whereEqualTo("Score", 3);
//                double threes = gcQuery.count();
//                Log.d("Parse", threes + " with score=3");
//
//                ParseQuery.getQuery(getCount);
//                gcQuery.whereEqualTo("Subclass", s);
//                gcQuery.whereEqualTo("Score", 4);
//                double fours = gcQuery.count();
//                Log.d("Parse", fours + " with score=4");
//
//                ParseQuery.getQuery(getCount);
//                gcQuery.whereEqualTo("Subclass", s);
//                gcQuery.whereEqualTo("Score", 5);
//                double fives = gcQuery.count();
//                Log.d("Parse", fives + " with score=5");
//
//
//                double classScore = (ones + 2*twos + 3*threes + 4*fours + 5*fives) / (double) sub.count;
//                Log.d("Parse", "Average Score for Subclass " + s + " = " + classScore);
//
//                Control.subClassScores.put(sub.className, classScore);
                Control.subclasses.add(sub);
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Control.subClassesLoaded = true;
    }

    public static void getDailyQuestions(String[] data) throws ParseException {
        ArrayList<Integer> usedIndicies = new ArrayList<Integer>();

        Control.questions.clear();
        HashMap<String, ArrayList<String>> allAnswerChoices = new HashMap<String, ArrayList<String>>();

        int numQuestions = Integer.parseInt(data[0]);
        int percentageCurrent = Integer.parseInt(data[1]);


        int numCurrQuestions = percentageCurrent * numQuestions / 100;
        int numPastQuestions = numQuestions - numCurrQuestions;

        String[] currSubClasses = null, pastSubClasses = null;

        Log.d("ASH", numCurrQuestions + "," + numPastQuestions);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subclass");
        query.whereEqualTo("isCurrentMaterial", true);
        List<ParseObject> ret = query.find();

        if (ret.size() > 0) {
            currSubClasses = new String[ret.size()];
            for (int i = 0; i < currSubClasses.length; i++) {
                currSubClasses[i] = ret.get(i).getString("Subclass");
            }
        } else {
            numCurrQuestions = 0;
        }

        query = ParseQuery.getQuery("Subclass");
        query.whereEqualTo("isCurrentMaterial", false);
        ret = query.find();

        if (ret.size() > 0) {
            pastSubClasses = new String[ret.size()];
            for (int i = 0; i < pastSubClasses.length; i++) {
                pastSubClasses[i] = ret.get(i).getString("Subclass");
            }
        } else {
            numPastQuestions = 0;
        }

        for (int i = 0; i < numPastQuestions; i++) {
            int combinedIndex = -1;
            int qIndex = -1;

            do {
                int index = (int) (Math.random() * pastSubClasses.length);
                String subClass = pastSubClasses[index];
                query = ParseQuery.getQuery("GeneralKnowledge");
                query.whereEqualTo("Subclass", subClass);
                ret = query.find();

                qIndex = (int) (Math.random() * ret.size());

                String combined = index + "" + qIndex;

                combinedIndex = Integer.parseInt(combined);
                Log.d("ASH", index + "," + qIndex + "," + combinedIndex);
            } while (usedIndicies.contains(combinedIndex));

            usedIndicies.add(combinedIndex);
            createQuestion(ret, qIndex, allAnswerChoices);
        }

        for (int i = 0; i < numCurrQuestions; i++) {
            int combinedIndex = -1;
            int qIndex = -1;

            do {
                int index = (int) (Math.random() * currSubClasses.length);
                String subClass = currSubClasses[index];
                query = ParseQuery.getQuery("GeneralKnowledge");
                query.whereEqualTo("Subclass", subClass);
                ret = query.find();

                qIndex = (int) (Math.random() * ret.size());

                String combined = index + "" + qIndex;

                combinedIndex = Integer.parseInt(combined);
                Log.d("ASH", index + "," + qIndex + "," + combinedIndex);
            } while (usedIndicies.contains(combinedIndex));

            usedIndicies.add(combinedIndex);
            createQuestion(ret, qIndex, allAnswerChoices);
        }

    }

    public static void createQuestion(List<ParseObject> ret, int i, HashMap<String, ArrayList<String>> allAnswerChoices) throws ParseException {
        String id = ret.get(i).getObjectId();
        double score = ret.get(i).getDouble("Score_" + Control.user);
        Date date = ret.get(i).getUpdatedAt();
        String type = ret.get(i).getString("Type");
        String className = ret.get(i).getString("Class");
        String subclass = ret.get(i).getString("Subclass");
        String topic = ret.get(i).getString("Topic");
        String author = ret.get(i).getString("Author");
        String question = "", answer = null;

        if (type.equals("FC") || type.equals("SA") || type.equals("TF")) {
            // Randomly choose question and answer
            double choice = Math.random();

            if (type.equals("FC")) {
                if (choice < 0.5) {
                    // Stick with original question and answer
                    question = ret.get(i).getString("Q");
                    answer = ret.get(i).getString("A");
                } else {
                    // Reverse Q and A
                    answer = ret.get(i).getString("Q");
                    question = ret.get(i).getString("A");
                }
            } else {
                question = ret.get(i).getString("Q");
                answer = ret.get(i).getString("A");
            }

            // Create question object and add
            if(author ==  null)
                author = "Ashwin";
            FlashCard fc = new FlashCard(author, className, "GeneralKnowledge", id, question, answer, score, date, subclass, topic);
            if (type.equals("TF")) {
                fc.trueFalse = true;
                fc.question = "True or False: " + fc.question;
            }
            Control.questions.add(fc);

            // Type = multiple choice or OR
        } else {
            ArrayList<String> answerChoices = null;
            answer = ret.get(i).getString("A");

            if (type.equals("OR")) {
                answerChoices = new ArrayList<String>(2);
                String[] q = ret.get(i).getString("Q").split(" ");
                for (int j = 0; j < q.length; j++) {
                    if (q[j].contains("OR")) {
                        String[] qs = q[j].split("OR");
                        answerChoices.add(qs[0]);
                        answerChoices.add(qs[1]);
                        question += "__________";
                    } else {
                        question += q[j];
                    }

                    if (j < q.length - 1) {
                        question += " ";
                    } else {
                        question += ".";
                    }
                }

                Log.d("ASH", "question = " + question);
            } else {
                question = ret.get(i).getString("Q");
                answerChoices = new ArrayList<String>(4);
                answerChoices.add(answer);

                // query answer choices for specific MC type if not already saved
                if (!allAnswerChoices.containsKey(type)) {
                    ParseQuery<ParseObject> choicesQuery = ParseQuery.getQuery("GeneralKnowledge");
                    choicesQuery.orderByAscending("Score");
                    choicesQuery.whereEqualTo("Type", type);

                    List<ParseObject> choicesRet = choicesQuery.find();
                    ArrayList<String> choicesToAdd = new ArrayList<String>(choicesRet.size());
                    for (int j = 0; j < choicesRet.size(); j++) {
                        String choice = choicesRet.get(j).getString("A");
                        choicesToAdd.add(choice);
                    }

                    allAnswerChoices.put(type, choicesToAdd);
                }

                // Get list of all possible answer choices for specific MC type
                ArrayList<String> allChoices = allAnswerChoices.get(type);

                // If list is too small, add all of them (except for duplicate) and for "No choices left" for the rest
                if (allChoices.size() < 5) {
                    for (int j = 0; j < allChoices.size(); j++) {
                        if (!allChoices.get(j).equals(answer)) {
                            answerChoices.add(allChoices.get(j));
                        }
                    }

                    if (answerChoices.size() < 4) {
                        for (int j = answerChoices.size(); j < 4; j++) {
                            answerChoices.add("No choices left!");
                        }
                    }
                    // If the list is not too small, select 3 non-duplicate choices randomly
                } else {
                    for (int index = 1; index < 4; index++) {
                        int val = -1;
                        do {
                            val = (int) (Math.random() * allChoices.size());
                        } while (answerChoices.contains(allChoices.get(val)));
                        answerChoices.add(allChoices.get(val));
                    }
                }

                Collections.shuffle(answerChoices);
                Collections.shuffle(answerChoices);
            }

            // Create question object and add
            if(author ==  null)
                author = "Ashwin";
            MultipleChoice mc = new MultipleChoice(author, className, "GeneralKnowledge", id, question, answer, answerChoices, score, date, subclass, topic, type);
            Control.questions.add(mc);
        }

    }
}
