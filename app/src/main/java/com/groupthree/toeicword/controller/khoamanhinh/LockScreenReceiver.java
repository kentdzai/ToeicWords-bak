package com.groupthree.toeicword.controller.khoamanhinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;

public class LockScreenReceiver extends BroadcastReceiver implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Intent itLockScreenService;
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.registerOnSharedPreferenceChangeListener(this);
        editor = pref.edit();
        itLockScreenService = new Intent(context, LockScreenService.class);
            if (null != context) {
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {

                    TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    boolean isPhoneIdle = tManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;
                    if (isPhoneIdle) {
                        id = 1;
                        itLockScreenService.putExtra("id", id);
                        context.startService(itLockScreenService);
                    }
                }
            }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
