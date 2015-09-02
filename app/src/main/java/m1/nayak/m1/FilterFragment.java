package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import m1.nayak.m1.custom.FilterAdapter;


// TODO: Add action bar title "Choose classes" or "Choose topics"

public class FilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // ListView
    ListView chooseClassesList;
    FilterAdapter adapter;
    ArrayAdapter<String> chooseClassesListAdapter;
    Button next, prev;
    boolean sc, t;

    // Chosen filters
    ArrayList<String> classes, chosenClasses, subclasses, chosenSubclasses, topics, chosenTopics;

    // updated flow
    int mode;

    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        // Expandable list view
        chooseClassesList = (ListView) rootView.findViewById(R.id.ListView_chooseClasses);
        chooseClassesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        chooseClassesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(chooseClassesList.isItemChecked(position)) {
//                    Log.d("ASH","is checked");
//                    view.setSelected(true);
//
//                } else {
//                    Log.d("ASH","is NOT checked");
//                    view.setSelected(false);
//                }
//            }
//        });

        mode = 1;
        loadList();

        sc = false;
        t = false;

        prev = (Button) rootView.findViewById(R.id.Button_filter_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode -= 1;
                loadList();
            }
        });

        // Next filter
        next = (Button) rootView.findViewById(R.id.Button_filter_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case 1:
                        // Get selected classes
                        chosenClasses = new ArrayList<String>();
                        subclasses = new ArrayList<String>();

                        for (int i = 0; i < Control.classes.size(); i++) {
                            if (chooseClassesList.isItemChecked(i)) {
                                chosenClasses.add(Control.classes.get(i).className);
                                for (int j = 0; j < Control.classes.get(i).subclasses.size(); j++) {
                                    subclasses.add(Control.classes.get(i).subclasses.get(j));
                                }
                            }
                        }

                        if (chosenClasses.size() == 0) {
                            Toast.makeText(getActivity(), "Select at least one class", Toast.LENGTH_LONG).show();
                        } else {
                            mode++;
                            loadList();
                        }
                        break;
                    case 2:
                        // Get selected subclasses
                        chosenSubclasses = new ArrayList<String>();
                        topics = new ArrayList<String>();

                        for (int i = 0; i < subclasses.size(); i++) {
                            if (chooseClassesList.isItemChecked(i)) {
                                chosenSubclasses.add(subclasses.get(i));
                            }
                        }

                        if (chosenSubclasses.size() == 0) {
                            Toast.makeText(getActivity(), "Select at least one subclass", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < Control.subclasses.size(); i++) {
                                if (chosenSubclasses.contains(Control.subclasses.get(i).className)) {
                                    topics.addAll(Control.subclasses.get(i).subclasses);
                                }
                            }

                            Log.d("ash", "Topics size = " + topics.size());


                            mode++;
                            loadList();
                        }
                        break;
                    case 3:
                        // Get selected topics
                        chosenTopics = new ArrayList<String>();

                        for (int i = 0; i < topics.size(); i++) {
                            if (chooseClassesList.isItemChecked(i)) {
                                chosenTopics.add(topics.get(i));
                            }
                        }

                        if (chosenTopics.size() == 0) {
                            Toast.makeText(getActivity(), "Select at least one topic", Toast.LENGTH_LONG).show();
                        } else {
                            mode++;
                            loadList();
                        }
                        break;
                    default:
                        break;
                }


