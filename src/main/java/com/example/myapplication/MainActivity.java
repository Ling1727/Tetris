package com.example.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button btStartGame,btTop,btSet;
    private MusicService.MyBinder myBinder;
    private ServiceConnection conn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service();
        //打开布局
        setContentView(R.layout.activity_main);
        UIThings();
    }


    //按键监听
    public void UIThings(){
        btStartGame=findViewById(R.id.btStartGame);
        btTop=findViewById(R.id.btTop);
        btSet=findViewById(R.id.btSet);
        //按键设置为透明
        //btStartGame.getBackground().setAlpha(0);
        //btTop.getBackground().setAlpha(0);
        //btSet.getBackground().setAlpha(0);
        //按键监听
        Button.OnClickListener listener=new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    //带返回参数的打开设置界面
                    case R.id.btSet:
                        Intent intent=new Intent("com.SET");
                        startActivityForResult(intent,0x0001);

                        //intent.setAction("com.SET");
                        //startActivity(intent);
                        break;
                        //打开游戏界面
                    case R.id.btStartGame:
                        Intent intent1=new Intent("com.game");
                        startActivity(intent1);
                        break;
                        //打开排行榜界面
                    case R.id.btTop:
                        break;
                }
            }
        };
        btSet.setOnClickListener(listener);
        btStartGame.setOnClickListener(listener);
        btTop.setOnClickListener(listener);
    }

    //准备背景音乐，默认播放背景音乐
    public void service(){
        //背景音乐
        conn= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                myBinder=(MusicService.MyBinder)iBinder;
                myBinder.ready();
                SharedPreferences sp = getSharedPreferences("BackGround", 0);
                Boolean music = sp.getBoolean("music", true);
                if (music) {
                    myBinder.start();
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        //打开绑定Service
        Intent service=new Intent(this,MusicService.class);
        bindService(service,conn,BIND_AUTO_CREATE);
    }

    //设置界面返回时获得返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0x0001&&resultCode==0x0002){
            String text=data.getStringExtra("data1");
            Log.i(text,"test");
        }
    }

    //界面被销毁时解除service绑定
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
        Log.d("new","onDestroy");
    }

    //界面被完全覆盖是暂停背景音乐
    @Override
    protected void onStop() {
        super.onStop();
        myBinder.pause();
        Log.d("new","onStop");
    }

    //界面重新回到活跃状态时判断背景音乐是否播放
    @Override
    protected void onRestart() {
        super.onRestart();
            SharedPreferences sp = getSharedPreferences("BackGround", 0);
            Boolean music = sp.getBoolean("music", true);
            if (music) {
                myBinder.start();
            }
        Log.d("new","onRestart");
    }



/*    @Override
    protected void onPause() {
        super.onPause();
        Log.d("new","onPause");
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("new","onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("new","onStart");
    }*/
}
