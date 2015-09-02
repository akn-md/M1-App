package m1.nayak.m1.custom;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import m1.nayak.m1.R;

public class FilterAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> items;
    private ArrayList<Double> percentages;

    public FilterAdapter(Context context, ArrayList<String> items, ArrayList<Double> percentages) {
        this.context = context;
        this.items = items;
        this.percentages = percentages;
    }

    @Override
    public int getCount() {
        return items.size();
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
            convertView = mInflater.inflate(R.layout.listview_row_filter, parent, false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ASH", "view clicked");
                if(v.isSelected()) {
                    v.setSelected(false);
                } else {
                    v.setSelected(true);
                }
            }
        });
        TextView item =(TextView) convertView.findViewById(R.id.TextView_class);
        ProgressBar percentage = (ProgressBar) convertView.findViewById(R.id.ProgressBar_filter);

        item.setText(items.get(position));
        percentage.setProgress(50);
        percentage.setBackgroundResource(R.drawable.progress_red);

        return convertView;
    }
}
