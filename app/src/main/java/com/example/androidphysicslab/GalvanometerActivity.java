package com.example.androidphysicslab;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GalvanometerActivity extends AppCompatActivity
{
    GalvanometerView galvanometerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        galvanometerView=new GalvanometerView(this);
        setContentView(galvanometerView);
    }
}

class GalvanometerView extends SurfaceView
{
    boolean started;
    Paint paint;
    SurfaceHolder surfaceHolder;
    int width,height,counter;
    Canvas canvas;

    public GalvanometerView(Context context)
    {
        super(context);
        started=false;
        paint=new Paint();
        surfaceHolder=getHolder();
        width= Resources.getSystem().getDisplayMetrics().widthPixels;
        height=Resources.getSystem().getDisplayMetrics().heightPixels;
        counter=10;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (surfaceHolder.getSurface().isValid() && !started)
            {
                started=true;
                Timer t=new Timer();
                t.scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        int arrowX=width*2/5+width*counter/50;

                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        paint.setStrokeWidth(10);
                        paint.setColor(Color.RED);
                        //circuit
                        canvas.drawLine(width/5,height/5,width/2-15,height/5,paint);
                        canvas.drawLine(width/2+15,height/5,width*4/5,height/5,paint);
                        canvas.drawLine(width/2-15,height/5-50,width/2-15,height/5+50,paint);
                        canvas.drawLine(width/2+15,height/5-25,width/2+15,height/5+25,paint);
                        canvas.drawLine(width/5,height/5,width/5,height*7/10,paint);
                        canvas.drawLine(width/5,height*7/10,width*2/5,height*7/10,paint);
                        canvas.drawLine(width*4/5,height/5,width*4/5,height/2,paint);
                        canvas.drawLine(width*4/5,height/2,arrowX,height/2,paint);
                        canvas.drawLine(arrowX,height/2,arrowX,height*7/10,paint);
                        //voltmeter
                        canvas.drawLine(width/2-90,height/5,width/2-90,height/5-150,paint);
                        canvas.drawLine(width/2+90,height/5,width/2+90,height/5-150,paint);
                        canvas.drawLine(width/2-90,height/5-150,width/2+90,height/5-150,paint);

                        paint.setStrokeWidth(70);
                        canvas.drawLine(width*2/5,height*7/10,width*3/5,height*7/10,paint);

                        paint.setColor(Color.BLUE);
                        canvas.drawCircle(width/5,height/2,50,paint);
                        canvas.drawCircle(width/2,height/5-150,50,paint);
                        canvas.drawCircle(width*4/5,height/3,50,paint);

                        paint.setColor(Color.WHITE);
                        paint.setTextSize(100);
                        canvas.drawText("A",width/5-36,height/2+36,paint);
                        canvas.drawText("V",width/2-36,height/5-114,paint);
                        paint.setTextSize(70);
                        canvas.drawText("TG",width*4/5-40,height/3+30,paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);

                        /*double r=maxR*counter/10;
                        double i=epsilon/(r+internalR);
                        double v=i*r;
                        Log.d("TAG","R="+r+"  I="+i+"  V="+v);

                        rList.add(r);
                        iList.add(i);
                        vList.add(v);*/

                        counter--;
                        if(counter==0)
                        {
                            t.cancel();
                        }
                    }
                },500,500);
            }
        }
        return true;
    }
}