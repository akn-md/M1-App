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

import java.util.ArrayList;
import java.util.Collections;


public class FilterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // ListView
    ListView chooseClassesList;
    ArrayAdapter<String> chooseClassesListAdapter;
    Button next;
    boolean sc;

    // Chosen filters
    ArrayList<String> chosenClasses, subclasses, chosenSubclasses;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        for(int i = 0; i < Control.classes.size(); i++) {
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
                if(sc) {
                    // TODO: Make sure user has selected at least one subclass

                    // Get selected subclasses
                    chosenSubclasses = new ArrayList<String>();

                    for (int i = 0; i < subclasses.size(); i++) {
                        if (chooseClassesList.isItemChecked(i)) {
                            chosenSubclasses.add(subclasses.get(i));
                        }
                    }

                    mListener.onQuizFiltered(chosenClasses, chosenSubclasses);

                } else {
                    // TODO: Make sure user has selected at least one class

                    // Get selected classes
                    chosenClasses = new ArrayList<String>();
                    subclasses = new ArrayList<String>();

                    for (int i = 0; i < Control.classes.size(); i++) {
                        if (chooseClassesList.isItemChecked(i)) {
                            chosenClasses.add(Control.classes.get(i).className);
                            for(int j = 0; j < Control.classes.get(i).subclasses.size(); j++) {
                                subclasses.add(Control.classes.get(i).subclasses.get(j));
                            }
                        }
                    }

                    Collections.sort(subclasses);
                    sc = true;
                    chooseClassesListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, subclasses);
                    chooseClassesList.setAdapter(chooseClassesListAdapter);

                    next.setText("Start Quiz");
                }
            }
        });

        // Inflate the layout for this fragment
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onQuizFiltered(ArrayList<String> chosenClasses, ArrayList<String> chosenSubclasses);
    }
}
