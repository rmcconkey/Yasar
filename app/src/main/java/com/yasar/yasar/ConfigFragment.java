package com.yasar.yasar;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ConfigFragment extends Fragment {

    private CheckBox firstCB, secondCB, thirdCB;
    private EditText firstET, secondET, thirdET;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private View rootView;

    public static ConfigFragment newInstance() {
        ConfigFragment fragment = new ConfigFragment();
        return fragment;
    }

    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.config_fragment, container, false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firstCB = (CheckBox) rootView.findViewById(R.id.checkBox1);
        secondCB = (CheckBox) rootView.findViewById(R.id.checkBox2);
        thirdCB = (CheckBox) rootView.findViewById(R.id.checkBox3);

        firstET = (EditText) rootView.findViewById(R.id.editText1);
        secondET = (EditText) rootView.findViewById(R.id.editText2);
        thirdET = (EditText) rootView.findViewById(R.id.editText3);

        sp = getActivity().getSharedPreferences(Constants.SHAREDPREFNAME, 0);
        editor = sp.edit();

        // Create ad view
        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreConfig();
    }

    // When any checkbox is changed, this method checks to identify
    // which checkbox is enabled, if any, then unchecks any currently
    // checked box. It then updates the SharedPreferences to indicate
    // which checkbox is enabled and what message to send.
    public void checked(View v) {

        switch (v.getId()) {

            case R.id.checkBox1:
                secondCB.setChecked(false);
                thirdCB.setChecked(false);
                if (firstCB.isChecked()) {
                    editor.putString(Constants.RESPONCE, firstET.getText()
                            .toString());
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_1);
                } else {
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_NONE);
                }
                editor.commit();
                break;
            case R.id.checkBox2:
                firstCB.setChecked(false);
                thirdCB.setChecked(false);
                if (secondCB.isChecked()) {
                    editor.putString(Constants.RESPONCE, secondET.getText()
                            .toString());
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_2);
                } else {
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_NONE);
                }
                editor.commit();
                break;
            case R.id.checkBox3:
                firstCB.setChecked(false);
                secondCB.setChecked(false);
                if (thirdCB.isChecked()) {
                    editor.putString(Constants.RESPONCE, thirdET.getText()
                            .toString());
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_3);
                } else {
                    editor.putString(Constants.CHECKBOX_ID, Constants.CHECKBOX_NONE);
                }
                editor.commit();
                break;
            default:
                break;

        }

    }

    // Each time this fragment gains focus, this method is called to
    // read the SharedPreferences and set the checkboxes accordingly
    private void restoreConfig() {
        switch (sp.getString(Constants.CHECKBOX_ID, "0")) {
            case Constants.CHECKBOX_NONE:
                firstCB.setChecked(false);
                secondCB.setChecked(false);
                thirdCB.setChecked(false);
                break;
            case Constants.CHECKBOX_1:
                firstCB.setChecked(true);
                secondCB.setChecked(false);
                thirdCB.setChecked(false);
                break;
            case Constants.CHECKBOX_2:
                firstCB.setChecked(false);
                secondCB.setChecked(true);
                thirdCB.setChecked(false);
                break;
            case Constants.CHECKBOX_3:
                firstCB.setChecked(false);
                secondCB.setChecked(false);
                thirdCB.setChecked(true);
                break;
            default:
                firstCB.setChecked(false);
                secondCB.setChecked(false);
                thirdCB.setChecked(false);

        }
    }

}
