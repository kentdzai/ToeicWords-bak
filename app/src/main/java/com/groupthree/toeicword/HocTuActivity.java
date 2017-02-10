package com.groupthree.toeicword;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;
import com.groupthree.toeicword.view.AdapterListWordWithListView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HocTuActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvHocTu;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Button btnHocTu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tu);
        init();
    }

    private void init() {
        setupActionBar();
        lvHocTu = (ListView) findViewById(R.id.lvHocTu);
        btnHocTu = (Button) findViewById(R.id.btnHocTu);
        btnHocTu.setOnClickListener(this);
        checkNull();
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    private AdapterListWordWithListView showListWord() {
        arrL = getListWord();
        db = new DatabaseWord(getApplicationContext());
        AdapterListWordWithListView adapter = new AdapterListWordWithListView(HocTuActivity.this, R.layout.adapter_list_word, arrL, db);
        return adapter;
    }

    public void thongBao() {
        final SweetAlertDialog dialog = new SweetAlertDialog(HocTuActivity.this);
        dialog.setTitleText("Bạn có muốn thêm từ để học");
        dialog.setContentText("Bạn cần có ít nhất 4 từ đánh dấu !");
        dialog.setConfirmText("Đồng ý");
        dialog.show();
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                startActivity(new Intent(HocTuActivity.this, ListSubjectActivity.class));
                overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
                dialog.cancel();
                finish();
            }
        });
        dialog.setCancelText("Hủy");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                startActivity(new Intent(HocTuActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                dialog.cancel();
                finish();
            }
        });
    }

    public void checkNull() {
        if (getListWord().size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    thongBao();
                }
            }, 300);

        } else {
            lvHocTu.setAdapter(showListWord());
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHocTu:
                if (getListWord().size() < 4) {
                    thongBao();
                } else {
                    LuyenTapDialog cdd = new LuyenTapDialog(HocTuActivity.this);
                    cdd.show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(HocTuActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HocTuActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }
}
