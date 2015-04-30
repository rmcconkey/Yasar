/**
 * ContactsFragment - displays list of contacts to auto respond
 *
 * Permits user to add or remove contacts
 */

package com.yasar.yasar;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ContactsFragment extends Fragment implements OnItemClickListener,
        OnItemSelectedListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();

    private View rootView;

    private DBController controller;
    private Button addContactBtn;

    private AutoCompleteTextView acTextView = null;
    // Adapter for AutoCompleteTextView list
    private ArrayAdapter<String> dropdownAdapter;

    private ListView respondListView;
    private RespondListAdapter respondListAdapter;

    // List of contacts retrieved from system
    private ArrayList<Contact> contacts;
    // List of contacts to auto respond
    private ArrayList<Contact> respondList;

    // Currently selected contact from autocomplete list
    private int selected = -1;

    // Phone number to search through contacts for
    private String searchPhoneNumber;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contacts_fragment, container,
                false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addContactBtn = (Button) getActivity().findViewById(R.id.addBtn);
        controller = DBController.getInstance(getActivity());

        // Initialize AutoCompleteTextView values
        acTextView = (AutoCompleteTextView) getActivity().findViewById(
                R.id.autoCompleteTextView1);

        // Create adapter for AutoComplete
        dropdownAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.dropdown_item, R.id.dropdownTextView, new ArrayList<String>());

        // Set minimum number of chars to search
        acTextView.setThreshold(3);

        // Set adapter to AutoCompleteTextView
        acTextView.setAdapter(dropdownAdapter);
        acTextView.setOnItemSelectedListener(this);
        acTextView.setOnItemClickListener(this);

        // Read contact data and add data to an
        // ArrayAdapter used by AutoCompleteTextView
        readContactData();

        // Button Click pass AutoCompleteTextView object
        addContactBtn.setOnClickListener(addContact());

        // Setup adapter for respond listview
        respondListView = (ListView) getActivity().findViewById(R.id.listView1);

        // Create list from contacts in database then add to adapter
        respondList = controller.getAllContacts();
        respondListAdapter = new RespondListAdapter(getActivity(), respondList);

        respondListView.setAdapter(respondListAdapter);

        // Create ad view
        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void readContactData() {

        // Get the contacts from the phone ordered by their name.
        contacts = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(Phone.CONTENT_URI, null, null,
                null, Phone.DISPLAY_NAME + " ASC");
        // Populate the list of contacts that will serve as data for our list.
        if (cursor.moveToFirst()) {
            do {
                contacts.add(new Contact(cursor.getInt(cursor.getColumnIndex(Phone.CONTACT_ID)),
                        cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)),
                        formatPhoneNumber(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)))));
                dropdownAdapter.add(cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)) + "\n" +
                        cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private OnClickListener addContact() {
        return new OnClickListener() {
            public void onClick(View v) {

                long row = -1;

                if (selected < 0) {
                    Toast.makeText(getActivity(), "Please fill phone number",
                            Toast.LENGTH_SHORT).show();
                } else {
                    contacts.get(selected).setNumber(contacts.get(selected).getNumber());
                    row = controller.insertContact(contacts.get(selected));
                    respondListAdapter.add(contacts.get(selected));
                    acTextView.setText("");
                }

                if (BuildConfig.DEBUG) {
                    Toast.makeText(getActivity(), "Inserted into row " + Long.toString(row), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                .getWindowToken(), 0);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int index, long l) {

        int position = -1;

        // Name to search through contacts
        searchPhoneNumber = formatPhoneNumber(arg0.getItemAtPosition(index).toString());
        Log.d(TAG, searchPhoneNumber);

        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getNumber().equals(searchPhoneNumber)) {
                position = i;
                break;
            }
        }

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                .getWindowToken(), 0);

        selected = position;
    }

    private String formatPhoneNumber(String number) {
        String result = "";
        String[] splitResult;

        splitResult = number.split("\\D");

        if (splitResult.length < 1) {
            Log.d(TAG, "Bad value passed to formatPhoneNumber()");
            return null;
        }

        for (int i = 0; i < splitResult.length; i++) {
            result += splitResult[i];
        }

        // Strip the leading 1 from the number
        if (result.charAt(0) == '1') {
            result = result.substring(1);
        }

        return result;
    }
}
