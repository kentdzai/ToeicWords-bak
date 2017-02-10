package com.groupthree.toeicword.controller.khoamanhinh;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class LockScreenService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    SharedPreferences pref;
    CountDownTimer ct;
    public Context mContext = null;
    WindowManager windowManager = null;
    RelativeLayout rlLock;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    View view = null;
    private WindowManager.LayoutParams mParams;
    private LayoutInflater mInflater = null;
    TextView tvWord, tvDay, tvDoiCau, tvMoKhoaGap;
    Button btnAns1;
    Button btnAns2;
    String nghia;
    Calendar calendar;
    DigitalClock digital;
    int year_x, month_x, day_x;
    int check;
    int count = 0;
    int key = 0;
    Animation animation;
    Intent it, it1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        it = intent;
        it1 = new Intent("test");


        if(getListWord().size() >= 2) {

            try {
                key = intent.getIntExtra("id", 0);

            } catch (Exception e) {

            }
            if (key == 0) {
                stopService(it);

            } else {
                init();
                question(0);
                setupTime();
                it1.putExtra("id2", 2);
                sendBroadcast(it1);
            }

        }else{
            stopService(it);
        }


        return START_NOT_STICKY;
    }




    private void attachLockScreenView() {

        if (null != windowManager && null != view && null != mParams) {
            windowManager.addView(view, mParams);
            settingLockView();
        }

    }

    private void settingLockView() {
        rlLock = (RelativeLayout) view.findViewById(R.id.rlLock);
        tvWord = (TextView) view.findViewById(R.id.tvWord);
        btnAns1 = (Button) view.findViewById(R.id.btnAns1);
        btnAns2 = (Button) view.findViewById(R.id.btnAns2);
        tvDay = (TextView) view.findViewById(R.id.tvDay);
        tvMoKhoaGap = (TextView) view.findViewById(R.id.tvMoKhoagap);
        tvDoiCau = (TextView) view.findViewById(R.id.tvDoiCau);
        digital = (DigitalClock) view.findViewById(R.id.digital_clock);
        btnAns1.setOnClickListener(this);
        btnAns2.setOnClickListener(this);
        tvDoiCau.setOnClickListener(this);
        tvMoKhoaGap.setOnClickListener(this);


    }

    public void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        ct = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                question(1);
            }
        };

        getListWord();
        if(pref.getBoolean("khoa_man_hinh", false)){
            if (null != windowManager) {
                if (null != view) {
                    windowManager.removeView(view);
                }
                windowManager = null;
                mParams = null;
                mInflater = null;
                view = null;
            }
            initState();
            initView();
            attachLockScreenView();
        }

    }

    public void setBackground(){
        btnAns1.setBackgroundResource(R.drawable.cuttom_button);
        btnAns2.setBackgroundResource(R.drawable.cuttom_button);
        btnAns1.setEnabled(true);
        btnAns2.setEnabled(true);

    }

    private boolean dettachLockScreenView() {
        if (null != windowManager && null != view) {
            windowManager.removeView(view);
            windowManager = null;
            view = null;
            stopSelf(0);
            return true;
        } else {
            return false;
        }
    }

    private void initState() {
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                PixelFormat.TRANSLUCENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mParams.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
        } else {
            mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }

        if (null == windowManager) {
            windowManager = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE));
        }
    }

    private void initView() {
        if (null == mInflater) {
            mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (null == view) {
            view = mInflater.inflate(R.layout.activity_lockscreen, null);

        }
    }

    public void setupTime() {
        calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        tvDay.setText(day_x + "/" + (month_x + 1) + "/" + year_x);
    }

    public void question(int id) {
        ArrayList<String> listNghia = new ArrayList<String>();
        setBackground();
        int i = 0;
        if(count == arrL.size()){
            count =0;
        }
        if(id == 0){
            Random random = new Random();
            i = random.nextInt(arrL.size());
            check = i;
            count++;
        }else{
            if(check >= 0 && check < (arrL.size() - 1)){
                i = check + 1;
                check++;
            }else if(check == (arrL.size() - 1)){
                i = 0;
                check = i;
            }
            count++;
        }
        Random rd = new Random();
        int i2 = rd.nextInt(arrL.size());
        tvWord.setText(arrL.get(i).Word);
        nghia = arrL.get(i).Mean;
        String nghia2 = "";
        if (i2 != i) {
            nghia2 = arrL.get(i2).Mean;
        } else {
            if (i == 0) {
                nghia2 = arrL.get(i + 1).Mean;
            } else {
                nghia2 = arrL.get(i - 1).Mean;
            }
        }
        listNghia.add(nghia);
        listNghia.add(nghia2);
        Random rd1 = new Random();
        int i3 = rd1.nextInt(2);
        String n1 = listNghia.get(i3);
        btnAns1.setText(n1);
        if (i3 == 0) {
            btnAns2.setText(listNghia.get(1));
        } else {
            btnAns2.setText(listNghia.get(0));
        }
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getBaseContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    public void test(Button btn){
        if(btn.getText().toString().equals(nghia)){
            btn.startAnimation(animation);
            btn.setBackgroundResource(R.drawable.cuttom_button_true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAns1:
                if (btnAns1.getText().toString().equals(nghia)) {
                    dettachLockScreenView();
                    stopService(it);
                    ct.cancel();
                    btnAns1.startAnimation(animation);
                    btnAns1.setBackgroundResource(R.drawable.cuttom_button_true);
                } else {
                    btnAns1.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnAns2);
                    setVisible(btnAns1);
                    setVisible(btnAns2);
                    ct.start();

                }

                break;
            case R.id.btnAns2:
                if (btnAns2.getText().toString().equals(nghia)) {
                    dettachLockScreenView();
                    stopService(it);
                    ct.cancel();
                    btnAns2.startAnimation(animation);
                    btnAns2.setBackgroundResource(R.drawable.cuttom_button_true);
                } else {
                    btnAns2.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnAns1);
                    setVisible(btnAns1);
                    setVisible(btnAns2);
                    ct.start();
                }

                break;

            case R.id.tvDoiCau:
                btnAns2.setBackgroundResource(R.drawable.cuttom_button_false);
                question(1);

                break;

            case R.id.tvMoKhoagap:
                dettachLockScreenView();
                stopService(it);
                ct.cancel();

                break;
        }
    }

    public void setVisible(Button btn){
        btn.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dettachLockScreenView();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
