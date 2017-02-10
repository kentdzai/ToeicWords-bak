package com.groupthree.toeicword.controller.luyentap;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.controller.listen.OpenMp3;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.Word;

import java.util.ArrayList;
import java.util.Random;

public class LuyenNgheActivity extends AppCompatActivity implements View.OnClickListener {
    ScrollView rootLuyenNghe;
    Button btnnAns1, btnnAns2, btnnAns3, btnnAns4, btnnNext;
    ImageButton btnnListen;
    DatabaseWord db;
    ArrayList<Word> arrW, arrW1, arrW2;
    Word w;
    int pos = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luyen_nghe);
        init();
    }

    private void init() {
        setupActionBar();
        btnnNext = (Button) findViewById(R.id.btnnNext);
        btnnAns1 = (Button) findViewById(R.id.btnnAns1);
        btnnAns2 = (Button) findViewById(R.id.btnnAns2);
        btnnAns3 = (Button) findViewById(R.id.btnnAns3);
        btnnAns4 = (Button) findViewById(R.id.btnnAns4);
        btnnListen = (ImageButton) findViewById(R.id.btnnListen);
        rootLuyenNghe = (ScrollView) findViewById(R.id.rootLuyenNghe);
        setupWord(0);

        btnnNext.setOnClickListener(this);
        btnnAns1.setOnClickListener(this);
        btnnAns2.setOnClickListener(this);
        btnnAns3.setOnClickListener(this);
        btnnAns4.setOnClickListener(this);
        btnnListen.setOnClickListener(this);
    }

    public void defaultButton(Button btn) {
        btn.setEnabled(true);
        btn.setAlpha(1);
    }

    int arrR[];

    public void setupWord(int pos) {
        defaultButton(btnnAns1);
        defaultButton(btnnAns2);
        defaultButton(btnnAns3);
        defaultButton(btnnAns4);
        arrW1 = new ArrayList<>();
        ArrayList<String> arrS = new ArrayList<>();
        arrW2 = getWord();
        w = arrW.get(pos);
        Random rd = new Random();

        arrS.add(w.Word);
        arrW1.add(w);
        arrW2.remove(w);

        int i2 = rd.nextInt(arrW.size() - 1);
        arrW1.add(arrW2.get(i2));
        arrS.add(arrW2.get(i2).Word);
        arrW2.remove(i2);

        int i3 = rd.nextInt(arrW.size() - 1);
        arrW1.add(arrW2.get(i3));
        arrS.add(arrW2.get(i3).Word);
        arrW2.remove(i3);

        int i4 = rd.nextInt(arrW.size());
        arrW1.add(arrW2.get(i4));
        arrS.add(arrW2.get(i4).Word);
        arrW2.remove(i4);

        arrR = daoViTri(new int[]{0, 1, 2, 3});

        btnnAns1.setText(arrS.get(arrR[0]));
        btnnAns2.setText(arrS.get(arrR[1]));
        btnnAns3.setText(arrS.get(arrR[2]));
        btnnAns4.setText(arrS.get(arrR[3]));
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMp3(w);
            }
        }, 150);
    }

    Handler handler;

    public ArrayList<Word> getWord() {
        db = new DatabaseWord(getApplicationContext());
        arrW = db.queryWordWithFavorite();
        return arrW;
    }

    public int[] daoViTri(int arrR[]) {
        Random r = new Random();
        for (int j = arrR.length - 1; j > 0; j--) {
            int q = r.nextInt(j + 1);
            int k = arrR[q];
            arrR[q] = arrR[j];
            arrR[j] = k;
        }
        return arrR;
    }


    public void checkAnswer(Button btn) {
        String text = btn.getText().toString();
        if (text.equals(w.Word)) {
            count = 0;
            Word result = db.queryWordWithText(text);
            btn.setText(new StringBuilder()
                    .append(result.Word)
                    .append(" - ")
                    .append(result.Phonetic));
            thongBao("Chính xác!");
            setFalse();
            btn.setAlpha(1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doNext();
                }
            }, 3000);
        } else {
            count++;
            btn.setEnabled(false);
            btn.setAlpha(0.5f);
            if (text.equals(textCompare(btnnAns1))) {
                btnnAns1.setText(setWordAndPhoneticWithFalse(0));
            }
            if (text.equals(textCompare(btnnAns2))) {
                btnnAns2.setText(setWordAndPhoneticWithFalse(1));
            }
            if (text.equals(textCompare(btnnAns3))) {
                btnnAns3.setText(setWordAndPhoneticWithFalse(2));
            }
            if (text.equals(textCompare(btnnAns4))) {
                btnnAns4.setText(setWordAndPhoneticWithFalse(3));
            }
        }
    }

    public String textCompare(Button btn) {
        return btn.getText().toString();
    }

    public void setFalse() {
        checkFalse(btnnAns1);
        checkFalse(btnnAns2);
        checkFalse(btnnAns3);
        checkFalse(btnnAns4);
        btnnAns1.setText(setWordAndPhoneticWithFalse(0));
        btnnAns2.setText(setWordAndPhoneticWithFalse(1));
        btnnAns3.setText(setWordAndPhoneticWithFalse(2));
        btnnAns4.setText(setWordAndPhoneticWithFalse(3));
    }

    public String setWordAndPhoneticWithFalse(int pos) {
        return new StringBuilder()
                .append(arrW1.get(arrR[pos]).Word)
                .append(" - ")
                .append(arrW1.get(arrR[pos]).Phonetic).toString();
    }

    public boolean checkFalse(Button btn) {
        if (btn.getText().toString().equals(w.Word)) {
            return true;
        } else {
            btn.setAlpha(0.5f);
            return false;
        }
    }

    public void doNext() {
        if (pos < getWord().size() - 1) {
            pos++;
        } else {
            pos = 0;
        }
        setupWord(pos);
    }

    public void thongBao(String msg) {
        Snackbar.make(rootLuyenNghe, msg, Snackbar.LENGTH_LONG).show();
    }

    public MediaPlayer getMp3(Word w) {
        return OpenMp3.a(getApplicationContext()).playMp3(w.id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnnListen:
                getMp3(w);
                break;
            case R.id.btnnAns1:
                checkAnswer(btnnAns1);
                break;
            case R.id.btnnAns2:
                checkAnswer(btnnAns2);
                break;
            case R.id.btnnAns3:
                checkAnswer(btnnAns3);
                break;
            case R.id.btnnAns4:
                checkAnswer(btnnAns4);
                break;
            case R.id.btnnNext:
                doNext();
                break;
        }
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
                finish();
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
    }
}
