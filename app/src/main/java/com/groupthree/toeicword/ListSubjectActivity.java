package com.groupthree.toeicword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.Subject;

import java.util.ArrayList;

public class ListSubjectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvChuDe;
    DatabaseWord db;
    ArrayList<Subject> arrS;
    AdapterChuDe adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        init();
    }

    private void init() {
        setupActionBar();
        lvChuDe = (ListView) findViewById(R.id.lvSubject);
        getSubject();
        showSubject();
        lvChuDe.setOnItemClickListener(this);
    }

    private void showSubject() {
        adapter = new AdapterChuDe(ListSubjectActivity.this, R.layout.adapter_list_subject, getSubject());
        lvChuDe.setAdapter(adapter);
    }

    public ArrayList<Subject> getSubject() {
        db = new DatabaseWord(getApplicationContext());
        arrS = db.querySubject();
        return arrS;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(ListSubjectActivity.this, ListWordWithSubjectActivity.class).putExtra("word", arrS.get(position).Subject));
        overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
        finish();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setSubtitle("Nhấn giữ vào chủ đề để chỉnh sửa");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ListSubjectActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class AdapterChuDe extends ArrayAdapter<Subject> {
        Activity activity;
        int idLayout;
        ArrayList<Subject> arrS;
        TextView tvSubject, tvSubjectMean, tvCount;

        public AdapterChuDe(Activity activity, int idLayout, ArrayList<Subject> arrS) {
            super(activity, idLayout, arrS);
            this.activity = activity;
            this.idLayout = idLayout;
            this.arrS = arrS;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = activity.getLayoutInflater().inflate(idLayout, null);
            tvSubject = (TextView) v.findViewById(R.id.tvSubject);
            tvSubjectMean = (TextView) v.findViewById(R.id.tvSubjectMean);
            tvCount = (TextView) v.findViewById(R.id.tvCountFavouriteWord);
            Subject s = arrS.get(position);
            tvSubject.setText(s.Subject);
            tvSubjectMean.setText(s.SubjectMean);
            tvCount.setText("("+s.countFavouriteWord+")");
            return v;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListSubjectActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }
}

