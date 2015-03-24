/**
 * AutoSMSReceiver - Broadcast receiver responsible for handling incoming 
 * SMS messages and auto responding if enabled. 
 * 
 */

package com.yasar.yasar;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class AutoSMSReceiver extends BroadcastReceiver {

	private String message = null;
	private String sender = null;
	private Bundle extras = null;
	private SharedPreferences sp;
	private DBController controller;
	private ArrayList<Contact> contacts;

	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences(Constants.SHAREDPREFNAME, 0);

		controller = DBController.getInstance(context);
		contacts = controller.getAllContacts();

		extras = intent.getExtras();
		if (extras == null)
			return;
		autoRespond();
	}

	// When the system notifies AutoSMSReceiver of a message, this method
	// parses the PDU to extract the sender, iterates through the contacts
	// in the DB looking for a match. If a match is found, it reads the
	// response message from SharedPreferences and sends that message to
	// the sender.
	private void autoRespond() {

		// Parse PDU Object and extract sender
		Object[] pdus = (Object[]) extras.get("pdus");
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
			sender = SMessage.getOriginatingAddress();
		}

		// Check for match against contact list
		boolean match = false;
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).getNumber().equals(sender)) {
				match = true;
			} }

		// Respond if there is a match and one of the checkboxes is checked
		if (match && !sp.getString(Constants.CHECKBOX_ID, "0").equals("0")) {
			message = sp.getString(Constants.RESPONCE, "Default responce");
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(sender, null, message, null, null);
		}

	}
}
