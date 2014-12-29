package m1.nayak.m1.backend;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import m1.nayak.m1.Control;

/**
 * Created by Ashwin on 12/28/14.
 */
public class Update {

    public static void updateScores() throws ParseException {
        String entity = "EnzymeTest";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(entity);

        for(int i = 0; i < Control.questions.size(); i++) {
            int updatedScore = Control.questions.get(i).score;

            ParseObject p = query.get(Control.questions.get(i).id);
            Log.d("ASH", "score = " + p.get("Score"));
            Log.d("ASH", "updated = " + updatedScore);
            p.put("Score", updatedScore);
            p.save();
        }

    }

}
