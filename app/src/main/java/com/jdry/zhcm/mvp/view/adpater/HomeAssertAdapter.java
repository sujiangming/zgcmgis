package com.jdry.zhcm.mvp.view.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.AssetBean;
import com.jdry.zhcm.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class HomeAssertAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<AssetBean> mData;

    public HomeAssertAdapter(Context context, List<AssetBean> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public void setItemData(List<AssetBean> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return mData.get(parentPosition).getChildrens() == null ? 0 : mData.get(parentPosition).getChildrens().size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mData.get(parentPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getChildrens().get(childPosition);
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
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_home_item_parent_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.title);
            groupViewHolder.ivDirection = convertView.findViewById(R.id.kpi_back_img);
            groupViewHolder.view_line_top = convertView.findViewById(R.id.view_line_top);
            groupViewHolder.view_line_bottom = convertView.findViewById(R.id.view_line_bottom);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(mData.get(groupPosition).getDeviceName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name);
            childViewHolder.tvCount = convertView.findViewById(R.id.tv_alarm_count);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(Utils.getPhaseDesc(mData.get(groupPosition).getChildrens().get(childPosition).getPhaseNo()));
        childViewHolder.tvCount.setText(mData.get(groupPosition).getChildrens().get(childPosition).getAlarmCount());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
        ImageView ivDirection;
        View view_line_top;
        View view_line_bottom;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        TextView tvCount;
    }
}
