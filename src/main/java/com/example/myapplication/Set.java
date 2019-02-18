package com.example.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by hasee on 2018/5/31.
 */

public class Set extends Activity {
    RadioGroup rgSound;
    RadioButton rbSoundOpen,rbSoundStop;
    MusicService.MyBinder myBinder1;
    Button btOk;
    View.OnClickListener listener;
    ServiceConnection conn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        conn=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                myBinder1=(MusicService.MyBinder)iBinder;
                //监听
                rgSound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch(i){
                            case R.id.rbSoundOpen:
                                rbSoundOpen.setChecked(true);
                                myBinder1.start();
                                SharedPreferences sp1=getSharedPreferences("BackGround",0);
                                SharedPreferences.Editor editor1=sp1.edit();
                                editor1.putBoolean("music",true);
                                editor1.commit();
                                break;
                            case R.id.rbSoundStop:
                                rbSoundStop.setChecked(true);
                                myBinder1.pause();
                                SharedPreferences sp2=getSharedPreferences("BackGround",0);
                                SharedPreferences.Editor editor2=sp2.edit();
                                editor2.putBoolean("music",false);
                                editor2.commit();
                                break;
                        }
                    }
                });
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };

        super.onCreate(savedInstanceState);
        //打开布局
        setContentView(R.layout.set);
        //关联
        rgSound=findViewById(R.id.rgSound);
        rbSoundOpen=findViewById(R.id.rbSoundOpen);
        rbSoundStop=findViewById(R.id.rbSoundStop);
        btOk=findViewById(R.id.btOk);
        //设置
        SharedPreferences sp1 = getSharedPreferences("BackGround", 0);
        Boolean music = sp1.getBoolean("music", true);
        if (music){
            rbSoundOpen.setChecked(true);
        }else{
            rbSoundStop.setChecked(true);
        }
        //button监听
        listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btOk:
                        Intent data=new Intent();
                        data.putExtra("data1","test");
                        setResult(0x0002,data);
                        finish();
                        break;
                }
            }
        };
        btOk.setOnClickListener(listener);
        //打开绑定Service
        Intent service=new Intent(this,MusicService.class);
        bindService(service,conn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
    }
}
