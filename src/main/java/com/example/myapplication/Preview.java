package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

/**
 * Created by hasee on 2019/2/15.
 */

public class Preview extends View {
    //预览区
    private int[][] preview=new int[4][4];
    //画笔
    private Paint previewPaint,boxPaint;
    //方块大小
    private int boxSize;
    //生成预览方块的类型
    private int type;
    //方块
    private Point[] box;

    public Preview(Context context,int boxSize) {
        super(context);
        this.boxSize=boxSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*previewPaint=new Paint();
        previewPaint.setColor(Color.parseColor("#CDC9C9"));
        previewPaint.setStrokeWidth(5);
        previewPaint.setAntiAlias(true);
        for(int i=0;i<6;i++){
            canvas.drawLine(i*boxSize,0,i*boxSize,3*boxSize,previewPaint);
        }
        for(int i=0;i<4;i++){
            canvas.drawLine(0,i*boxSize,5*boxSize,i*boxSize,previewPaint);
        }*/

        boxPaint=new Paint();
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStrokeWidth(5);
        boxPaint.setAntiAlias(true);
        for(int i=0;i<box.length;i++){
            canvas.drawRect(box[i].x*boxSize+5,box[i].y*boxSize,box[i].x*boxSize+boxSize,box[i].y*boxSize+boxSize-5,boxPaint);
        }
        invalidate();
    }

    //生成预览方块
    public void newBox(int type){
        switch(type){
            case 0:
                box=new Point[]{new Point(1,0),new Point(2,0),new Point(1,1),new Point(2,1)};
                break;
            case 1:
                box=new Point[]{new Point(1,0),new Point(1,1),new Point(1,2),new Point(2,2)};
                break;
            case 2:
                box=new Point[]{new Point(1,2),new Point(2,1),new Point(2,0),new Point(2,2)};
                break;
            case 3:
                box=new Point[]{new Point(1,0),new Point(1,1),new Point(2,1),new Point(2,2)};
                break;
            case 4:
                box=new Point[]{new Point(2,0),new Point(2,1),new Point(1,1),new Point(1,2)};
                break;
            case 5:
                box=new Point[]{new Point(1,1),new Point(2,1),new Point(3,1),new Point(0,1)};
                break;
            case 6:
                box=new Point[]{new Point(2,0),new Point(2,1),new Point(1,1),new Point(3,1)};
                break;
        }
    }
}
