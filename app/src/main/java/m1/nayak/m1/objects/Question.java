package m1.nayak.m1.objects;

import java.util.Date;

/**
 * Created by Ashwin on 12/18/14.
 */
public abstract class Question {

    public String entity;
    public String id;
    public String author;

    public String question;
    public String answer;

    public double score;
    public Date lastAsked;

    public boolean answered;
    public boolean answeredCorrectly;
    public boolean isHighYield;

    public String className, subject, topic;

    public String type;


    public Question(String auth, String c, String e, String i, String q, String a, double s, Date last, String sub, String top, boolean IHY) {
        author = auth;
        className = c;
        entity = e;
        id = i;
        question = q;
        answer = a;
        score = s;
        lastAsked = last;
        subject = sub;
        topic = top;
        answeredCorrectly = true;
        isHighYield = IHY;
    }
}
