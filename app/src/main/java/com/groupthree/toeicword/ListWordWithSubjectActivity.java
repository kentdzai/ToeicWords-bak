package com.groupthree.toeicword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;
import com.groupthree.toeicword.view.AdapterListWordWithListView;

import java.util.ArrayList;

public class ListWordWithSubjectActivity extends AppCompatActivity {
    ListView lvTuVung;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    AdapterListWordWithListView adapter;
    String title[] = {"Chọn tất cả", "Bỏ chọn tất cả"};
    MenuItem choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_word);
        init();
    }

    private void init() {
        setupActionBar();
        lvTuVung = (ListView) findViewById(R.id.lvListWord);
        showListWord();
    }

    public String getValue() {
        Intent it = getIntent();
        String key = it.getStringExtra("word");
        return key;
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE Subject = '" + getValue() + "'");
        return arrL;
    }

    public void showListWord() {
        db = new DatabaseWord(getApplicationContext());
        adapter = new AdapterListWordWithListView(ListWordWithSubjectActivity.this, R.layout.adapter_list_word, getListWord(), db);
        lvTuVung.setAdapter(adapter);
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listword, menu);
        choose = menu.findItem(R.id.action_listword);
        db = new DatabaseWord(getApplicationContext());
        boolean check = db.checkFavourite(getValue());
        if (check) {
            choose.setTitle(title[1]);
        } else {
            choose.setTitle(title[0]);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ListWordWithSubjectActivity.this, ListSubjectActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
                break;
            case R.id.action_listword:
                choose = item;
                String it = choose.getTitle().toString();
                if (it.equals(title[0])) {
                    thongBao("Chọn tất cả", "Bạn có muốn chọn tất cả?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    choose.setTitle(title[1]);
                                    db = new DatabaseWord(getApplicationContext());
                                    db.updateFavouriteWithSubject(1, getValue());
                                    for (int i = 0; i < arrL.size(); i++) {
                                        arrL.get(i).setCheck(true);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Hủy", null).show();
                }
                if (it.equals(title[1])) {
                    thongBao("Bỏ chọn tất cả", "Bạn có muốn bỏ chọn tất cả?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    choose.setTitle(title[0]);
                                    db = new DatabaseWord(getApplicationContext());
                                    db.updateFavouriteWithSubject(0, getValue());
                                    for (int i = 0; i < arrL.size(); i++) {
                                        arrL.get(i).setCheck(false);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Hủy", null).show();
                }
                break;
        }
        return super.onOptionsItemSelected(choose);
    }

    public AlertDialog.Builder thongBao(String titlte, String msg) {
        return new AlertDialog.Builder(ListWordWithSubjectActivity.this).setTitle(titlte).setMessage(msg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListWordWithSubjectActivity.this, ListSubjectActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }

}
