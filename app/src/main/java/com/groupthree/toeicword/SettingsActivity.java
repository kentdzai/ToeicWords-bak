package com.groupthree.toeicword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.groupthree.toeicword.controller.MyLog;
import com.groupthree.toeicword.model.ToeicWordPreferences;
import com.groupthree.toeicword.controller.khoamanhinh.ServiceLockScreen;
import com.groupthree.toeicword.controller.nhactu.NhacTuService;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatPreferenceActivity {
    Preference kmh, nt, ds, color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setupActionBar();

        kmh = getPreferenceScreen().findPreference(ToeicWordPreferences.khoa_man_hinh);
        kmh.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent itLockScreenService = new Intent(getApplicationContext(), ServiceLockScreen.class);
                boolean bl = preference.getSharedPreferences().getBoolean(ToeicWordPreferences.khoa_man_hinh, false);
                if (getListWord().size() < 2) {
                    Toast.makeText(getApplicationContext(), "Đánh dấu ít nhất 2 từ để mở chức năng này !", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (bl) {
                        stopService(itLockScreenService);
                    } else {
                        startService(itLockScreenService);
                    }
                }
                return true;
            }
        });
        nt = getPreferenceScreen().findPreference(ToeicWordPreferences.nhac_tu);
        nt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent itNhacTu = new Intent(getApplicationContext(), NhacTuService.class);
                boolean bl = preference.getSharedPreferences().getBoolean(ToeicWordPreferences.nhac_tu, false);
                if (bl) {
                    MyLog.e("123" + true);
                    itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, true);
                    preference.getSharedPreferences().edit().putInt(ToeicWordPreferences.pos_nhac_tu, 0).commit();
                    startService(itNhacTu);
                } else {
                    MyLog.e("123" + false);
                    itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, false);
                    stopService(itNhacTu);
                }
                return true;
            }
        });
        color = getPreferenceScreen().findPreference(ToeicWordPreferences.color_nhac_tu);


        ds = getPreferenceScreen().findPreference(ToeicWordPreferences.list_color);
        ds.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!preference.getSharedPreferences().getBoolean(ToeicWordPreferences.color_nhac_tu, false)) {
                    return true;
                } else {
                    thongBao("Bạn phải tắt Chế độ màu sắc.");
                    return false;
                }
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            nt.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Intent itNhacTu = new Intent(getApplicationContext(), NhacTuService.class);
                    boolean bl = preference.getSharedPreferences().getBoolean(ToeicWordPreferences.nhac_tu, false);
                    if (bl) {
                        MyLog.e("123" + bl);
                        itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, true);
                        preference.getSharedPreferences().edit().putInt(ToeicWordPreferences.pos_nhac_tu, 0).commit();
                        startService(itNhacTu);
                    } else {
                        MyLog.e("123" + bl);
                        itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, false);
                        stopService(itNhacTu);
                    }
                    return true;
                }
            });
        }

    }

    public void thongBao(String msg) {
        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ListWord> getListWord() {
        DatabaseWord db = new DatabaseWord(getApplicationContext());
        return db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }

}