//                if (sc) {
//                    // Get selected subclasses
//                    chosenSubclasses = new ArrayList<String>();
//
//                    for (int i = 0; i < subclasses.size(); i++) {
//                        if (chooseClassesList.isItemChecked(i)) {
//                            chosenSubclasses.add(subclasses.get(i));
//                        }
//                    }
//
//                    if (chosenSubclasses.size() == 0) {
//                        Toast.makeText(getActivity(), "Select at least one subclass", Toast.LENGTH_LONG).show();
//                    } else {
//                        topics = new ArrayList<String>();
//
//                        for (int i = 0; i < Control.subclasses.size(); i++) {
//                            if (chosenSubclasses.contains(Control.subclasses.get(i).className)) {
//                                topics.addAll(Control.subclasses.get(i).subclasses);
//                            }
//                        }
//
//                        Collections.sort(topics);
//                        t = true;
//                        sc = false;
//                        chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, topics);
//                        chooseClassesList.setAdapter(chooseClassesListAdapter);
//
//                        next.setText("Start Quiz");
//                    }
//
//
//                } else if (t) {
//                    // Get selected topics
//                    chosenTopics = new ArrayList<String>();
//
//                    for (int i = 0; i < topics.size(); i++) {
//                        if (chooseClassesList.isItemChecked(i)) {
//                            chosenTopics.add(topics.get(i));
//                        }
//                    }
//
//                    if (chosenTopics.size() == 0) {
//                        Toast.makeText(getActivity(), "Select at least one topic", Toast.LENGTH_LONG).show();
//                    } else {
//                        mListener.onQuizFiltered(chosenClasses, chosenSubclasses, chosenTopics);
//                    }
//                } else {
//                    // Get selected classes
//                    chosenClasses = new ArrayList<String>();
//                    subclasses = new ArrayList<String>();
//                    currList.clear();
//
//                    for (int i = 0; i < Control.classes.size(); i++) {
//                        if (chooseClassesList.isItemChecked(i)) {
//                            chosenClasses.add(Control.classes.get(i).className);
//                            for (int j = 0; j < Control.classes.get(i).subclasses.size(); j++) {
//                                String scName = Control.classes.get(i).subclasses.get(j);
//                                for (int k = 0; k < Control.subclasses.size(); k++) {
//                                    if (scName.equals(Control.subclasses.get(k).className)) {
//                                        scName += " (" + Control.subclasses.get(k).count + " questions)";
//                                    }
//                                }
//                                subclasses.add(Control.classes.get(i).subclasses.get(j));
//                                currList.add(scName);
//                            }
//                        }
//                    }
//
//
//                    if (chosenClasses.size() == 0) {
//                        Toast.makeText(getActivity(), "Select at least one class", Toast.LENGTH_LONG).show();
//                    } else {
//                        Collections.sort(currList);
//                        Collections.sort(subclasses);
//                        sc = true;
//                        chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, currList);
//                        chooseClassesList.setAdapter(chooseClassesListAdapter);
//                    }
//                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadList() {
        switch (mode) {
            case 0:
                // Back to Main

                break;
            case 1:
                // Show classes
                classes = new ArrayList<String>(Control.classes.size());
                for (int i = 0; i < Control.classes.size(); i++) {
                    classes.add(Control.classes.get(i).className + " (" + Control.classes.get(i).count + " questions)");
                }

                Collections.sort(classes);
//                chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, classes);
//                chooseClassesList.setAdapter(chooseClassesListAdapter);

                adapter = new FilterAdapter(getActivity(), classes, null);
                chooseClassesList.setAdapter(adapter);

                break;
            case 2:
                ArrayList<String> temp = new ArrayList<String>();
                // Get question counts for subclasses
                for (int i = 0; i < Control.subclasses.size(); i++) {
                    String scName = Control.subclasses.get(i).className;
                    if (subclasses.contains(scName)) {
                        temp.add(scName + " (" + Control.subclasses.get(i).count + " questions)");
                    }
                }

                Collections.sort(subclasses);
                Collections.sort(temp);

//                chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, temp);
//                chooseClassesList.setAdapter(chooseClassesListAdapter);
                adapter = new FilterAdapter(getActivity(), subclasses, null);
                chooseClassesList.setAdapter(adapter);
                next.setText("Next");
                break;
            case 3:
                // Show topics of chosen subclasses
                Collections.sort(topics);
//                chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, topics);
//                chooseClassesList.setAdapter(chooseClassesListAdapter);
                adapter = new FilterAdapter(getActivity(), topics, null);
                chooseClassesList.setAdapter(adapter);
                next.setText("Start Quiz");
                break;
            case 4:
                // Start quiz
                mListener.onQuizFiltered(chosenClasses, chosenSubclasses, chosenTopics);
                break;
            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onQuizFiltered(ArrayList<String> chosenClasses, ArrayList<String> chosenSubclasses, ArrayList<String> chosenTopics);
    }
}
