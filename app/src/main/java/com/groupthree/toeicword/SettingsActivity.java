package com.groupthree.toeicword;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.groupthree.toeicword.controller.ToeicWordPreferences;
import com.groupthree.toeicword.controller.khoamanhinh.ServiceTest;
import com.groupthree.toeicword.controller.nhactu.NhacTuService;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setupActionBar();
        Preference kmh = getPreferenceScreen().findPreference(ToeicWordPreferences.khoa_man_hinh);
        kmh.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent itLockScreenService = new Intent(getApplicationContext(), ServiceTest.class);
                Intent itReceiver = new Intent("onAnswer");
                boolean bl = preference.getSharedPreferences().getBoolean(ToeicWordPreferences.khoa_man_hinh, false);
                if (getListWord().size() < 2) {
                    Toast.makeText(getApplicationContext(), "Đánh dấu ít nhất 2 từ để mở chức năng này !", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (bl) {
                        stopService(itLockScreenService);
                    } else {
                        startService(itLockScreenService);
                        sendBroadcast(itReceiver);
                    }
                }
                return true;
            }
        });
        Preference nt = getPreferenceScreen().findPreference(ToeicWordPreferences.nhac_tu);
        nt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent itNhacTu = new Intent(getApplicationContext(), NhacTuService.class);
                boolean bl = preference.getSharedPreferences().getBoolean(ToeicWordPreferences.nhac_tu, false);
                if (bl) {
                    itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, true);
                    startService(itNhacTu);
                } else {
                    stopService(itNhacTu);
                    itNhacTu.putExtra(ToeicWordPreferences.nhac_tu, false);
                }
                return true;
            }
        });

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
