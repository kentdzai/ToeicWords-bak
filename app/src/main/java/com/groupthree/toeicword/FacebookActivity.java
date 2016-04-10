package com.groupthree.toeicword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class FacebookActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook);
        init();
    }

    private void init() {
        showShare();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}
