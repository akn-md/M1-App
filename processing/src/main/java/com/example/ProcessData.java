package com.example;

import java.io.File;
import java.util.ArrayList;


public class ProcessData {

    public static void main(String[] args) {
        processQuestions();
    }

    public static void processQuestions() {
        String root = "/Users/Ashwin/Drive/Developer/Projects/M1/imports/STEP I/step";

        String pathToData = root + ".txt";
        String pathToDataJson = root + ".json";

        String pathToClasses = "/Users/Ashwin/Drive/Developer/Projects/M1/classes.ser";
        String pathToClassesJson = "/Users/Ashwin/Drive/Developer/Projects/M1/classes.json";

        File f = new File(pathToClasses);

        ArrayList<Subject> classes;
        ArrayList<String> newClasses, newSubclasses;
        newClasses = new ArrayList<String>();
        newSubclasses = new ArrayList<String>();

        if (f.exists()) {
            classes = (ArrayList) IO1L.readObject(pathToClasses);
        } else {
            classes = new ArrayList<Subject>();
        }

        TextIO.readFile(pathToData);
        TextIO.getln();

        TextIO.writeFile(pathToDataJson);

        boolean first = true;
        TextIO.putln("{ \"results\": [");
        while (!TextIO.eof()) {
            if (first) {
                first = false;
            } else {
                TextIO.putln(",");
            }

            TextIO.putln("{");
            String[] data = TextIO.getln().split("\\t");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].replaceAll("\"", "");
            }

            TextIO.putln("\"Class\": \"" + data[0] + "\",");
            TextIO.putln("\"Q\": \"" + data[1] + "\",");
            TextIO.putln("\"A\": \"" + data[2] + "\",");
            TextIO.putln("\"Hint\": \"" + data[3] + "\",");
            TextIO.putln("\"Type\": \"" + data[4] + "\",");
            TextIO.putln("\"Subclass\": \"" + data[5] + "\",");
            TextIO.putln("\"Topic\": \"" + data[6] + "\",");
//            TextIO.putln("\"Count\": " + 0 + ",");
//            TextIO.putln("\"Correct\": " + 0 + ",");
            TextIO.putln("\"Score\": " + 0 + ",");

            boolean contained = false;
            for(int i = 0; i < classes.size(); i++) {
                if(classes.get(i).className.equals(data[0])) {
                    contained = true;

                    // check subclasses
                    if(!classes.get(i).subclasses.contains(data[5])) {
                        classes.get(i).subclasses.add(data[5]);
                        newSubclasses.add(data[5]);
                    }
                }
            }

            if(!contained) {
                ArrayList<String> sc = new ArrayList<String>();
                sc.add(data[5]);
                Subject s = new Subject(data[0], sc);
                classes.add(s);

                newClasses.add(data[0]);
            }

            TextIO.put("}");
        }

        TextIO.putln();
        TextIO.putln("]");
        TextIO.putln("}");

        IO1L.writeObject(pathToClasses, classes);

        System.out.println("New Classes:");
        for (String s : newClasses)
            System.out.println(s);

        System.out.println("New Subclasses:");
        for (String s : newSubclasses)
            System.out.println(s);


        TextIO.writeFile(pathToClassesJson);
        TextIO.putln("{ \"results\": [");
        first = true;

        for(int i = 0; i < classes.size(); i++) {
            if (first) {
                first = false;
            } else {
                TextIO.putln(",");
            }

            TextIO.putln("{");
            TextIO.putln("\"Class\": \"" + classes.get(i).className + "\",");

            TextIO.put("\"Subclasses\": [");
            for (int j = 0; j < classes.get(i).subclasses.size(); j++) {
                TextIO.put("\"" + classes.get(i).subclasses.get(j) + "\"");
                if (j < classes.get(i).subclasses.size() - 1)
                    TextIO.put(",");
            }
            TextIO.putln("]");

            TextIO.put("}");
        }

        TextIO.putln();
        TextIO.putln("]");
        TextIO.putln("}");
    }
}
