package com.groupthree.toeicword.controller.luyentap;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.controller.listen.OpenMp3;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.Word;

import java.util.ArrayList;

public class NgheLapActivity extends AppCompatActivity {
    LinearLayout rootNgheLap;
    TextView tvnlType, tvnlWord, tvnlPhonetic, tvnlMean, tvnlSentence, tvnlSentenceMean;
    ArrayList<Word> arrW;
    DatabaseWord db;
    Word w;
    int pos = -1;
    float fromx;
    float tox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nghe_lap);
        init();
    }

    private void init() {
        setupActionBar();
        getWord();
        rootNgheLap = (LinearLayout) findViewById(R.id.rootNgheLap);
        tvnlType = (TextView) findViewById(R.id.tvnlType);
        tvnlWord = (TextView) findViewById(R.id.tvnlWord);
        tvnlPhonetic = (TextView) findViewById(R.id.tvnlPhonetic);
        tvnlMean = (TextView) findViewById(R.id.tvnlMean);
        tvnlSentence = (TextView) findViewById(R.id.tvnlSentence);
        tvnlSentenceMean = (TextView) findViewById(R.id.tvnlSentenceMean);
        lapNghe();
    }

    public void setupWord(int pos, float fromx, float tox) {
        w = arrW.get(pos);
        tvnlType.setText(new StringBuilder().append("(").append(w.Type).append(")"));
        tvnlWord.setText(w.Word);
        tvnlPhonetic.setText(w.Phonetic);
        tvnlMean.setText(w.Mean);
        tvnlSentence.setText(w.Sentence);
        tvnlSentenceMean.setText(w.SentenceMean);
        new Handler().postDelayed(new RunablePlayer(), 150);
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootNgheLap, "x", fromx, tox);
        ObjectAnimator rootFadeOut = ObjectAnimator.ofFloat(rootNgheLap, "alpha", 0.4f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, rootFadeOut);
        set.setDuration(1000);
        set.start();
    }

    MediaPlayer med;

    public class RunablePlayer implements Runnable {
        @Override
        public void run() {
            playMp3();
        }
    }

    Handler handler;
    Runnable runnable;

    public void lapNghe() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 4000);
                if (pos < arrW.size() - 1) {
                    pos++;
                } else {
                    pos = 0;
                }
                fromx = rootNgheLap.getX() - 300;
                tox = rootNgheLap.getX();
                setupWord(pos, fromx, tox);
            }
        };
        runnable.run();
    }

    public void playMp3() {
        med = OpenMp3.a(getApplicationContext()).playMp3(w.id);
    }

    public ArrayList<Word> getWord() {
        db = new DatabaseWord(getApplicationContext());
        arrW = db.queryWordWithFavorite();
        return arrW;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
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
                onStopActivity();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStopActivity();
        finish();
    }

    public void onStopActivity() {
        if (med != null && med.isPlaying()) {
            med.release();
        }
    }
}