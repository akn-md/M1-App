package m1.nayak.m1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseAnalytics;
import com.parse.ParseException;

import java.util.ArrayList;

import m1.nayak.m1.backend.Query;
import m1.nayak.m1.backend.Update;
import m1.nayak.m1.objects.Question;


// TODO: Change button name based on what is checked. If General Knowledge is checked, name is "Next", if not, name is "Start Quiz"
// TODO: Add settings page for following parameters: quiz length, smart quizzing, flash card grading

public class MainActivity extends ActionBarActivity implements FilterFragment.OnFragmentInteractionListener, ResultsFragment.OnFragmentInteractionListener, QuizConfigureFragment.OnFragmentInteractionListener, QuizChoiceFragment.OnFragmentInteractionListener, QuizQuestionFragment.OnFragmentInteractionListener {

    public String fragmentLevel;
    public boolean fragmentMC;

    // for AsyncTasks
    ProgressDialog dialog;

    // quiz parameters
    ArrayList<String> chosenClasses;
    ArrayList<String> chosenSubclasses;
    ArrayList<String> chosenCategories;
    boolean smartQuiz;

    // quiz navigation
    int curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpened(getIntent());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        if (Control.classes.size() == 0)
            new LoadClasses().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayView(int position, boolean forward) {

        Fragment fragment = null;

        switch (position) {
            // Main menu
            case 0:
                fragment = new PlaceholderFragment();
                break;
            // Quiz settings
            case 1:
                fragment = new QuizConfigureFragment();
                break;
            // Quiz question
            case 2:
                fragment = QuizQuestionFragment.newInstance(false, curr, Control.questions.size());
                break;
            // Quiz results
            case 3:
                fragment = new ResultsFragment();
                break;
            case 4:
                fragment = new FilterFragment();
            case 5:
                fragmentLevel = "main";
                fragmentMC = false;
                break;
            // Choose Classes
            case 6:
                fragmentLevel = "class";
                fragmentMC = true;
                break;
            default:
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (forward)
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        else
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onChoiceSelected(int position) {

        if (fragmentLevel.equals("main")) {
            switch (position) {
                // General Knowledge
                case 0:
//                    displayView(1);
                    break;
                // Diseases
                case 1:
                    break;
                // Enzymes
                case 2:
                    break;
                // Drugs
                case 3:
                    break;
                // Hormones
                case 4:
                    break;
            }

            Fragment fragment = new QuizQuestionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
//                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onQuizConfigured(ArrayList<String> categories, boolean smart) {
        chosenCategories = categories;
        smartQuiz = smart;

        // TODO: Only show view 4 (class filters) if General Knowledge category is checked, otherwise start quiz

        displayView(4, true);
    }

    @Override
    public void onNextPressed(int c, boolean lastQuestion) {

        if (!lastQuestion) {
            curr++;
            displayView(2, true);
        } else {
            // calculate new scores
            updateScores();

            // open quiz results page
            displayView(3, true);
        }
    }

    @Override
    public void onPrevPressed(int c) {
        curr--;
        displayView(2, false);
    }

    @Override
    public void onResultsClosed(boolean save) {
        if (save) {
            new UploadResults().execute();
        } else {
            displayView(0, false);
        }
    }

    @Override
    public void onQuizFiltered(ArrayList<String> cC, ArrayList<String> cSC) {
        chosenClasses = cC;
        chosenSubclasses = cSC;

        Control.questions.clear();

        Log.d("ASH", "Chosen classes:");
        for (String s : chosenClasses) {
            Log.d("ASH", s);
        }

        Log.d("ASH", "Chosen subclasses:");
        for (String s : chosenSubclasses) {
            Log.d("ASH", s);
        }

        Log.d("ASH", "Chosen categories:");
        for (String s : chosenCategories) {
            Log.d("ASH", s);
        }

        Log.d("ASH", "Smart quiz = " + smartQuiz);

        dialog = new ProgressDialog(this);
        new GetQuestions().execute();
    }

    public void updateScores() {
        // TODO: factor time into score update

        for(int i = 0; i < Control.questions.size(); i++) {
            Question q = Control.questions.get(i);
            int score = q.score;

            if(q.answeredCorrectly) {
                score += 20;
                if (score > 100)
                    score = 100;
                q.score = score;
            } else {
                score -= 20;
                if (score < 0)
                    score = 0;
                q.score = score;
            }
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    // Button listeners
    public void quizMe(View view) {
        displayView(1, true);
    }

    class GetQuestions extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Creating quiz ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            try {
                Query.getData(chosenSubclasses, chosenCategories, smartQuiz);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
//                    if (Control.questions == null) {
//                        Toast.makeText(getActivity(), "Error with query", Toast.LENGTH_LONG).show();
//                    } else if (Control.questions.size() == 0) {
//                        Toast.makeText(getActivity(), "No questions match criteria!", Toast.LENGTH_LONG).show();
//                    } else {
//                        Intent i = new Intent(getActivity(), ReviewActivity.class);
//                        i.putExtra("numQuestions", Control.questions.size());
//                        startActivity(i);
//                    }
                    curr = 0;
                    displayView(2, true);
                }
            });
        }
    }

    class UploadResults extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Uploading results ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Update.updateScores();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    displayView(0, false);
                }
            });
        }
    }

    class LoadClasses extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Query.loadClasses();
            return null;
        }
    }
}
