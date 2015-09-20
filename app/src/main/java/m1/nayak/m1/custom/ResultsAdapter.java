package m1.nayak.m1.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import m1.nayak.m1.Control;
import m1.nayak.m1.R;

/**
 * Created by Ashwin on 12/20/14.
 */
public class ResultsAdapter extends BaseAdapter {

    private Context context;
    private String[] topics;
    private String[] results;
    private String[] proficiencies;

    public ResultsAdapter(Context context) {
        this.context = context;
        calculateResults();
    }

    @Override
    public int getCount() {
        return topics.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_row_results, null);
        }

        TextView topic = (TextView) convertView.findViewById(R.id.TextView_topic);
        TextView result = (TextView) convertView.findViewById(R.id.TextView_result);
        TextView proficiency = (TextView) convertView.findViewById(R.id.TextView_proficiency);

        topic.setText(topics[position]);
        result.setText(results[position]);
        proficiency.setText(proficiencies[position]);

        if (position == 0) {
            topic.setTypeface(Typeface.DEFAULT_BOLD);
            result.setTypeface(Typeface.DEFAULT_BOLD);
            proficiency.setTypeface(Typeface.DEFAULT_BOLD);
        }

        return convertView;
    }

    public void calculateResults() {
        HashMap<String, Integer> correct = new HashMap<String, Integer>();
        HashMap<String, Integer> total = new HashMap<String, Integer>();
        HashMap<String, Integer> score = new HashMap<String, Integer>();
        for (int i = 0; i < Control.questions.size(); i++) {
            Log.d("ASH", "-----");
            Log.d("ASH", "Subject = " + Control.questions.get(i).subject);
            Log.d("ASH", "Topic = " + Control.questions.get(i).topic);
            Log.d("ASH", "score = " + Control.questions.get(i).score);
            Log.d("ASH", "answered correctly = " + Control.questions.get(i).answeredCorrectly);

            if (total.containsKey(Control.questions.get(i).topic)) {
                total.put(Control.questions.get(i).topic, (total.get(Control.questions.get(i).topic) + 1));
                score.put(Control.questions.get(i).topic, (score.get(Control.questions.get(i).topic) + Control.questions.get(i).score));
            } else {
                total.put(Control.questions.get(i).topic, 1);
                score.put(Control.questions.get(i).topic, Control.questions.get(i).score);
            }

            if (correct.containsKey(Control.questions.get(i).topic)) {
                if (Control.questions.get(i).answeredCorrectly)
                    correct.put(Control.questions.get(i).topic, (correct.get(Control.questions.get(i).topic) + 1));
            } else {
                if (Control.questions.get(i).answeredCorrectly)
                    correct.put(Control.questions.get(i).topic, 1);
                else
                    correct.put(Control.questions.get(i).topic, 0);
            }
        }

        topics = new String[total.size() + 1];
        results = new String[total.size() + 1];
        proficiencies = new String[total.size() + 1];

        topics[0] = "TOPIC";
        results[0] = "NUMBER OF QUESTIONS";
        proficiencies[0] = "MASTERY";

        int count = 1;
        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            topics[count] = entry.getKey();
//            results[count] = correct.get(entry.getKey()) + "/" + entry.getValue();
            results[count] = "" + entry.getValue();

            double mastery = ((double) score.get(entry.getKey()) / (double) entry.getValue());
            DecimalFormat df = new DecimalFormat("#.##");
            mastery = Double.valueOf(df.format(mastery));
            proficiencies[count] = mastery + "";

            Log.d("ASH", "Topic = " + entry.getKey() + ", Num Questions = " + entry.getValue() + ",  Average score = " + mastery);
            count++;
        }
    }
}
