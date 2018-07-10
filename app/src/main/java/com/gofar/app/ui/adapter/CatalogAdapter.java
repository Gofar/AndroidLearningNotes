package com.gofar.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gofar.app.db.entity.CatalogEntity;

import java.util.List;

/**
 * @author lcf
 * @date 2018/7/10 15:36
 * @since 1.0
 */
public class CatalogAdapter extends BaseExpandableListAdapter {
    private List<String> mGroupData;
    private List<List<CatalogEntity>> mChildData;
    private int mGroupLayoutId = android.R.layout.simple_expandable_list_item_1;
    private int mChildLayoutId = android.R.layout.simple_list_item_1;

    private LayoutInflater mInflater;

    public CatalogAdapter(Context context, List<String> groupData, List<List<CatalogEntity>> childData) {
        mGroupData = groupData;
        mChildData = childData;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View group;
        if (convertView == null) {
            group = mInflater.inflate(mGroupLayoutId, parent, false);
        } else {
            group = convertView;
        }
        TextView textView = group.findViewById(android.R.id.text1);
        textView.setText(mGroupData.get(groupPosition));
        return group;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View child;
        if (convertView == null) {
            child = mInflater.inflate(mChildLayoutId, parent, false);
        } else {
            child = convertView;
        }
        TextView textView = child.findViewById(android.R.id.text1);
        CatalogEntity entity = mChildData.get(groupPosition).get(childPosition);
        textView.setText(entity.getTitle());
        return child;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
