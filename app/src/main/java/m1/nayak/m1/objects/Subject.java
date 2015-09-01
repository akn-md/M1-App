package m1.nayak.m1.objects;

import java.util.ArrayList;

/**
 * Created by Ashwin on 12/29/14.
 */
public class Subject {

    public String className;
    public ArrayList<String> subclasses;
    public int count;

    public Subject(String c, ArrayList<String> s) {
        className = c;
        subclasses = s;
    }
}
