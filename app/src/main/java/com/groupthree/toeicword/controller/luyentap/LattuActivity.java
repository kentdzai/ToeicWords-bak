package com.groupthree.toeicword.controller.luyentap;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.controller.listen.OpenMp3;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;
import com.groupthree.toeicword.model.Word;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by QuyetChu on 4/4/16.
 */
public class LattuActivity extends AppCompatActivity implements
        View.OnClickListener {

    Word w;
    ListWord lw;
    TextView tvChuDe, tvTuVung, tvSTTCauHoi;
    Button btnLattu, btnTiepTheo, btnThoat;
    LinearLayout llTuVung;
    DatabaseWord db;
    ArrayList<ListWord> arrL, arrL2;
    int test = 0;
    int i = 0;
    int check;
    Animator shake;
    AnimationSet as1, as2;
    CountDownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lattu);
        init();
        question(0);
    }

    public void init() {
        setupActionBar();
        shake = AnimatorInflater.loadAnimator(this, R.animator.lattu_animator);
        as1 = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.xin_from);
        as2 = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.lattu_animation);
        db = new DatabaseWord(getApplicationContext());
        arrL = getListWord();
        llTuVung = (LinearLayout) findViewById(R.id.llTuVung);
        tvChuDe = (TextView) findViewById(R.id.tvChuDe2);
        tvTuVung = (TextView) findViewById(R.id.tvTuVung2);
        btnLattu = (Button) findViewById(R.id.btnLatTu);
        btnThoat = (Button) findViewById(R.id.btnThoat);
        btnTiepTheo = (Button) findViewById(R.id.btnTiepTheo);
        tvSTTCauHoi = (TextView) findViewById(R.id.tvSTTCauHoi2);
        ct = new CountDownTimer(500, 100) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                tvTuVung.setText(arrL.get(i).Mean);
            }
        };
        btnTiepTheo.setOnClickListener(this);
        btnLattu.setOnClickListener(this);
        btnThoat.setOnClickListener(this);

    }

    public Word getWord() {
        db = new DatabaseWord(getApplication());
        w = db.queryWordWithId(arrL.get(i).Id);
        lw = new ListWord(w.id, w.Word, w.Mean, w.FavouriteWord);
        return w;
    }

    public void question(int id) {
        btnLattu.setEnabled(true);
        llTuVung.startAnimation(as2);
        if (test == (arrL.size())) {
            btnTiepTheo.setText("LÀM LẠI");
        } else {
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
            tvChuDe.setText(db.getFavourite(arrL.get(i).Id));
            tvTuVung.setText(arrL.get(i).Word);
            tvSTTCauHoi.setText(test + "/" + arrL.size());
        }

        if (test == (arrL.size())) {
            btnTiepTheo.setText("LÀM LẠI");
        }

        getWord();

        OpenMp3.a(getApplicationContext()).playMp3(w.id);

    }

    public ArrayList<ListWord> getListWord() {
        ArrayList<ListWord> list = new ArrayList<ListWord>();
        list = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTiepTheo:

                if (btnTiepTheo.getText().toString().equals("LÀM LẠI")) {
                    test = 0;
                    question(0);
                    btnTiepTheo.setText("TIẾP THEO");
                } else {
                    question(1);
                }

                break;

            case R.id.btnLatTu:

                shake.setTarget(llTuVung);
                shake.start();
//                llTuVung.startAnimation(as2);
//                tvTuVung.setText(arrL.get(i).Mean);
                btnLattu.setEnabled(false);
                ct.start();
                break;

            case R.id.btnThoat:

                finish();
                break;

            default:
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
                ct.cancel();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ct.cancel();
        finish();
    }
}
