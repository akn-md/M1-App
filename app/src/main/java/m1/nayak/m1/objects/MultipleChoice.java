package m1.nayak.m1.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashwin on 12/18/14.
 */
public class MultipleChoice extends Question {

    public ArrayList<String> choices;

    public MultipleChoice(String auth, String cc, String e, String i, String q, String a, ArrayList<String> c, double score, Date last, String sub, String top, String t) {
        super(auth, cc, e, i, q, a, score, last, sub, top);
        choices = c;
        type = t;
    }

}
