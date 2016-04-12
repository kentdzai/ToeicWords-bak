package com.groupthree.toeicword.controller.nhactu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.Word;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NhacTuService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {
    WindowManager windowManager;
    View view;
    WindowParams parmW;
    LinearLayout lnNhacTu;
    TextView tvWord;
    TextView tvPhonetic;
    TextView tvSortMean;

    DatabaseWord db;
    ArrayList<Word> arrW;
    Word w;
    SharedPreferences pref;

    int random = 0;
    int color;
    String phien_am;
    int[] arrColor;
    int pos = -1;
    boolean nhac_tu;
    Timer timer;
    Handler uiHandler;
    NotificationCompat.Builder builder;

    NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        if (intent != null) {
            nhac_tu = intent.getBooleanExtra("nhac_tu", false);
            if (nhac_tu) {
                getWord();
                uiHandler = new Handler(getMainLooper());
                init();
                pref.edit().putBoolean("nhac_tu", true).commit();
            } else {
                stopService(intent);
                pref.edit().putBoolean("nhac_tu", false).commit();
            }
        }
        return START_STICKY;
    }

    public void getWord() {
        db = new DatabaseWord(getBaseContext());
        arrW = db.queryWord();
    }

    public void init() {
        setupView();
        lnNhacTu = (LinearLayout) view.findViewById(R.id.lnNhacTu);
        tvWord = (TextView) view.findViewById(R.id.tvWord);
        tvPhonetic = (TextView) view.findViewById(R.id.tvPhonetic);
        tvSortMean = (TextView) view.findViewById(R.id.tvSortMean);
        if (timer == null) {
            lnNhacTu.setVisibility(LinearLayout.INVISIBLE);
            setupLoop();
        }
        setupNotification();
    }

    public void setupNotification() {
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getBaseContext())
                .setContentTitle("Nhắc từ").setContentText("chạm để đóng nhắc từ")
                .setSmallIcon(R.mipmap.ic_launcher);
        builder.setOngoing(true);
        Intent it = new Intent(this, NhacTuService.class);
        it.putExtra("nhac_tu", false);
        PendingIntent pdIntent = PendingIntent.getService(getApplicationContext(), 0, it, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pdIntent);
        notificationManager.notify(6969, builder.build());
    }

    public boolean getColorRandom() {
        return pref.getBoolean("color_nhac_tu", false);
    }

    public boolean getPhienAm() {
        return pref.getBoolean("phien_am_nhac_tu", false);
    }

    public int getTime() {
        return Integer.parseInt(pref.getString("time_nhac_tu", "3"));
    }

    public int getColor() {
        return Color.parseColor(pref.getString("list_color", "#3F51B5"));
    }

    private void setupView() {
        if (view == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            view = View.inflate(getBaseContext(), R.layout.layout_nhactu, null);
            parmW = new WindowParams();
            windowManager.addView(view, parmW);
            view.setOnTouchListener(new Touch(view, windowManager,
                    parmW, pos));
        }
    }

    public void setupText(final int position, final boolean get_phien_am, final boolean get_color_random) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arrColor = getResources().getIntArray(R.array.arrColor);
                w = arrW.get(position);
                if (get_phien_am) {
                    phien_am = w.Phonetic;
                } else {
                    phien_am = "";
                }

                if (get_color_random) {
                    random = new Random().nextInt(arrColor.length - 1);
                    color = arrColor[random];
                } else {
                    color = getColor();
                }
                lnNhacTu.setVisibility(LinearLayout.VISIBLE);
                tvWord.setText(w.Word);
                tvPhonetic.setText(phien_am);
                tvSortMean.setText(w.SortMean);
                lnNhacTu.setBackgroundColor(color);
                builder.setContentTitle(w.Word);
                notificationManager.notify(6969, builder.build());
            }
        }, 0);
    }

    public void setupLoop() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pos < arrW.size() - 1) {
                    pos++;
                } else {
                    pos = 0;
                }
                setupText(pos, getPhienAm(), getColorRandom());
            }
        }, 0, getTime() * 1000L);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("time_nhac_tu") || key.equals("phien_am_nhac_tu") || key.equals("color_nhac_tu")) {
            timer.cancel();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setupLoop();
                }
            }, getTime() * 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            windowManager.removeView(view);
            stopSelf(0);
        }
        if (timer != null) {
            timer.cancel();
        }
        if (notificationManager != null) {
            notificationManager.cancel(6969);
        }

    }
}