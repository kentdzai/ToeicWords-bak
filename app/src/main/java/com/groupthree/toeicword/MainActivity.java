package com.groupthree.toeicword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.groupthree.toeicword.controller.InternetReceiver;
import com.groupthree.toeicword.controller.khoamanhinh.LockScreenService;
import com.groupthree.toeicword.controller.khoamanhinh.ServiceTest;
import com.groupthree.toeicword.controller.nhactu.NhacTuService;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    ArrayAdapter<String> adapterSearch;
    ArrayList<String> arrSearch;

    ListView lvMain;
    ArrayList<NavigationMain> arrN;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    final String[] btnNhacTu = {"Bật nhắc từ", "Tắt nhắc từ"};
    final String[] btnKhoaManHinh = {"Bật khóa màn hình", "Tắt khóa màn hình"};

    String Title[] = {
            "Từ vựng theo chủ đề",
            "Học từ đã đánh dấu",
            "Bật khóa màn hình",
            "Bật nhắc từ",
            "Cài đặt",
            "Chia sẻ",
            "Hướng dẫn"};

    final int Image[] = {
            R.mipmap.ic_hoctuvungtheochude,
            R.mipmap.ic_hoctukho,
            R.mipmap.ic_khoamanhinh,
            R.mipmap.ic_nhactu,
            R.mipmap.ic_setting,
            R.mipmap.ic_facebook,
            R.mipmap.ic_huongdan};

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    AdapterMain adapter;
    LinearLayout rootMain;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Intent itReceiver;
    Intent itLockScreenService;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCheckNetwork();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        init();
        itReceiver = new Intent("onAnswer");
        itLockScreenService = new Intent(getApplicationContext(), ServiceTest.class);
        if (getListWord().size() < 2) {
            arrN.set(2, new NavigationMain(Image[2], "Bật khóa màn hình"));
            editor.putBoolean("khoa_man_hinh", false);
            stopService(itLockScreenService);
        }
        editor.commit();
        handleIntent(getIntent());
    }

    public void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        editor = pref.edit();
        lvMain = (ListView) findViewById(R.id.lvMain);
        rootMain = (LinearLayout) findViewById(R.id.rootMain);
        setTypeNhacTu(0);
        setTypeKhoaManHinh(0);
        setupNav();
        openOrcreateDatabase();
    }

    public void setTypeKhoaManHinh(int type) {
        boolean khoa_man_hinh = pref.getBoolean("khoa_man_hinh", false);
        if (type == 0) {
            if (khoa_man_hinh) {
                Title[2] = btnKhoaManHinh[1];
            } else {
                Title[2] = btnKhoaManHinh[0];
            }
        } else {
            Intent itLockScreenService = new Intent(MainActivity.this, LockScreenService.class);
            if (khoa_man_hinh) {
                Title[2] = btnKhoaManHinh[1];
                startService(itLockScreenService);
            } else {
                Title[2] = btnKhoaManHinh[0];
                stopService(itLockScreenService);
            }
        }
    }

    public void setTypeNhacTu(int type) {
        boolean nhac_tu = pref.getBoolean("nhac_tu", false);
        if (type == 0) {
            if (nhac_tu) {
                Title[3] = btnNhacTu[1];
            } else {
                Title[3] = btnNhacTu[0];
            }
        } else {
            Intent itNhacTuService = new Intent(MainActivity.this, NhacTuService.class);
            if (nhac_tu) {
                Title[3] = btnNhacTu[1];
                itNhacTuService.putExtra("nhac_tu", true);
                startService(itNhacTuService);
            } else {
                Title[3] = btnNhacTu[0];
                itNhacTuService.putExtra("nhac_tu", false);
                stopService(itNhacTuService);
            }
        }
    }

    public void setupNav() {
        arrN = new ArrayList<>();
        for (int i = 0; i < Title.length; i++) {
            arrN.add(new NavigationMain(Image[i], Title[i]));
        }
        adapter = new AdapterMain(MainActivity.this, R.layout.adapter_main, arrN);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(this);
    }

    public void openOrcreateDatabase() {
        DatabaseWord db = new DatabaseWord(getApplicationContext());
        db.createDb();
    }

    public void handleStartActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
        overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
    }

    public Snackbar handlerMessage(String msg) {
        return Snackbar.make(rootMain, msg, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                handleStartActivity(ListSubjectActivity.class);
                finish();
                break;
            case 1:
                handleStartActivity(HocTuActivity.class);
                finish();
                break;
            case 2:
                TextView tvKhoaManHinh = (TextView) view.findViewById(R.id.lvm_title);
                if (getListWord().size() < 2) {
                    stopService(itLockScreenService);
                    editor.putBoolean("khoa_man_hinh", false);
                    thongBao().show();
                } else {
                    String kmh = tvKhoaManHinh.getText().toString();
                    if (kmh.equals(btnKhoaManHinh[0])) {
                        tvKhoaManHinh.setText(btnKhoaManHinh[1]);
                        startService(itLockScreenService);
                        sendBroadcast(itReceiver);
                        editor.putBoolean("khoa_man_hinh", true);
                    }
                    if (kmh.equals(btnKhoaManHinh[1])) {
                        tvKhoaManHinh.setText(btnKhoaManHinh[0]);
                        stopService(itLockScreenService);
                        editor.putBoolean("khoa_man_hinh", false);
                    }
                    editor.commit();
                }
                break;
            case 3:
                TextView tvNhacTu = (TextView) view.findViewById(R.id.lvm_title);
                String batOrTat = tvNhacTu.getText().toString();
                if (batOrTat.equals(btnNhacTu[0])) {
                    tvNhacTu.setText(btnNhacTu[1]);
                    editor.putBoolean("nhac_tu", true);
                }
                if (batOrTat.equals(btnNhacTu[1])) {
                    tvNhacTu.setText(btnNhacTu[0]);
                    editor.putBoolean("nhac_tu", false);
                }
                editor.commit();
                break;
            case 4:
                handleStartActivity(SettingsActivity.class);
                finish();
                break;
            case 5:
                if (InternetReceiver.network) {
                    showShare();
                } else {
                    handlerMessage("Kiểm tra kết nối mạng.").show();
                }
                break;
            case 6:
                handleStartActivity(HelpActivity.class);
                finish();
                break;
        }
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    public ArrayList<String> listWord2() {
        arrSearch = new ArrayList<>();
        db = new DatabaseWord(getApplicationContext());
        arrSearch = db.queryListWord2("SELECT Word FROM Word");
        return arrSearch;
    }

    public AlertDialog.Builder thongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Học từ đã đánh dấu");
        builder.setMessage("Bạn vui lòng đánh dấu ít nhất 2 từ để thực hiện chức năng này !");
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, ListSubjectActivity.class));
                finish();
            }
        });
        builder.setPositiveButton("Hủy", null);
        return builder;
    }

    InternetReceiver internetReceiver;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("nhac_tu")) {
            setTypeNhacTu(1);
        }
        if (key.equals("khoa_man_hinh")) {
            setTypeKhoaManHinh(1);
        }
    }

    public void setupCheckNetwork() {
        internetReceiver = new InternetReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver, filter);
    }

    public void showShare() {
        shareDialog = new ShareDialog(this);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Một ứng dụng học từ vựng tiếng anh thật tuyệt vời")
                .setImageUrl(Uri.parse("https://scontent-hkg3-1.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/1530451_594845904003981_2510383800762108996_n.jpg?oh=39a9e6b6c53578b9378af38fcd5db749&oe=5787DE5D"))
                .setContentDescription("Tải và cài đặt ngay hôm nay")
                .setContentUrl(Uri.parse("https://www.facebook.com/hoc600tuvungtienganh/"))
                .build();
        shareDialog.show(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
                LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
                LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                final AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
                listWord2();
                adapterSearch = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arrSearch);
                autoComplete.setAdapter(adapterSearch);
                autoComplete.setDropDownBackgroundResource(android.R.color.background_light);

                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        db = new DatabaseWord(getApplicationContext());
                        int result = db.queryIdWithWord(tv.getText().toString());
                        if (result != -1) {
                            Intent it = new Intent(MainActivity.this, DetailsWord.class);
                            it.putExtra("from", "MAIN");
                            it.putExtra("Id", result);
                            startActivity(it);
                        }
                    }
                });
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY).trim();
            db = new DatabaseWord(getApplicationContext());
            int result = db.queryIdWithWord(query);
            if (result != -1) {
                Intent it = new Intent(MainActivity.this, DetailsWord.class);
                it.putExtra("from", "MAIN");
                it.putExtra("Id", result);
                startActivity(it);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Không tìm thấy '" + query + "'").setMessage("Bạn có muốn dịch qua Google dịch?");
                builder.setPositiveButton("Hủy", null);
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (InternetReceiver.network) {
                            Intent ii = new Intent(MainActivity.this, SearchResultsActivity.class);
                            ii.putExtra("result", query);
                            startActivity(ii);
                        } else {
                            handlerMessage("Kiểm tra lại kết nối.").show();
                        }

                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetReceiver);
    }

    class AdapterMain extends ArrayAdapter<NavigationMain> {
        Activity activity;
        int idLayout;
        ArrayList<NavigationMain> arrN;
        ImageView ivMain;
        TextView tvMain;

        public AdapterMain(Activity activity, int idLayout, ArrayList<NavigationMain> arrN) {
            super(activity, idLayout, arrN);
            this.activity = activity;
            this.idLayout = idLayout;
            this.arrN = arrN;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
            ivMain = (ImageView) v.findViewById(R.id.lvm_ico);
            tvMain = (TextView) v.findViewById(R.id.lvm_title);
            NavigationMain n = arrN.get(position);
            ivMain.setImageResource(n.image);
            tvMain.setText(n.title);
            return v;
        }
    }

    class NavigationMain {
        public int image;
        public String title;

        public NavigationMain(int image, String title) {
            this.image = image;
            this.title = title;
        }
    }
}
