package m1.nayak.m1.custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1.nayak.m1.Control;
import m1.nayak.m1.R;

public class FilterAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> items;
    private ArrayList<Double> percentages;

    public FilterAdapter(Context context, ArrayList<String> items, ArrayList<Double> percentages) {
        this.context = context;
        this.items = items;
        this.percentages = percentages;
        Control.selectedItems = new boolean[items.size()];
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
        final int pos = position;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_row_filter, parent, false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    Control.selectedItems[pos] = false;
                } else {
                    v.setSelected(true);
                    Control.selectedItems[pos] = true;
                }
            }
        });
        TextView item = (TextView) convertView.findViewById(R.id.TextView_class);
        item.setText(items.get(position));

        return convertView;
    }
}
