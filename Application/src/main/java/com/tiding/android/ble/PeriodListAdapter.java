package com.tiding.android.ble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiding.android.ble.param.PeriodSetting;

public class PeriodListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private PeriodSetting[] mList = {};

    PeriodListAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public void setmList(PeriodSetting[] mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    public void setmListItem(int position, PeriodSetting setting) {
        PeriodSetting[] mList = this.mList;
        if (position >= mList.length) {
            return;
        }
        mList[position] = setting;
        this.notifyDataSetChanged();
    }

    public PeriodSetting getmListItem(int position) {
        PeriodSetting[] mList = this.mList;
        if (position >= mList.length) {
            return null;
        }
        return mList[position];
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_daysetting, null);
            viewHolder = new ViewHolder();
            viewHolder.numberTextView = convertView.findViewById(R.id.number);
            viewHolder.periodTextView = convertView.findViewById(R.id.period);
            viewHolder.openSecondsTextView = convertView.findViewById(R.id.open_seconds);
            viewHolder.stopMinsTextView = convertView.findViewById(R.id.stop_mins);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PeriodSetting setting = mList[position];
        viewHolder.periodTextView.setText(String.format("%02d:%02d--%02d:%02d",
                setting.getmStartTimeHour(),
                setting.getmStartTimeMinute(),
                setting.getmStopTimeHour(),
                setting.getmStopTimeMinute()
        ));
        viewHolder.openSecondsTextView.setText(String.format("开 %02d 分", setting.getmStartDurationSeconds()));
        viewHolder.stopMinsTextView.setText(String.format("关 %02d 分", setting.getmStopDurationMins()));
        viewHolder.numberTextView.setText(String.format("%02d.", position + 1));

        return convertView;
    }

    private class ViewHolder {
        TextView numberTextView, periodTextView, openSecondsTextView, stopMinsTextView;
    }
}
