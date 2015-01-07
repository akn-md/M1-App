package m1.nayak.m1.backend;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import m1.nayak.m1.Control;
import m1.nayak.m1.objects.Question;

/**
 * Created by Ashwin on 12/28/14.
 */
public class Update {

    public static void updateScores() throws ParseException {

        for(int i = 0; i < Control.questions.size(); i++) {
            Question q = Control.questions.get(i);

            ParseQuery<ParseObject> query = ParseQuery.getQuery(q.entity);
            ParseObject p = query.get(q.id);

            int updatedScore = q.score;

            Log.d("ASH", "Question = " + p.get("Q"));
            Log.d("ASH", "old score = " + p.get("Score"));
            Log.d("ASH", "updated score = " + updatedScore);

            p.put("Score", updatedScore);
            p.save();
        }

    }

}
