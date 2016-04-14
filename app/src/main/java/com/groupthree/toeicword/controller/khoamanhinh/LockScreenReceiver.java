package com.groupthree.toeicword.controller.khoamanhinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class LockScreenReceiver extends BroadcastReceiver {

    Intent itLockScreenService;
    int id;
    int key;
    LockScreenService ls;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        itLockScreenService = new Intent(context, LockScreenService.class);

        try{
            key = intent.getIntExtra("id2", 0);
        }catch (Exception e){

        }


        if (null != context) {
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {

                TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                boolean isPhoneIdle = tManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;

                if (isPhoneIdle) {
                    if(ls == null) {
                        id = 1;
                        itLockScreenService.putExtra("id", id);
                        context.startService(itLockScreenService);
                    }
                }

            }
        }

        Toast.makeText(context, "abc" + key, Toast.LENGTH_SHORT).show();

    }

}
