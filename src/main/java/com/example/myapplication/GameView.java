package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

/**
 * Created by hasee on 2018/6/12.
 */

public class GameView extends View {
    //游戏窗口宽高
    private int height,width;
    //地图线条、方块、地图方块、预览画笔
    private Paint LinerPaint,boxPaint,mapPaint;
    //地图
    private int[][] map=new int[10][20];
    //方块的大小
    private int boxSize;
    //方块
    private Point[] box;
    //得分
    private int score=0;



    public GameView(Context context,int height,int width) {
        super(context);
        this.height=height;
        this.width=width;
        boxSize=width/10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinerPaint=new Paint();
        LinerPaint.setStrokeWidth(5);
        LinerPaint.setColor(Color.parseColor("#CDC9C9"));
        for(int i=0;i<11;i++){
            canvas.drawLine(i*boxSize,0,i*boxSize,20*boxSize,LinerPaint);
        }
        for(int i=0;i<20;i++){
            canvas.drawLine(0,i*boxSize,10*boxSize,i*boxSize,LinerPaint);
        }

        boxPaint=new Paint();
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStrokeWidth(5);
        boxPaint.setAntiAlias(true);
        for(int i=0;i<box.length;i++){
            canvas.drawRect(box[i].x*boxSize+5,box[i].y*boxSize,box[i].x*boxSize+boxSize,box[i].y*boxSize+boxSize-5,boxPaint);
        }

        mapPaint=new Paint();
        mapPaint.setColor(Color.parseColor("#DEB887"));
        mapPaint.setStrokeWidth(5);
        mapPaint.setAntiAlias(true);
        for(int a=0;a<map.length;a++){
            for(int b=0;b<map[a].length;b++){
                if(map[a][b]==1){
                    canvas.drawRect(a*boxSize+5,b*boxSize,a*boxSize+boxSize,b*boxSize+boxSize-5,mapPaint);
                }
            }
        }

        invalidate();
    }
     //左右移动及判定
    public boolean move(int x,int y){
        boolean isOver=false;
        for(int j=0;j<box.length;j++){
            if(box[j].x+x<0||box[j].x+x>9||map[box[j].x+x][box[j].y+y]==1||box[j].y + y > 19){
                isOver=true;
            }
        }
        if(!isOver){
            for(int i=0;i<box.length;i++){
                box[i].x+=x;
                box[i].y+=y;
            }
        }
        return isOver;
    }

    //下落及判定
    public boolean down(){
        boolean isOver=false;
        for(int j=0;j<box.length;j++) {
            if (box[j].y + 1 > 19||map[box[j].x][box[j].y+1]==1) {
                for (int a = 0; a < box.length; a++) {
                    map[box[a].x][box[a].y] = 1;
                }
                isOver = true;
            }
        }
        if(!isOver){
            for(int i=0;i<box.length;i++){
                box[i].y+=1;
            }
        }
        return isOver;
    }

    //旋转及判定
    public void rotate(){
        boolean isOver=false;
        for(int j=0;j<box.length;j++){
            int a=-box[j].y+box[1].x+box[1].y;
            int b=box[j].x-box[1].x+box[1].y;
            if(a<0||a>9||b<0||b>19||map[a][b]==1){
                isOver=true;
            }
        }
        if(!isOver){
            for(int i=0;i<box.length;i++){
                int x=-box[i].y+box[1].x+box[1].y;
                int y=box[i].x-box[1].x+box[1].y;
                box[i].x=x;
                box[i].y=y;
            }

        }
    }


    //随机生成方块并返回下一个方块类型
    public int boxBulid(int type){
        switch(type){
            case 0:
                box=new Point[]{new Point(4,0),new Point(5,0),new Point(4,1),new Point(5,1)};
                break;
            case 1:
                box=new Point[]{new Point(4,0),new Point(4,1),new Point(4,2),new Point(5,2)};
                break;
            case 2:
                box=new Point[]{new Point(4,2),new Point(5,1),new Point(5,0),new Point(5,2)};
                break;
            case 3:
                box=new Point[]{new Point(4,0),new Point(4,1),new Point(5,1),new Point(5,2)};
                break;
            case 4:
                box=new Point[]{new Point(5,0),new Point(5,1),new Point(4,1),new Point(4,2)};
                break;
            case 5:
                box=new Point[]{new Point(3,0),new Point(4,0),new Point(5,0),new Point(6,0)};
                break;
            case 6:
                box=new Point[]{new Point(4,0),new Point(4,1),new Point(3,1),new Point(5,1)};
                break;
        }
        return (int)(Math.random()*7);
    }


    //消行
    public Boolean decide(){
        int x=0;
        //横向遍历地图数组，找出等于1的方块
        for(int a=19;a>=0;a--){
            for(int b=0;b<map.length;b++){
                //判定是否为1,为1则x++
                if(map[b][a]==1){
                    x++;
                }
            }
            //若x为10则整行为1，则需消除此行
            if(x==10){
                //将改行全赋值为0达到消行目的
                for (int c=0;c<map.length;c++){
                    map[c][a]=0;
                    score++;
                }
                //从消行的上一行开始遍历，把值为1的往下移一行
                for(int d=a-1;d>=0;d--){
                    for(int e=0;e<map.length;e++){
                        if(map[e][d]==1){
                            map[e][d]=0;
                            map[e][d+1]=1;
                        }
                    }
                }
                a++;
            }else if(x==0){
                break;
            }
            if(a==0){
                return true;
            }
            x=0;
        }
        return false;
    }

    //初始化地图
    public  void initMap(){
        for(int a=0;a<map.length;a++){
            for(int b=0;b<map[a].length;b++){
                map[a][b]=0;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
