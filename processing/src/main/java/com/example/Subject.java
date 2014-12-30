package com.example;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashwin on 12/29/14.
 */
public class Subject implements Serializable {

    public String className;
    public ArrayList<String> subclasses;

    public Subject(String c, ArrayList<String> s) {
        className = c;
        subclasses = s;
    }
}
