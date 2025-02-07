package com.legendmp.mad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalcGridAdapter extends BaseAdapter {
    String[] items;
    Context context;

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.btn_calc,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.calc_btn_text);
        textView.setText(items[position]);
        return convertView;
    }
}
