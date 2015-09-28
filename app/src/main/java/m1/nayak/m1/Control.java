package m1.nayak.m1;

import java.util.ArrayList;
import java.util.HashMap;

import m1.nayak.m1.objects.Question;
import m1.nayak.m1.objects.Subject;

/**
 * Created by Ashwin on 12/16/14.
 */
public class Control {

    public static String user;

    public static boolean subClassesLoaded = false;

    public static ArrayList<Integer> selectedIndices = new ArrayList<Integer>();

    public static ArrayList<Subject> classes = new ArrayList<Subject>();
    public static ArrayList<Subject> subclasses = new ArrayList<Subject>();
    public static ArrayList<Question> questions = new ArrayList<Question>();

    public static boolean[] selectedItems;

    public static HashMap<String, Double> classScores = new HashMap<String, Double>();
    public static HashMap<String, Double> subClassScores = new HashMap<String, Double>();
    public static HashMap<String, Double> topicScores = new HashMap<String, Double>();

    // for question updates
    public static double updateIncrement = 0.75;
    public static double minScore;
    public static Question update;
    public static int quizMode;



}
