package com.example;

import java.io.File;
import java.util.ArrayList;


public class ProcessData {

    public static String pathToClasses = "/Users/Ashwin/Drive/Developer/Projects/M1/Class.ser";
    public static String pathToClassesJson = "/Users/Ashwin/Drive/Developer/Projects/M1/Class.json";
    public static String pathToSubClasses = "/Users/Ashwin/Drive/Developer/Projects/M1/Subclass.ser";
    public static String pathToSubClassesJson = "/Users/Ashwin/Drive/Developer/Projects/M1/Subclass.json";

    public static void main(String[] args) {
//        processQuestions();

        edit();
//        print();
    }

    public static void print() {
        ArrayList<Subject> classes, subClasses;

        classes = (ArrayList) IO1L.readObject(pathToClasses);
        subClasses = (ArrayList) IO1L.readObject(pathToSubClasses);

        for(int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i).className);

            for(int j = 0; j < classes.get(i).subclasses.size(); j++) {
                System.out.print(classes.get(i).subclasses.get(j) + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for(int i = 0; i < subClasses.size(); i++) {
            System.out.println(subClasses.get(i).className);

            for(int j = 0; j < subClasses.get(i).subclasses.size(); j++) {
                System.out.print(subClasses.get(i).subclasses.get(j) + ", ");
            }
            System.out.println();
        }

    }
    public static void edit() {
        ArrayList<Subject> classes, subClasses;

        classes = (ArrayList) IO1L.readObject(pathToClasses);
        subClasses = (ArrayList) IO1L.readObject(pathToSubClasses);

//        for(int i = 0; i < subClasses.size(); i++) {
//            if(subClasses.get(i).className.startsWith("lecture")) {
//                subClasses.remove(i);
//                i--;
//                System.out.print("deleted");
//            }
//        }
//
//        for(int i = 0; i < classes.size(); i++) {
//            if(classes.get(i).className.equals("Physiology")) {
//                for(int j = 0; j < classes.get(i).subclasses.size(); j++) {
//                    if(classes.get(i).subclasses.get(j).startsWith("lecture")) {
//                        classes.get(i).subclasses.remove(j);
//                        j--;
//                        System.out.print("deleted");
//                    }
//                }
//            }
//        }
//        IO1L.writeObject(pathToClasses, classes);
//        IO1L.writeObject(pathToSubClasses, subClasses);
        saveJson(pathToSubClassesJson, subClasses, false);
        saveJson(pathToClassesJson, classes, true);
    }

    public static void saveJson(String path, ArrayList<Subject> classes, boolean classMode) {

        TextIO.writeFile(path);
        TextIO.putln("{ \"results\": [");
        boolean first = true;
        for (int i = 0; i < classes.size(); i++) {
            if (first) {
                first = false;
            } else {
                TextIO.putln(",");
            }

            TextIO.putln("{");

            if (classMode) {
                TextIO.putln("\"Class\": \"" + classes.get(i).className + "\",");
                TextIO.put("\"Subclasses\": [");
            } else {
                TextIO.putln("\"Subclass\": \"" + classes.get(i).className + "\",");
                TextIO.putln("\"isCurrentMaterial\": " + true + ",");
                TextIO.put("\"Topics\": [");
            }

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

    public static void processQuestions() {
        String root = "/Users/Ashwin/Drive/Developer/Projects/M1/imports/Imports/10_22_15/physiology_gir";

        String pathToData = root + ".txt";
        String pathToDataJson = root + ".json";

        File f = new File(pathToClasses);
        File f1 = new File(pathToSubClasses);

        ArrayList<Subject> classes, subClasses;
        ArrayList<String> newClasses, newSubclasses, newTopics;
        newClasses = new ArrayList<String>();
        newSubclasses = new ArrayList<String>();
        newTopics = new ArrayList<String>();

        if (f.exists()) {
            classes = (ArrayList) IO1L.readObject(pathToClasses);
        } else {
            classes = new ArrayList<Subject>();
        }

        if (f1.exists()) {
            subClasses = (ArrayList) IO1L.readObject(pathToSubClasses);
        } else {
            subClasses = new ArrayList<Subject>();
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

            TextIO.putln("\"Class\": \"" + data[0].trim() + "\",");
            TextIO.putln("\"Author\": \"" + data[1].trim() + "\",");
            TextIO.putln("\"Q\": \"" + data[2].trim() + "\",");
            TextIO.putln("\"A\": \"" + data[3].trim() + "\",");
            TextIO.putln("\"Type\": \"" + data[4].trim() + "\",");
            TextIO.putln("\"Subclass\": \"" + data[5].trim() + "\",");
            TextIO.putln("\"Topic\": \"" + data[6].trim() + "\",");
            TextIO.putln("\"isHighYield\": " + true + ",");
            TextIO.putln("\"Score_Kristen\": " + 0 + ",");
            TextIO.putln("\"Score_Ashwin\": " + 0);

//            System.out.println("Class = " + data[0] + ", Subclass = " + data[4] + ", Topic = " + data[5]);

            boolean classExists = false, subClassExists = false;
            int classIndex = 0, subClassIndex = 0;

            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).className.equals(data[0])) {
                    classExists = true;
                    classIndex = i;
                }
            }

            for (int i = 0; i < subClasses.size(); i++) {
                if (subClasses.get(i).className.equals(data[5])) {
                    subClassExists = true;
                    subClassIndex = i;
                }
            }

            // class exists
            if (classExists) {
                // check subclasses and if it isn't linked to class we link it
                // we don't add it to subClasses because it might already exist (by being linked to another class)
                if (!classes.get(classIndex).subclasses.contains(data[5])) {
                    classes.get(classIndex).subclasses.add(data[5]);
                }

                // check if subClass exists in subClasses
                if (subClassExists) {
                    // check topics and if it isn't linked to subclass we link it
                    if (!subClasses.get(subClassIndex).subclasses.contains(data[6])) {
                        subClasses.get(subClassIndex).subclasses.add(data[6]);
                        newTopics.add(data[6]);
                    }
                } else {
                    // subClass doesn't exist in subClasses so we create it and link topic
                    ArrayList<String> newTops = new ArrayList<String>();
                    newTops.add(data[6]);
                    Subject newSub = new Subject(data[5], newTops);
                    subClasses.add(newSub);
                    newSubclasses.add(data[5]);
                    newTopics.add(data[6]);
                }
            } else {
                // Add new class to classes and link subClass
                ArrayList<String> newSubs = new ArrayList<String>();
                newSubs.add(data[5]);
                Subject s = new Subject(data[0], newSubs);
                classes.add(s);
                newClasses.add(data[0]);

                // check if subClass exists in subClasses
                if (subClassExists) {
                    // check topics and link if it doesn't exist
                    if (!subClasses.get(subClassIndex).subclasses.contains(data[6])) {
                        subClasses.get(subClassIndex).subclasses.add(data[6]);
                        newTopics.add(data[6]);
                    }
                } else {
                    // subClass doesn't exist
                    ArrayList<String> newTops = new ArrayList<String>();
                    newTops.add(data[6]);
                    Subject newSub = new Subject(data[5], newTops);
                    subClasses.add(newSub);
                    newSubclasses.add(data[5]);
                    newTopics.add(data[6]);
                }
            }

            TextIO.put("}");
        }

        TextIO.putln();
        TextIO.putln("]");
        TextIO.putln("}");

        IO1L.writeObject(pathToClasses, classes);
        IO1L.writeObject(pathToSubClasses, subClasses);

        System.out.println("New Classes:");
        for (String s : newClasses)
            System.out.println(s);

        System.out.println();

        System.out.println("New Subclasses:");
        for (String s : newSubclasses)
            System.out.println(s);

        System.out.println();

        System.out.println("New Topics:");
        for (String s : newTopics)
            System.out.println(s);

        System.out.println();

        saveJson(pathToClassesJson, classes, true);
        saveJson(pathToSubClassesJson, subClasses, false);
    }


}
