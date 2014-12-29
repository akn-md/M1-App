package m1.nayak.m1.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1.nayak.m1.R;
import m1.nayak.m1.objects.FilterGroup;

/**
 * Created by Ashwin on 12/29/14.
 */
public class CustomExpListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<FilterGroup> classes;
    private LayoutInflater mInflater;

    public CustomExpListAdapter(Context context, ArrayList<FilterGroup> c) {
        mContext = context;
        classes = c;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return classes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classes.get(groupPosition).subclasses.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classes.get(groupPosition).subclasses.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.explistview_class, null);
        }

        // Get the group item
        FilterGroup group = (FilterGroup) getGroup(groupPosition);

        // Set group name
        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
        textView.setText(group.className);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.explistview_subclass, null);
        }

        // Get child name
        String children = (String) getChild(groupPosition, childPosition);

        // Set child name
        TextView text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(children);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
