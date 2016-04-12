package com.groupthree.toeicword.controller.luyentap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.controller.InternetReceiver;
import com.groupthree.toeicword.controller.listen.OpenMp3;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class LuyenNoiActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout rootLuyenNoi;
    TextView tvnType, tvnWord, tvnPhonetic, tvnMean;
    ImageButton ibnMic, ibnSpeaker;
    Button btnnNext, btnnExit;
    ArrayList<Word> arrW;
    DatabaseWord db;
    int pos = 0;
    Word w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luyen_noi);
        init();
    }

    private void init() {
        setupActionBar();
        getWord();
        rootLuyenNoi = (LinearLayout) findViewById(R.id.rootLuyenNoi);
        tvnType = (TextView) findViewById(R.id.tvnType);
        tvnWord = (TextView) findViewById(R.id.tvnWord);
        tvnPhonetic = (TextView) findViewById(R.id.tvnPhonetic);
        tvnMean = (TextView) findViewById(R.id.tvnMean);
        ibnMic = (ImageButton) findViewById(R.id.ibnMic);
        ibnSpeaker = (ImageButton) findViewById(R.id.ibnSpeaker);
        btnnNext = (Button) findViewById(R.id.btnnNext);
        btnnExit = (Button) findViewById(R.id.btnnExit);
        ibnMic.setOnClickListener(this);
        ibnSpeaker.setOnClickListener(this);
        btnnNext.setOnClickListener(this);
        btnnExit.setOnClickListener(this);
        setupWord(0);
    }

    public void setupWord(int pos) {
        w = arrW.get(pos);
        tvnMean.setText(w.Mean);
        tvnPhonetic.setText(w.Phonetic);
        tvnType.setText(new StringBuilder().append("(").append(w.Type).append(")"));
        tvnWord.setText(w.Word);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playMp3(w.id);
            }
        }, 150);

    }

    public ArrayList<Word> getWord() {
        db = new DatabaseWord(getApplicationContext());
        arrW = db.queryWordWithFavorite();
        Collections.shuffle(arrW);
        return arrW;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Snackbar thongBao(String msg) {
        return Snackbar.make(rootLuyenNoi, msg, Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void letsSpeak() {
        if (InternetReceiver.network) {
            Intent itSpeak = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            itSpeak.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            itSpeak.putExtra("android.speech.extra.LANGUAGE", String.valueOf(Locale.US.getLanguage()));
            itSpeak.putExtra("android.speech.extra.PROMPT", "Phát âm '" + w.Word + "'");
            try {
                startActivityForResult(itSpeak, 100);
                return;
            } catch (ActivityNotFoundException localActivityNotFoundException) {
                thongBao("Máy không hỗ trợ.").show();
            }
        } else {
            thongBao("Kiểm tra lại kết nối.").show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((resultCode != -1) || (data != null)) ;
            assert data != null;
            String result_speak = data.getStringArrayListExtra("android.speech.extra.RESULTS").get(0);
            if (result_speak.equals(w.Word)) {
                w.View = 1;
                new DatabaseWord(getApplicationContext()).updateLearned(w);
                thongBao("Bạn đã học thành công từ: " + w.Word).show();
            } else {
                thongBao("Phải là: " + w.Word + "\nChứ không phải là: " + result_speak).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMp3(int id) {
        OpenMp3.a(getApplicationContext()).playMp3(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibnMic:
                letsSpeak();
                break;
            case R.id.ibnSpeaker:
                playMp3(w.id);
                break;
            case R.id.btnnNext:
                if (pos < arrW.size() - 1) {
                    pos++;
                } else {
                    pos = 0;
                }
                setupWord(pos);
                break;
            case R.id.btnnExit:
                finish();
                break;
        }
    }
}