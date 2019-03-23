package com.example.lorebase.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.example.lorebase.AlarmService;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.ui.activity.MainActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

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
                if (stringValue.contains("true")) {
//                SkinCompatManager.getInstance().loadSkin("night.skin", 0);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    login();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    login();
                }
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
    public void onDestroy() {
        super.onDestroy();
    }

    private void login() {
        SharedPreferences sp = getActivity().getSharedPreferences(ConstName.LOGIN_DATA, MODE_PRIVATE);
        String userName = sp.getString(ConstName.USER_NAME, "");
        String password = sp.getString(ConstName.PASS_WORD, "");
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<User> loginCall = api.login(userName, password);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        SharedPreferences.Editor editor;
                        editor = sp.edit();
                        editor.putBoolean(ConstName.IS_LOGIN, true); //存储登陆状态的Boolean
                        editor.apply(); //提交保存数据
                        L.v("skinskin","重新登陆了");
                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                ToastUtil.showShortToastCenter(t.getMessage(),getActivity());
            }
        });
    }
}
