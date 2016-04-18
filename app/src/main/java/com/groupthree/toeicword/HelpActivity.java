package com.groupthree.toeicword;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
    ListView lvHelp;
    ArrayList<ListHelp> arrH;
    AdapterHelp adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        init();
    }

    private void init() {
        setupActionBar();
        lvHelp = (ListView) findViewById(R.id.lvHelp);
        showHelpCustom();
    }

    public ArrayList<ListHelp> getValues() {
        arrH = new ArrayList<>();
        String[] titles = {"Chức năng của ứng dụng", "Học qua màn hình khóa", "Học qua các bài luyện tập", "Chế độ nhắc từ"};
        String[] values = {"Hỗ trợ học 600 từ vựng Toeic nhanh nhất và đơn giản nhất với ba chế độ học: học qua màn hình khóa, học qua các bài luyện tập và học qua chế độ nhắc từ."
                , "Khi kích hoạt chế độ này, màn hình khóa của điện thoại sẽ được thay thế bằng màn hình khóa của ứng dụng.Để mở khóa, người dùng phải trả lời đúng từ vựng trên màn hình khóa là gì bằng cách chọn một trong hai đáp án. Để phòng trừ trường hợp khẩn cấp, chúng tôi đã thêm chức năng 'Mở khóa gấp' cho phép bạn mở khóa màn hình mà không cần trả lời câu hỏi."
                , "Để hỗ trợ học nhanh 600 từ, chúng tôi hỗ trợ bốn phương thức luyện tập là: nghe lặp, lật từ, trắc nghiệm và luyện nói. Để có thể thực hiện các bài tập này, người dùng phải vào mục 'Từ vựng theo chủ đề' sau đó tích vào các chủ để cần luyện tập và quay lại màn hình chính để 'Học từ đã đánh dấu'."
                , "Đây là một chế độ tiện dụng, giúp người dùng có thể học mọi lúc mọi nơi. Khi kích hoạt chế độ này, ứng dụng sẽ hiện lên một cửa sổ nhỏ, có một từ vựng và nghĩa của từ nó. Khi bạn dùng điện thoại, thì nó sẽ đập vào mắt bạn và đi vào bộ não một cách tự nhiên. Các bạn yên tập việc hiển thị của sổ này không hề ảnh hưởng đến việc tương tác với ứng dụng của bạn. Bạn có thể tùy chỉnh chế độ này thông qua 'Cài đặt'."};
        for (int i = 0; i < titles.length; i++) {
            arrH.add(new ListHelp(titles[i], values[i]));
        }
        return arrH;
    }

    public void showHelpCustom() {
        adapter = new AdapterHelp(HelpActivity.this, R.layout.adapter_help, getValues());
        lvHelp.setAdapter(adapter);
        lvHelp.setDividerHeight(0);
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
                startActivity(new Intent(HelpActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class AdapterHelp extends ArrayAdapter<ListHelp> {
        Activity activity;
        int idLayout;
        ArrayList<ListHelp> arrH;
        TextView tvTitles;
        TextView tvValues;

        public AdapterHelp(Activity activity, int idLayout, ArrayList<ListHelp> arrH) {
            super(activity, idLayout, arrH);
            this.activity = activity;
            this.idLayout = idLayout;
            this.arrH = arrH;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = activity.getLayoutInflater().inflate(idLayout, null);
            tvTitles = (TextView) v.findViewById(R.id.tvTitles);
            tvValues = (TextView) v.findViewById(R.id.tvValues);
            ListHelp h = arrH.get(position);
            tvTitles.setText(h.titles);
            tvValues.setText(h.values);
            return v;
        }
    }

    class ListHelp {
        public String titles;
        public String values;

        public ListHelp(String titles, String values) {
            this.titles = titles;
            this.values = values;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HelpActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }
}
