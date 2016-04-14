package com.groupthree.toeicword.controller.khoamanhinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class LockScreenReceiver extends BroadcastReceiver {

    Intent itLockScreenService;
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
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

}
