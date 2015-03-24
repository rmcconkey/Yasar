/**
 * RespondListAdapter - handles ArrayList<Contact> for use with 
 * ListView of chosen contacts
 */

package com.yasar.yasar;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class RespondListAdapter extends BaseAdapter {
	
	// Adapter needs reference to DB when removing
	// contact from respond list
	private DBController controller;
	
	private static LayoutInflater inflater = null;

	private static ArrayList<Contact> contactArrayList;

	public RespondListAdapter(Context context, ArrayList<Contact> list) {
		contactArrayList = list;
		inflater = LayoutInflater.from(context);
		controller = DBController.getInstance(context);

	}
	
	@Override
	public int getCount() {
		return contactArrayList.size();
	}

	@Override
	public Contact getItem(int position) {
		return contactArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add(Contact contact) {
		contactArrayList.add(contact);
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		controller.deleteContact(contactArrayList.get(position).getId());
		contactArrayList.remove(position);
		notifyDataSetChanged();
		
		return;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
			holder.number = (TextView) convertView.findViewById(R.id.numberTextView);
			holder.removeBtn = (ImageButton) convertView.findViewById(R.id.removeBtn);
			holder.removeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					remove(holder.position);
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
				
		holder.name.setText(contactArrayList.get(position).getName());
		holder.number.setText(formatPhoneNumber(contactArrayList.get(position).getNumber()));
		holder.position = position;
		
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView number;
		ImageButton removeBtn;
		int position;
	}
	
	private String formatPhoneNumber(String number) {
		String result = "(";

		result += number.substring(0, 3) + ")";
		result += number.substring(3, 6) + "-";
		result += number.substring(6);
		
		return result;
	}

}
