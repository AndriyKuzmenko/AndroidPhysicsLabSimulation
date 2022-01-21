package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SpringActivity extends AppCompatActivity
{
    SpringView springView;
    double m,g,k,pixelsPerMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("mass",0);
        int planet=gi.getIntExtra("planet",0);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];
        k=gi.getDoubleExtra("k",0);

        double height=2.3*m*g/k;
        pixelsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/(height);

        Log.d("values","m="+m+"  k="+k+"  g="+g);
        Log.d("values","h="+height+"   meter="+pixelsPerMeter);

        super.onCreate(savedInstanceState);
        springView=new SpringView(this,m,g,k,pixelsPerMeter);
        setContentView(springView);
    }
}

class SpringView extends SurfaceView
{
    private SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    double m,g,k,pixelsPerMeter;
    Rect ceiling;
    double deltaX;
    int middle,left,right;
    boolean started;

    public SpringView(Context context, double m, double g, double k, double pixelsPerMeter)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();

        this.m=m;
        this.g=g;
        this.k=k;
        this.pixelsPerMeter=pixelsPerMeter;
        ceiling=new Rect(0,0,Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels/10);
        deltaX=ceiling.bottom+35;
        middle=Resources.getSystem().getDisplayMetrics().widthPixels/2;
        left=(int)(middle*0.7);
        right=(int)(middle*1.3);
        started=false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (surfaceHolder.getSurface().isValid() && !started)
            {
                Timer t=new Timer();
                t.scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        paint.setColor(Color.RED);
                        started=true;

                        int difference=(int)(deltaX-ceiling.bottom)/7;
                        int x1=middle;
                        int x2=right;
                        int y1=ceiling.bottom;
                        int y2=ceiling.bottom+difference;

                        for(int i=0;i<7;i++)
                        {
                            canvas.drawLine(x1,y1,x2,y2,paint);
                            y1+=difference;
                            y2+=difference;
                            if(i==6)
                            {
                                x1=left;
                                x2=middle;
                            }
                            else if(i%2==0)
                            {
                                x1=right;
                                x2=left;
                            }
                            else
                            {
                                x1=left;
                                x2=right;
                            }
                        }

                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                },5,5);
            }
        }
        return true;
    }
}