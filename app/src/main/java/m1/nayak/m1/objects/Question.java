package m1.nayak.m1.objects;

import java.util.Date;

/**
 * Created by Ashwin on 12/18/14.
 */
public abstract class Question {

    public String question;
    public String answer;

    public int score;
    public Date lastAsked;

    public boolean answered;

    public Question(String q, String a, int s, Date last) {
        question = q;
        answer = a;
        score = s;
        lastAsked = last;
    }
}
