package com.groupthree.toeicword.controller.nhactu;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.groupthree.toeicword.R;

public class Touch implements OnTouchListener {
    int x, y;
    float touchX, touchY;
    private WindowManager.LayoutParams update;
    View view;
    WindowManager windowManager;

    public Touch(View view,
                 WindowManager windowManager, WindowManager.LayoutParams update) {
        super();
        this.view = view;
        this.windowManager = windowManager;
        this.update = update;
    }

    int tap = 0;
    LinearLayout lnNhacTu;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = update.x;
                y = update.y;
                touchX = event.getRawX();
                touchY = event.getRawY();
                tap++;
                lnNhacTu = (LinearLayout) v.findViewById(R.id.lnNhacTu);
                break;
            case MotionEvent.ACTION_UP:
                if (tap == 2) {
                    tap = 0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                update.x = (int) (x + (event.getRawX() - touchX));
                update.y = (int) (y + (event.getRawY() - touchY));
                windowManager.updateViewLayout(view, update);
                break;
        }
        return true;
    }

}
