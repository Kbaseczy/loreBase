package com.example.lorebase.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.example.lorebase.AlarmService;
import com.example.lorebase.R;
import com.example.lorebase.ui.activity.MySettingActivity;
import com.example.lorebase.util.L;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */

//todo 夜间模式  switch按钮逻辑有问题
public class MySettingActivityFragment extends PreferenceFragment {
    Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else if (preference instanceof SwitchPreference) {
            if (preference.getKey().equals("setting_switch_skin")) {
//                if (stringValue.contains("true")) {
////                SkinCompatManager.getInstance().loadSkin("night.skin", 0);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
            } else if (preference.getKey().equals("setting_switch")) {
                if (stringValue.contains("true")) {
                    L.v("setting_switch", "startService");
                    preference.getContext().startService(new Intent(preference.getContext(), AlarmService.class));
                } else {
                    preference.getContext().stopService(new Intent(preference.getContext(), AlarmService.class));
                }
            }
        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    };

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof ListPreference || preference instanceof EditTextPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        } else {
            //接口回调
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        bindPreferenceSummaryToValue(findPreference("example_text"));
        bindPreferenceSummaryToValue(findPreference("example_list"));
        bindPreferenceSummaryToValue(findPreference("setting_switch_skin"));
        bindPreferenceSummaryToValue(findPreference("setting_switch"));

    }

    @Override
    public void onResume() {
        setNightMode();
        super.onResume();
    }

    private void setNightMode() {
        if (PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getBoolean("setting_switch_skin", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            startActivity(new Intent(getActivity(), MySettingActivity.class));
//            getActivity().overridePendingTransition(R.animator.go_in, R.animator.go_out);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            startActivity(new Intent(getActivity(), MySettingActivity.class));
//            getActivity().overridePendingTransition(R.animator.go_in, R.animator.go_out);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}