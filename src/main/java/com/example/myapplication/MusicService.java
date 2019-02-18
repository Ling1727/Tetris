package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


import static com.example.myapplication.R.*;

/**
 * Created by hasee on 2018/6/1.
 */

public class MusicService extends Service {
    IBinder binder = new MyBinder();
    MediaPlayer mp = new MediaPlayer();



    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }



    public class MyBinder extends Binder {
        public void ready(){
            mp = MediaPlayer.create(MusicService.this, raw.the_truth_that_you_leave);
        }
        public void start(){
            if(!mp.isPlaying()){
                mp.start();
            }
        }
        public void stop(){
            if(mp.isPlaying()){
                mp.stop();
            }
        }
        public void pause(){
            if(mp.isPlaying()){
                mp.pause();
            }
        }
    }
}




