package m1.nayak.m1;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

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

    // progress
    ProgressBar progress;

    // item data
    Dialog dialog;
    ToggleButton tButton;
    CheckBox resetScores;
    Button saveChanges;
    String currItem;

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

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_list_item);
        dialog.setCancelable(true);
        tButton = (ToggleButton) dialog.findViewById(R.id.TB_materialType);
        resetScores = (CheckBox) dialog.findViewById(R.id.CheckBox_resetScores);
        saveChanges = (Button) dialog.findViewById(R.id.Button_confrimCategoryChanges);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 2) {
                    for (int i = 0; i < Control.subclasses.size(); i++) {
                        if (Control.subclasses.get(i).className.equals(currItem)) {
                            if (tButton.isChecked()) {
                                Control.subclasses.get(i).current = true;
                            } else {
                                Control.subclasses.get(i).current = false;
                            }
                        }
                    }
                }
                mListener.updateCategory(mode, currItem, resetScores.isChecked(), tButton.isChecked());
                dialog.dismiss();
            }
        });

        // Expandable list view
        chooseClassesList = (ListView) rootView.findViewById(R.id.ListView_chooseClasses);
        chooseClassesList.setLongClickable(true);
        chooseClassesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                switch (mode) {
                    case 1:
                        currItem = classes.get(position);
                        tButton.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        currItem = subclasses.get(position);
                        tButton.setVisibility(View.VISIBLE);
                        for (int i = 0; i < Control.subclasses.size(); i++) {
                            if (Control.subclasses.get(i).className.equals(currItem)) {
                                if (Control.subclasses.get(i).current) {
                                    tButton.setChecked(true);
                                } else {
                                    tButton.setChecked(false);
                                }
                            }
                        }
                        break;
                    case 3:
                        currItem = topics.get(position);
                        tButton.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
                Toast.makeText(getActivity(), currItem, Toast.LENGTH_SHORT).show();
                dialog.setTitle(currItem);
                dialog.show();
                return true;
            }
        });


        progress = (ProgressBar) rootView.findViewById(R.id.Progress_filterProgress);

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
                    classes.add(Control.classes.get(i).className);
                }

                Collections.sort(classes);
                adapter = new FilterAdapter(getActivity(), classes, null, 1);
                chooseClassesList.setAdapter(adapter);

                // set progress bar
                double score = 0.0;
                for (int i = 0; i < Control.classes.size(); i++) {
                    score += Control.classScores.get(Control.classes.get(i).className);
                }

                score /= Control.classes.size();
                score /= 5;
                score *= 100;

                Log.d("ASH", "score (Classes) =  " + score);

                progress.setMax(100);
                progress.setProgress((int) score);

                break;
            case 2:
                Collections.sort(subclasses);
                adapter = new FilterAdapter(getActivity(), subclasses, null, 2);
                chooseClassesList.setAdapter(adapter);
                next.setText("Next");

                // set progress bar
//                score = 0.0;
//                for (int i = 0; i < subclasses.size(); i++) {
//                    score += Control.subClassScores.get(subclasses.get(i));
//                }
//
//                score /= subclasses.size();
//                score *= 100;
//
                score = 0.0;
                for (int i = 0; i < chosenClasses.size(); i++) {
                    score += Control.classScores.get(chosenClasses.get(i));
                }

                score /= chosenClasses.size();
                score /= 5;
                score *= 100;

                Log.d("ASH", "score =  " + score);

                progress.setMax(100);
                progress.setProgress((int) score);

                break;
            case 3:
                // Show topics of chosen subclasses
                Collections.sort(topics);
//                chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, topics);
//                chooseClassesList.setAdapter(chooseClassesListAdapter);
                adapter = new FilterAdapter(getActivity(), topics, null, 3);
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

        public void updateCategory(int mode, String category, boolean reset, boolean current);
    }
}
