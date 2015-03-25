package com.yasar.yasar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r_mcconkey on 3/25/15.
 */
public class DropDownListAdapter extends ArrayAdapter {

    private ArrayList<DropDownListItem> list;
    private int resource;
    private LayoutInflater inflater;

    public DropDownListAdapter(Context context, int resource) {
        super(context, resource);
        list = new ArrayList<DropDownListItem>();
        this.resource = resource;
    }

    public void add(DropDownListItem item) {
        list.add(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.number = (TextView) convertView.findViewById(R.id.numberTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.number.setText(list.get(position).getNumber());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView number;
    }

}
