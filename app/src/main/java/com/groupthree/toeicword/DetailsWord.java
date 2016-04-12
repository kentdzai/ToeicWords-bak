package com.groupthree.toeicword;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.controller.InternetReceiver;
import com.groupthree.toeicword.controller.listen.OpenMp3;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;
import com.groupthree.toeicword.model.Word;

import java.util.Locale;

public class DetailsWord extends AppCompatActivity implements View.OnClickListener {
    DatabaseWord db;
    Word w;
    ListWord lw;
    WebView wvDetails;
    TextView tvWordsType, tvPhonetic, tvMean, tvSentence, tvSentenceMean, tvSynonyms;
    ImageButton btnNext, btnPrev, btnSpeak, btnListen;
    CheckBox cbDetails;
    LinearLayout rootDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_word);
        init();
    }

    public void init() {
        getWord();
        setupActionBar();
        rootDetails = (LinearLayout) findViewById(R.id.rootDetails);
        wvDetails = (WebView) findViewById(R.id.wvDetails);
        tvWordsType = (TextView) findViewById(R.id.tvWordsType);
        tvPhonetic = (TextView) findViewById(R.id.tvPhonetic);
        tvMean = (TextView) findViewById(R.id.tvMean);
        tvSentence = (TextView) findViewById(R.id.tvSentence);
        tvSentenceMean = (TextView) findViewById(R.id.tvSentenceMean);
        tvSynonyms = (TextView) findViewById(R.id.tvSynonyms);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        cbDetails = (CheckBox) findViewById(R.id.cbDetails);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnListen = (ImageButton) findViewById(R.id.btnListen);
        btnListen.setOnClickListener(this);
        btnSpeak.setOnClickListener(this);
        cbDetails.setChecked(lw.getCheck());
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        cbDetails.setOnClickListener(this);
        setupDic();
    }

    public void setupDic() {
        wvDetails.loadDataWithBaseURL("", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html>  <head>  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><style>.title{font-weight:bold; color:#1A237E; font-size: 18px}.type{color:#FF9800} body{color: #424242}</style></head>  <body>" + w.AdequateMean + "</body></html>", "text/html", "UTF-8", "");
        tvWordsType.setText(w.Word + " (" + w.Type + ")");
        tvPhonetic.setText(w.Phonetic);
        tvMean.setText(w.Mean);
        tvSentence.setText(w.Sentence);
        tvSentenceMean.setText(w.SentenceMean);
        tvSynonyms.setText(w.Synonyms);
        if (getIntent().getIntExtra("Id", 0) == 1) {
            btnPrev.setEnabled(false);
            btnPrev.setAlpha(0.5f);
        } else {
            btnPrev.setEnabled(true);
            btnPrev.setAlpha(1f);
        }
        if (getIntent().getIntExtra("Id", 0) == 600) {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.5f);
        } else {
            btnNext.setEnabled(true);
            btnNext.setAlpha(1f);
        }
    }

    public void setupsDic() {
        wvDetails.loadDataWithBaseURL("", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html>  <head>  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><style>.title{font-weight:bold; color:#1A237E; font-size: 18px}.type{color:#FF9800} body{color: #424242}</style></head>  <body>" + w.AdequateMean + "</body></html>", "text/html", "UTF-8", "");
        tvWordsType.setText(w.Word + " (" + w.Type + ")");
        tvPhonetic.setText(w.Phonetic);
        tvMean.setText(w.Mean);
        tvSentence.setText(w.Sentence);
        tvSentenceMean.setText(w.SentenceMean);
        tvSynonyms.setText(w.Synonyms);
        if (getIntent().getIntExtra("Id", 0) == 1) {
            btnPrev.setEnabled(false);
            btnPrev.setAlpha(0.5f);
        } else {
            btnPrev.setEnabled(true);
            btnPrev.setAlpha(1f);
        }
        if (getIntent().getIntExtra("Id", 0) == 600) {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.5f);
        } else {
            btnNext.setEnabled(true);
            btnNext.setAlpha(1f);
        }
    }

    public Word getWord() {
        db = new DatabaseWord(getApplication());
        w = db.queryWordWithId(getIntent().getIntExtra("Id", 1));
        lw = new ListWord(w.id, w.Word, w.Mean, w.FavouriteWord);
        return w;
    }

    public void letsSpeak() {
        if (InternetReceiver.network) {
            Intent itSpeak = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            itSpeak.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            itSpeak.putExtra("android.speech.extra.LANGUAGE", Locale.US.getLanguage().toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                Intent itNext = new Intent(this, DetailsWord.class);
                itNext.putExtra("Id", w.id + 1);
                itNext.putExtra("from", getIntent().getStringExtra("from"));
                startActivity(itNext);
                overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
                finish();
                break;
            case R.id.btnPrev:
                Intent itPrev = new Intent(this, DetailsWord.class);
                itPrev.putExtra("Id", w.id - 1);
                itPrev.putExtra("from", getIntent().getStringExtra("from"));
                startActivity(itPrev);
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
                break;
            case R.id.cbDetails:
                if (!cbDetails.isChecked()) {
                    cbDetails.setChecked(false);
                    lw.setCheck(false);
                } else {
                    cbDetails.setChecked(true);
                    lw.setCheck(true);
                }
                db.updateFavourite(lw);
                break;
            case R.id.btnSpeak:
                letsSpeak();
                break;
            case R.id.btnListen:
                OpenMp3.a(getApplicationContext()).playMp3(w.id);
                break;
        }
    }

    public Snackbar thongBao(String msg) {
        return Snackbar.make(rootDetails, msg, Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(w.Word + " (" + w.id + ")");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = null;
                String from = getIntent().getStringExtra("from");
                if (from.equals("HOC_TU")) {
                    it = new Intent(DetailsWord.this, HocTuActivity.class);
                } else if (from.equals("LIST_WORD")) {
                    it = new Intent(DetailsWord.this, ListWordWithSubjectActivity.class);
                    it.putExtra("word", w.Subject);
                } else if (from.equals("MAIN")) {
                    it = new Intent(DetailsWord.this, MainActivity.class);
                }
                startActivity(it);
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = null;
        String from = getIntent().getStringExtra("from");
        if (from.equals("HOC_TU")) {
            it = new Intent(DetailsWord.this, HocTuActivity.class);

        } else if (from.equals("LIST_WORD")) {
            it = new Intent(DetailsWord.this, ListWordWithSubjectActivity.class);
            it.putExtra("word", w.Subject);
        } else if (from.equals("MAIN")) {
            it = new Intent(DetailsWord.this, MainActivity.class);
        }
        startActivity(it);
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }
}
