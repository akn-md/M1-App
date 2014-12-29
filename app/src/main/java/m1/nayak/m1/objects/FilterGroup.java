package m1.nayak.m1.objects;

import java.util.ArrayList;

/**
 * Created by Ashwin on 12/28/14.
 */
public class FilterGroup {

    public String className;
    public ArrayList<String> subclasses = new ArrayList<String>();

    public FilterGroup(String c) {
        className = c;
    }
}
