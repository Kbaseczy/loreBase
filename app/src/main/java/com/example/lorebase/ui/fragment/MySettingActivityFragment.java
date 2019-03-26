package com.example.lorebase.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.example.lorebase.AlarmService;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.User;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.ui.activity.MySettingActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PreferencesUtil;
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
public class MySettingActivityFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
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
        bindPreferenceSummaryToValue(findPreference("username"));
        bindPreferenceSummaryToValue(findPreference("example_list"));
        findPreference("setting_switch_skin").setOnPreferenceClickListener(this);
        findPreference("checkbox_notify").setOnPreferenceClickListener(this);
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
//                        SharedPreferences.Editor editor;
//                        editor = sp.edit();
//                        editor.putBoolean(ConstName.IS_LOGIN, true); //存储登陆状态的Boolean
//                        editor.apply(); //提交保存数据
                        PreferencesUtil.putIsLogin(getActivity(),true);
                        L.v("skinskin", "重新登陆了");
                        ToastUtil.showShortToastCenter(response.body().getErrorMsg(),getActivity());
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                ToastUtil.showShortToastCenter(t.getMessage(), getActivity());
            }
        });
    }

    //点击事件，取消之前根据值改变做出的操作--每次进入界面都会调用change，过于频繁
    @Override
    public boolean onPreferenceClick(Preference preference) {
        boolean is_auto = PreferencesUtil.getIsAuto(getActivity());
        boolean is_Login = PreferencesUtil.getIsLogin(getActivity());
        if (preference instanceof SwitchPreference) {
            if (PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getBoolean(preference.getKey(), false)) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //这里存在一个bug，夜间模式切换后，登陆态会丢失，所以必须记住密码，不管是否勾选自动登陆
                //todo 解决办法：1.强制记住用户名，密码   2.数据保护，日夜间模式切换后，进行数据恢复--这种办法是从Bundle中读取数据（用户名，密码）
                getActivity().startActivity(new Intent(getActivity(),MySettingActivity.class));  //跳转当前界面，实现刷新
                getActivity().finish();
                if (is_auto && !is_Login) login();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getActivity().startActivity(new Intent(getActivity(),MySettingActivity.class));
                getActivity().finish();
                if (is_auto && !is_Login) login();
            }
        } else if (preference instanceof CheckBoxPreference) {
            if (PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getBoolean(preference.getKey(), false)) {
                //todo 勾选后，出现立即通知的情况，需要在设定时间到之后才通知。解决立即通知的情况
                preference.getContext().startService(new Intent(preference.getContext(), AlarmService.class));
            } else {
                preference.getContext().stopService(new Intent(preference.getContext(), AlarmService.class));
            }
        }
        return false;
    }
}
