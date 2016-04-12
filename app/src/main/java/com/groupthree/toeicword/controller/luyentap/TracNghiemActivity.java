package com.groupthree.toeicword.controller.luyentap;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TracNghiemActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseWord db;
    ArrayList<ListWord> arrL, arrL2;

    Button btnTL1, btnTL2, btnTL3, btnTL4;
    TextView tvSTTCauHoi, tvChuDe, tvTuVung, tvTime;
    String nghia;
    int check;
    int test = 0;
    ProgressBar pbTime;
    CountDownTimer ct,ct1;
    int time;
    Animation shake;
    int point = 0;

    String status[] = {"Tuyệt vời !", "Tốt lắm !", "Cố gắng lên !"};
    String statusPoint = "Cố gắng lên !";

    int icon[] = {R.mipmap.ic_tuyetvoi, R.mipmap.ic_cogang, R.mipmap.ic_dongcam};

    int iconStatus = R.mipmap.ic_dongcam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);
        init();
        arrL = getListWord();
        question(0);

    }

    public void question(int id) {

        if(test == arrL.size()){
            if(((double) point/arrL.size()) >= 0.8 && (point/arrL.size()) <= 1){
                statusPoint = status[0];
                iconStatus = icon[0];
            }else if(((double) point/arrL.size()) >= 0.5 && (point/arrL.size()) < 0.8){
                statusPoint = status[1];
                iconStatus = icon[1];
            }else if(((double) point/arrL.size()) < 0.5){
                statusPoint = status[2];
                iconStatus = icon[2];
            }
            ct1.cancel();
            ct.cancel();
            handleMessage(true);
        }else {

            time = 30;
            ct1.start();
            setBackground();
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

    public void test(Button btn){
        if(btn.getText().toString().equals(nghia)){
            btn.startAnimation(shake);
            btn.setBackgroundResource(R.drawable.cuttom_button_true);
        }
    }

    public ArrayList<ListWord> getListWord() {
        ArrayList<ListWord> list = new ArrayList<ListWord>();
        list = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return list;
    }

    private void init(){
        db = new DatabaseWord(getApplicationContext());
        btnTL1 = (Button) findViewById(R.id.btnTL1);
        btnTL2 = (Button) findViewById(R.id.btnTL2);
        btnTL3 = (Button) findViewById(R.id.btnTL3);
        btnTL4 = (Button) findViewById(R.id.btnTL4);
        tvChuDe = (TextView) findViewById(R.id.tvChuDe);
        tvSTTCauHoi = (TextView) findViewById(R.id.tvSTTCauHoi);
        tvTuVung = (TextView) findViewById(R.id.tvTuVung);
        tvTime = (TextView) findViewById(R.id.tvTime);
        pbTime = (ProgressBar) findViewById(R.id.pbTime);
        shake = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        pbTime.setMax(30);
        ct = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                question(1);
            }
        };

        ct1 = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                int progress = (int) (millisUntilFinished/1000);
                pbTime.setProgress(progress);
                tvTime.setText(progress + "s");
            }

            public void onFinish() {

                setVisible(btnTL1);
                setVisible(btnTL2);
                setVisible(btnTL3);
                setVisible(btnTL4);

                test(btnTL1);
                test(btnTL2);
                test(btnTL3);
                test(btnTL4);

                ct.start();
            }
        };
        setupActionBar();
        getListWord();
        btnTL1.setOnClickListener(this);
        btnTL2.setOnClickListener(this);
        btnTL3.setOnClickListener(this);
        btnTL4.setOnClickListener(this);
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
                            question(1);
                            sweetAlertDialog.cancel();
                        }
                    }).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnTL1:

                if (btnTL1.getText().toString().equals(nghia)) {
                    btnTL1.startAnimation(shake);
                    btnTL1.setBackgroundResource(R.drawable.cuttom_button_true);
                    point++;

                } else {
                    btnTL1.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnTL2);
                    test(btnTL3);
                    test(btnTL4);
                }
                setVisible(btnTL1);
                setVisible(btnTL2);
                setVisible(btnTL3);
                setVisible(btnTL4);
                ct.start();
                break;
            case R.id.btnTL2:
                if (btnTL2.getText().toString().equals(nghia)) {
                    btnTL2.startAnimation(shake);
                    btnTL2.setBackgroundResource(R.drawable.cuttom_button_true);
                    point++;
                } else {
                    btnTL2.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnTL1);
                    test(btnTL3);
                    test(btnTL4);
                }
                setVisible(btnTL1);
                setVisible(btnTL2);
                setVisible(btnTL3);
                setVisible(btnTL4);
                ct.start();
                break;
            case R.id.btnTL3:
                if (btnTL3.getText().toString().equals(nghia)) {
                    btnTL3.startAnimation(shake);
                    btnTL3.setBackgroundResource(R.drawable.cuttom_button_true);
                    point++;

                } else {
                    btnTL3.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnTL2);
                    test(btnTL1);
                    test(btnTL4);
                }
                setVisible(btnTL1);
                setVisible(btnTL2);
                setVisible(btnTL3);
                setVisible(btnTL4);
                ct.start();
                break;
            case R.id.btnTL4:
                if (btnTL4.getText().toString().equals(nghia)) {
                    btnTL4.startAnimation(shake);
                    btnTL4.setBackgroundResource(R.drawable.cuttom_button_true);
                    point++;
                } else {
                    btnTL4.setBackgroundResource(R.drawable.cuttom_button_false);
                    test(btnTL2);
                    test(btnTL3);
                    test(btnTL1);
                }
                setVisible(btnTL1);
                setVisible(btnTL2);
                setVisible(btnTL3);
                setVisible(btnTL4);
                ct.start();
                break;
        }
    }

    public void setBackground(){
        btnTL1.setBackgroundResource(R.drawable.cuttom_button);
        btnTL2.setBackgroundResource(R.drawable.cuttom_button);
        btnTL3.setBackgroundResource(R.drawable.cuttom_button);
        btnTL4.setBackgroundResource(R.drawable.cuttom_button);
        btnTL1.setEnabled(true);
        btnTL2.setEnabled(true);
        btnTL3.setEnabled(true);
        btnTL4.setEnabled(true);

    }

    public void setVisible(Button btn){
        btn.setEnabled(false);
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
}
