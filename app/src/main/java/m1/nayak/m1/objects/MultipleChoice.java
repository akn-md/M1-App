package m1.nayak.m1.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashwin on 12/18/14.
 */
public class MultipleChoice extends Question {

    public ArrayList<String> choices;

    public MultipleChoice(String q, String a, ArrayList<String> c, int score, Date last) {
        super(q, a, score, last);
        choices = c;
    }

}
