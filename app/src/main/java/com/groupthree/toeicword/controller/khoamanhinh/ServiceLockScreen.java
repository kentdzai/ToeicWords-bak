package com.groupthree.toeicword.controller.khoamanhinh;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * Created by QuyetChu on 4/7/16.
 */
public class ServiceLockScreen extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{

    BroadcastReceiver receiver;
    private Context mContext = null;
    SharedPreferences pref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new LockScreenReceiver();
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        stateRecever(true);
        setLockGuard();

        return START_STICKY;
    }

    private void stateRecever(boolean isStartRecever) {
        if (isStartRecever) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(receiver, filter);
        } else {
            if (null != receiver) {
                unregisterReceiver(receiver);
            }
        }
    }

    private void setLockGuard() {
        initKeyguardService();
        if (pref.getBoolean("khoa_man_hinh", false)) {
            setStandardKeyguardState(false);
        } else {
            setStandardKeyguardState(true);
        }
    }

    private KeyguardManager mKeyManager = null;
    private KeyguardManager.KeyguardLock mKeyLock = null;

    private void initKeyguardService() {
        if (null != mKeyManager) {
            mKeyManager = null;
        }
        mKeyManager =(KeyguardManager)getSystemService(mContext.KEYGUARD_SERVICE);
        if (null != mKeyManager) {
            if (null != mKeyLock) {
                mKeyLock = null;
            }
            mKeyLock = mKeyManager.newKeyguardLock(mContext.KEYGUARD_SERVICE);
        }
    }

    private void setStandardKeyguardState(boolean isStart) {
        if (isStart) {
            if(null != mKeyLock){
                mKeyLock.reenableKeyguard();
            }
        }
        else {

            if(null != mKeyManager){
                mKeyLock.disableKeyguard();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stateRecever(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
