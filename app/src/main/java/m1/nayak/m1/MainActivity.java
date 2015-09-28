package m1.nayak.m1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;

import java.util.ArrayList;

import m1.nayak.m1.backend.Query;
import m1.nayak.m1.backend.Update;
import m1.nayak.m1.objects.Question;


// TODO: Change button name based on what is checked. If General Knowledge is checked, name is "Next", if not, name is "Start Quiz"
// TODO: Add settings page for following parameters: quiz length, smart quizzing, flash card grading

public class MainActivity extends ActionBarActivity implements DailyFragment.OnFragmentInteractionListener, FilterFragment.OnFragmentInteractionListener, ResultsFragment.OnFragmentInteractionListener, QuizConfigureFragment.OnFragmentInteractionListener, QuizChoiceFragment.OnFragmentInteractionListener, QuizQuestionFragment.OnFragmentInteractionListener {

    public String fragmentLevel;
    public boolean fragmentMC;

    // for AsyncTasks
    ProgressDialog dialog;

    // for username
    Dialog userDialog;
    private EditText username;
    private Button confirmUser;

    // quiz parameters
    ArrayList<String> chosenClasses;
    ArrayList<String> chosenSubclasses;
    ArrayList<String> chosenTopics;
    int mode;

    // quiz navigation
    int curr;

    ActionBar actionBar;


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

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1abc9c")));

        userDialog = new Dialog(this);
        userDialog.setContentView(R.layout.dialog_user);
        username = (EditText) userDialog.findViewById(R.id.UserDialog_ET1);
        confirmUser = (Button) userDialog.findViewById(R.id.UserDialog_confirm);
        confirmUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                if (user.length() > 0) {
                    Control.user = user;

                    // save user
                    SharedPreferences fields = getPreferences(0);
                    SharedPreferences.Editor editor = fields.edit();
                    editor.putString("user", user);

                    userDialog.dismiss();
                    editor.commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Please specify a username", Toast.LENGTH_LONG).show();
                }
            }
        });

        userDialog.setCancelable(false);
        userDialog.setTitle("Who are you?");

        if (Control.user == null)
            userDialog.show();

        // load user
        SharedPreferences fields = getPreferences(0);
        if (fields.contains("user")) {
            username.setText(fields.getString("user", ""));
        }
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
            // Daily question
            case 7:
                fragment = new DailyFragment();
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
    public void onQuizConfigured(int option) {
//        chosenCategories = categories;
//        smartQuiz = smart;
//
//        // TODO: Only show view 4 (class filters) if General Knowledge category is checked, otherwise start quiz


//        displayView(4, true);

        mode = option;
        dialog = new ProgressDialog(this);
        new GetQuestions().execute();
    }

    @Override
    public void onNextPressed(int c, boolean lastQuestion, int rating) {

        Log.d("ASH", "Rating = " + rating);
        if (!lastQuestion) {
            curr++;
            displayView(2, true);
        } else {
            // calculate new scores
//            updateScores();

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
    public void onQuizFiltered(ArrayList<String> cC, ArrayList<String> cSC, ArrayList<String> cT) {
        chosenClasses = cC;
        chosenSubclasses = cSC;
        chosenTopics = cT;

        Control.questions.clear();

        Log.d("ASH", "Chosen classes:");
        for (String s : chosenClasses) {
            Log.d("ASH", s);
        }

        Log.d("ASH", "Chosen subclasses:");
        for (String s : chosenSubclasses) {
            Log.d("ASH", s);
        }

        Log.d("ASH", "Chosen topics:");
        for (String s : chosenTopics) {
            Log.d("ASH", s);
        }

//        Log.d("ASH", "Chosen categories:");
//        for (String s : chosenCategories) {
//            Log.d("ASH", s);
//        }
//
//        Log.d("ASH", "Smart quiz = " + smartQuiz);

        displayView(1, true);

//        dialog = new ProgressDialog(this);
//        new GetQuestions().execute();
    }

    @Override
    public void updateCategory(int mode, String category, boolean reset, boolean current) {
        String[] data = {""+mode, category, String.valueOf(reset), String.valueOf(current)};
        new UpdateCategory().execute(data);
    }

    public void updateScores() {
        // TODO: factor time into score update

        for (int i = 0; i < Control.questions.size(); i++) {
            Question q = Control.questions.get(i);
            double score = q.score;

            if (q.answeredCorrectly) {
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

    @Override
    public void onDailyQuestionsStarted(int numQuestions, int percentageCurrent) {
        String[] data = {""+numQuestions, ""+percentageCurrent};
        new DailyQuestions().execute(data);
    }

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
        displayView(4, true);
    }

    public void dailyQuestions(View view) {
        displayView(7, true);
    }

    class DailyQuestions extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Creating quiz ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            try {
                Query.getDailyQuestions(args);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Control.questions.size() > 0) {
                        curr = 0;
                        displayView(2, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "No questions found!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    class GetQuestions extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Creating quiz ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            try {
//                Query.getData(chosenSubclasses, chosenTopics, chosenCategories, mode);
                Query.getGKQuestions(chosenSubclasses, chosenTopics, mode);
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

                    if(Control.questions.size() > 0) {
                        curr = 0;
                        displayView(2, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "No questions found!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    class UpdateCategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Updating ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Update.updateCategory(params);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
        }
    }

    class UploadResults extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
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

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Updating...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            Query.loadClasses();
            dialog.dismiss();
            Query.loadSubClasses();

            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
        }
    }

    class LoadSubclasses extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            Query.loadSubClasses();
            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
        }
    }
}
