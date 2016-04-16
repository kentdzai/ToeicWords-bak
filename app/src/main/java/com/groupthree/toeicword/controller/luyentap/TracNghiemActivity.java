package com.groupthree.toeicword.controller.luyentap;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TracNghiemActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseWord db;
    ArrayList<ListWord> arrL, arrL2;
    ScrollView rootTracNghiem;
    Button btnTL1, btnTL2, btnTL3, btnTL4;
    TextView tvSTTCauHoi, tvChuDe, tvTuVung, tvTime;
    String nghia;
    int check;
    int test = 0;
    ProgressBar pbTime;
    CountDownTimer ct, ct1;
    int time;
    Animation shake;
    int point = 0;

    String status[] = {"Tuyệt vời !", "Tốt lắm !", "Cố gắng lên !"};
    String statusPoint = "Cố gắng lên !";

    int icon[] = {R.mipmap.ic_tuyetvoi,
            R.mipmap.ic_cogang,
            R.mipmap.ic_dongcam};

    int iconStatus = R.mipmap.ic_dongcam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);
        init();
        arrL = getListWord();
        setupQuestion(0);
    }

    private void init() {
        setupActionBar();
        getListWord();
        btnTL1 = (Button) findViewById(R.id.btnTL1);
        btnTL2 = (Button) findViewById(R.id.btnTL2);
        btnTL3 = (Button) findViewById(R.id.btnTL3);
        btnTL4 = (Button) findViewById(R.id.btnTL4);
        rootTracNghiem = (ScrollView) findViewById(R.id.rootTracNghiem);
        tvChuDe = (TextView) findViewById(R.id.tvChuDe);
        tvSTTCauHoi = (TextView) findViewById(R.id.tvSTTCauHoi);
        tvTuVung = (TextView) findViewById(R.id.tvTuVung);
        tvTime = (TextView) findViewById(R.id.tvTime);
        pbTime = (ProgressBar) findViewById(R.id.pbTime);
        shake = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        pbTime.setMax(30);

        ct = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                setupQuestion(1);
            }
        };

        ct1 = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / 1000);
                pbTime.setProgress(progress);
                tvTime.setText(progress + "s");
            }

            public void onFinish() {
                disableButton();
                onAnswer();
                ct.start();
            }
        };
        btnTL1.setOnClickListener(this);
        btnTL2.setOnClickListener(this);
        btnTL3.setOnClickListener(this);
        btnTL4.setOnClickListener(this);
    }

    public void setupQuestion(int id) {
        if (test == arrL.size()) {
            if (((double) point / arrL.size()) >= 0.8 && (point / arrL.size()) <= 1) {
                statusPoint = status[0];
                iconStatus = icon[0];
            } else if (((double) point / arrL.size()) >= 0.5 && (point / arrL.size()) < 0.8) {
                statusPoint = status[1];
                iconStatus = icon[1];
            } else if (((double) point / arrL.size()) < 0.5) {
                statusPoint = status[2];
                iconStatus = icon[2];
            }
            ct1.cancel();
            ct.cancel();
            handleMessage(true);
        } else {
            time = 30;
            ct1.start();
            setButtonOnNextQuestion();
            ArrayList<String> listNghia = new ArrayList<String>();
            int i = 0;
            if (id == 0) {
                Random random = new Random();
                i = random.nextInt(arrL.size());
                check = i;
                test++;
            } else {
                if (check >= 0 && check < (arrL.size() - 1)) {
                    i = check + 1;
                    check++;
                } else if (check == (arrL.size() - 1)) {
                    i = 0;
                    check = i;
                }
                test++;
            }
            tvTuVung.setText(arrL.get(i).Word);
            nghia = arrL.get(i).Mean;
            tvChuDe.setText(db.getFavourite(arrL.get(i).Id));

            Random rd = new Random();

            arrL2 = getListWord();
            arrL2.remove(i);
            int i2 = rd.nextInt(arrL2.size());
            String nghia2 = arrL2.get(i2).Mean;
            arrL2.remove(i2);
            int i3 = rd.nextInt(arrL2.size());
            String nghia3 = arrL2.get(i3).Mean;
            arrL2.remove(i3);
            int i4 = rd.nextInt(arrL2.size());
            String nghia4 = arrL2.get(i4).Mean;

            listNghia.add(nghia);
            listNghia.add(nghia2);
            listNghia.add(nghia3);
            listNghia.add(nghia4);

            int it1 = rd.nextInt(listNghia.size());
            btnTL1.setText(listNghia.get(it1));
            listNghia.remove(it1);
            int it2 = rd.nextInt(listNghia.size());
            btnTL2.setText(listNghia.get(it2));
            listNghia.remove(it2);
            int it3 = rd.nextInt(listNghia.size());
            btnTL3.setText(listNghia.get(it3));
            listNghia.remove(it3);
            int it4 = rd.nextInt(listNghia.size());
            btnTL4.setText(listNghia.get(it4));
            tvSTTCauHoi.setText(test + "/" + arrL.size());
        }
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        ArrayList<ListWord> list = new ArrayList<ListWord>();
        list = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return list;
    }

    public void handleMessage(boolean chuancmnr) {
        if (chuancmnr) {
            new SweetAlertDialog(TracNghiemActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText(statusPoint)
                    .setCustomImage(iconStatus)
                    .setConfirmText("Làm lại")
                    .setContentText("Số đáp án đúng: " + point + "/" + arrL.size())
                    .setCancelText("Kết thúc")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            test = 0;
                            point = 0;
                            setupQuestion(1);
                            sweetAlertDialog.cancel();
                        }
                    }).show();
        }
    }

    public boolean checkQuestion(Button btn) {
        if (btn.getText().toString().equals(nghia)) {
            btn.startAnimation(shake);
            btn.setEnabled(true);
            return true;
        } else {
            btn.setAlpha(0.5f);
            return false;
        }
    }

    public void checkPoint(Button btn) {
        if (btn.getText().toString().equals(nghia)) {
            thongBao("Chính xác!");
            point++;
        }
    }

    public void onAnswer() {
        checkQuestion(btnTL1);
        checkQuestion(btnTL2);
        checkQuestion(btnTL3);
        checkQuestion(btnTL4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTL1:
                checkPoint(btnTL1);
                onAnswer();
                disableButton();
                ct.start();
                break;
            case R.id.btnTL2:
                checkPoint(btnTL2);
                onAnswer();
                disableButton();
                ct.start();
                break;
            case R.id.btnTL3:
                checkPoint(btnTL3);
                onAnswer();
                disableButton();
                ct.start();
                break;
            case R.id.btnTL4:
                checkPoint(btnTL4);
                onAnswer();
                disableButton();
                ct.start();
                break;
        }
    }

    public void setButtonOnNextQuestion() {
        btnTL1.setEnabled(true);
        btnTL2.setEnabled(true);
        btnTL3.setEnabled(true);
        btnTL4.setEnabled(true);
        btnTL1.setAlpha(1);
        btnTL2.setAlpha(1);
        btnTL3.setAlpha(1);
        btnTL4.setAlpha(1);
    }

    public void disableButton() {
        btnTL1.setEnabled(false);
        btnTL2.setEnabled(false);
        btnTL3.setEnabled(false);
        btnTL4.setEnabled(false);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void thongBao(String msg) {
        Snackbar.make(rootTracNghiem, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ct.cancel();
                ct1.cancel();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ct.cancel();
        ct1.cancel();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ct.cancel();
        ct1.cancel();
    }
}
