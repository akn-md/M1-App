package m1.nayak.m1.objects;

import java.util.Date;

/**
 * Created by Ashwin on 12/18/14.
 */
public abstract class Question {

    public String id;

    public String question;
    public String answer;

    public int score;
    public Date lastAsked;

    public boolean answered;
    public boolean answeredCorrectly;

    public String subject, topic;

    public Question(String i, String q, String a, int s, Date last, String sub, String top) {
        id = i;
        question = q;
        answer = a;
        score = s;
        lastAsked = last;
        subject = sub;
        topic = top;
        answeredCorrectly = true;
    }
}
