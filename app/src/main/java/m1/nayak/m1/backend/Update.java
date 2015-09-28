package m1.nayak.m1.backend;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import m1.nayak.m1.Control;
import m1.nayak.m1.objects.Question;

/**
 * Created by Ashwin on 12/28/14.
 */
public class Update {

    public static void updateScores() throws ParseException {

        for (int i = 0; i < Control.questions.size(); i++) {
            Question q = Control.questions.get(i);

            ParseQuery<ParseObject> query = ParseQuery.getQuery(q.entity);
            ParseObject p = query.get(q.id);

            double updatedScore = q.score;

            Log.d("ASH", "Question = " + p.get("Q"));
            Log.d("ASH", "old score = " + p.get("Score"));
            Log.d("ASH", "updated score = " + updatedScore);

            p.put("Score_" + Control.user, updatedScore);
            p.save();
        }

    }

    public static void updateQuestion() throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Control.update.entity);
        ParseObject p = query.get(Control.update.id);

        p.put("Score_" + Control.user, Control.update.score);
        p.put("Q", Control.update.question);
        p.put("A", Control.update.answer);
        p.put("Class", Control.update.className);
        p.put("Type", Control.update.type);
        p.put("Subclass", Control.update.subject);
        p.put("Topic", Control.update.topic);
        p.put("Author", Control.update.author);
        p.save();

    }
    public static void updateCategory(String[] data) throws ParseException {

        int mode = Integer.parseInt(data[0]);
        String category = data[1];
        boolean reset = Boolean.parseBoolean(data[2]);
        boolean current = Boolean.parseBoolean(data[3]);

        Log.d("ASH", mode + "," + category + "," + reset + "," + current);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GeneralKnowledge");

        switch (mode) {
            case 1:
                query.whereEqualTo("Class", category);
                break;
            case 2:
                query.whereEqualTo("Subclass", category);
                break;
            case 3:
                query.whereEqualTo("Topic", category);
                break;
            default:
                break;
        }

        if (reset) {
            query.whereNotEqualTo("Score_" + Control.user, 0);
            List<ParseObject> ret = null;

            ret = query.find();
            while (ret.size() > 0) {

                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).put("Score_" + Control.user, 0);
                    ret.get(i).save();
                }
                Log.d("ASH", "Updated " + ret.size() + " row(s).");
                ret = query.find();
            }
        }

        if(mode == 2) {
            query = ParseQuery.getQuery("Subclass");
            query.whereEqualTo("Subclass", category);
            List<ParseObject> ret = null;
            ret = query.find();

            ret.get(0).put("isCurrentMaterial", current);
            ret.get(0).save();
        }

    }

}
