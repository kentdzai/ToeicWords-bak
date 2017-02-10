package com.groupthree.toeicword.controller.listen;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class OpenMp3 {
    static OpenMp3 a;
    Context context;
    MediaPlayer player1;

    public OpenMp3(Context context) {
        this.context = context;
    }

    public static OpenMp3 a(Context context) {
        if (a != null) {
            return a;
        }
        a = new OpenMp3(context);

        return a;
    }

    public MediaPlayer playMp3(int Id) {
        MediaPlayer player2 = new MediaPlayer();
        player1 = player2;

        if (player2.isPlaying()) {
            player2.stop();
            player2.release();
            player1 = new MediaPlayer();
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return player1;
        }
        try {
            player1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor descriptor = context.getAssets().openFd("audio/" + Id + ".mp3");
            player1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player1.prepare();
            player1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player1;
    }


}
