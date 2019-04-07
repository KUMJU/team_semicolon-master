package com.semicolon.project.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.BaseAdapter;


public class SettingPreferenceFragment extends PreferenceFragment {

    SharedPreferences prefs;
    //SwitchPreference alert;
    //ListPreference alert_days;
    // 테스트
    public Context context = MainActivity.getAppContext(); // context 지역변수 설정

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_preference);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //alert = (SwitchPreference)findPreference("alert_switch");
        //alert_days = (ListPreference)findPreference("alert_date");

        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }// onCreate

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if(key.equals("alert_switch")){

                if(prefs.getBoolean("alert_switch", true)){
                    Log.d("알림스위치", "on");

                }else{
                    Log.d("알림스위치", "off");
                }

                //2뎁스 PreferenceScreen 내부에서 발생한 환경설정 내용을 2뎁스 PreferenceScreen에 적용하기 위한 소스
                ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }

            //추가
            if(key.equals("alert_date")){

                //Log.d("alert_date", "new section id = " + alert_days.getValue());
                Log.d("alert_date1", "new section id = " + prefs.getString("alert_date","3"));
                //String a = prefs.getString("alert_date","3");
            }


        }
    };

}