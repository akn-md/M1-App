package m1.nayak.m1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


// TODO: Add action bar title "Choose classes" or "Choose topics"

public class FilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // ListView
    ListView chooseClassesList;
    ArrayAdapter<String> chooseClassesListAdapter;
    Button next;
    boolean sc;

    // Chosen filters
    ArrayList<String> chosenClasses, subclasses, chosenSubclasses;

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

        // get names of classes
        ArrayList<String> c = new ArrayList<String>(Control.classes.size());
        for (int i = 0; i < Control.classes.size(); i++) {
            c.add(Control.classes.get(i).className);
        }
        Collections.sort(c);

        chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, c);
        chooseClassesList.setAdapter(chooseClassesListAdapter);
        sc = false;

        // Next filter
        next = (Button) rootView.findViewById(R.id.Button_filter_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sc) {
                    // Get selected subclasses
                    chosenSubclasses = new ArrayList<String>();

                    for (int i = 0; i < subclasses.size(); i++) {
                        if (chooseClassesList.isItemChecked(i)) {
                            chosenSubclasses.add(subclasses.get(i));
                        }
                    }

                    if (chosenSubclasses.size() == 0) {
                        Toast.makeText(getActivity(), "Select at least one subclass", Toast.LENGTH_LONG).show();
                    } else {
                        mListener.onQuizFiltered(chosenClasses, chosenSubclasses);
                    }
                } else {
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
                        Collections.sort(subclasses);
                        sc = true;
                        chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, subclasses);
                        chooseClassesList.setAdapter(chooseClassesListAdapter);

                        next.setText("Start Quiz");
                    }
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

    public interface OnFragmentInteractionListener {
        public void onQuizFiltered(ArrayList<String> chosenClasses, ArrayList<String> chosenSubclasses);
    }
}
