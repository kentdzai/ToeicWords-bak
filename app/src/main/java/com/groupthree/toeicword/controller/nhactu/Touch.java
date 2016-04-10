package com.groupthree.toeicword.controller.nhactu;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class Touch implements OnTouchListener {
    int x, y;
    float touchX, touchY;
    private WindowManager.LayoutParams update;
    View view;
    WindowManager windowManager;
    int p;

    public Touch(View view,
                 WindowManager windowManager, WindowManager.LayoutParams update, int p) {
        super();
        this.view = view;
        this.windowManager = windowManager;
        this.update = update;
        this.p = p;
    }

    int tap = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = update.x;
                y = update.y;
                touchX = event.getRawX();
                touchY = event.getRawY();
                tap++;

                break;
            case MotionEvent.ACTION_UP:
                if (tap == 2) {
                    Log.e("my_watch", "double tap" + p);
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
