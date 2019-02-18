package com.example.myapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hasee on 2018/6/12.
 */

public class Game extends Activity {
    //游戏界面宽度
    private int width;
    private LinearLayout game1,game2,llPreview;
    private Button btStop,btLeft,btRight,btChange,btDown;
    private View.OnClickListener listener;
    //游戏界面
    GameView gameView;
    //预览界面
    Preview preview;
    //下落线程
    private Thread downThrea;
    //暂停属性
    private Boolean isPause=false;
    //弹窗
    private DialogFragment0 dialogFragment0;
    //判断游戏暂停还是结束
    private String overOrPause;
    //生成方块的类型
    private int type;
    //分数
    private TextView tvCsore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);
        width=2*getWidth()/3;
        UIThings();
        initView();
        start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isPause=true;
                    dialog();
                    Toast.makeText(Game.this,"游戏结束",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void start(){
        if(downThrea==null) {
            downThrea = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isPause){
                            if(gameView.down()){
                                if(gameView.decide()){
                                    overOrPause="游戏结束";
                                    handler.sendEmptyMessage(0);
                                }else{
                                    type=gameView.boxBulid(type);
                                    preview.newBox(type);
                                }
                                tvCsore.setText(gameView.getScore()+"");
                            }
                        }
                    }
                }
            };
            downThrea.start();
        }
    }

    //获得屏幕宽高
    public int getWidth(){
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width;
    }

    //绘制画面
    public void initView(){
        gameView=new GameView(this,2*width,width);
        preview=new Preview(this,width/10);
        game1=findViewById(R.id.game1);
        ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(2*getWidth()/3,4*getWidth()/3);
        game1.setLayoutParams(layoutParams);
        game1.setBackgroundColor(Color.parseColor("#F0F8FF"));
        game1.addView(gameView);
        int x=(int)(Math.random()*7);
        //生成游戏方块
        type=gameView.boxBulid(x);
        //生成预览方块
        preview.newBox(type);
        gameView.setScore(0);
        tvCsore.setText(gameView.getScore()+"");

        ViewGroup.LayoutParams layoutParams1=new LinearLayout.LayoutParams(getWidth()/3,getWidth()/5);
        llPreview=findViewById(R.id.llPreview);
        llPreview.setLayoutParams(layoutParams1);
        llPreview.setBackgroundColor(Color.parseColor("#F0F8FF"));
        llPreview.addView(preview);

        game2=findViewById(R.id.game2);
        game2.setBackgroundColor(Color.parseColor("#F0F8FF"));
    }

    public void UIThings(){
        tvCsore=findViewById(R.id.tvScore);
        btStop=findViewById(R.id.btStop);
        btLeft=findViewById(R.id.btLeft);
        btRight=findViewById(R.id.btRight);
        btChange=findViewById(R.id.btChange);
        btDown=findViewById(R.id.btDown);
        listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btStop:
                        isPause=true;
                        overOrPause="游戏暂停";
                        dialog();
                        break;
                    case R.id.btLeft:
                        if(!isPause){
                            gameView.move(-1,0);
                        }
                        break;
                    case R.id.btRight:
                        if(!isPause){
                            gameView.move(1,0);
                        }
                        break;
                    case R.id.btChange:
                        if(!isPause){
                            gameView.rotate();
                        }
                        break;
                    case R.id.btDown:
                        if(!isPause){
                            while (true){
                                if(gameView.down()){
                                    if(gameView.decide()){
                                        overOrPause="游戏结束";
                                        handler.sendEmptyMessage(0);
                                    }else{
                                        type=gameView.boxBulid(type);
                                        preview.newBox(type);
                                    }
                                    tvCsore.setText(gameView.getScore()+"");
                                    break;
                                }
                            }
                        }
                }
            }
        };
        btStop.setOnClickListener(listener);
        btLeft.setOnClickListener(listener);
        btLeft.setOnClickListener(listener);
        btRight.setOnClickListener(listener);
        btChange.setOnClickListener(listener);
        btDown.setOnClickListener(listener);
    }

    //打开对话框
    public void dialog(){
        dialogFragment0=new DialogFragment0();
        dialogFragment0.setCancelable(false);
        Bundle arguments=new Bundle();
        arguments.putString("overOrPause",overOrPause);
        dialogFragment0.setArguments(arguments);
        dialogFragment0.show(getFragmentManager(),"dialog");
    }

    //关闭对话框
    public void dialogClose(){
        dialogFragment0.dismiss();
    }

    public boolean setPause(){
            isPause=false;
        return isPause;
    }

    //重新开始
    public void again(){
        gameView.initMap();
        int x=(int)(Math.random()*7);
        type=gameView.boxBulid(x);
        preview.newBox(type);
        gameView.setScore(0);
        tvCsore.setText(gameView.getScore()+"");
    }
    //返回主菜单
    public void back(){
        /*Intent intent=new Intent("android.intent.action.MAIN");
        startActivity(intent);*/
        this.finish();
    }

    @Override
    public void onBackPressed() {
        isPause=true;
        overOrPause="游戏暂停";
        dialog();
    }

    //进入onPause状态
    @Override
    protected void onPause() {
        super.onPause();
        if(isPause==false){
            isPause=true;
            overOrPause="游戏暂停";
            dialog();
        }
        Log.d("new","onPause");
    }
}
