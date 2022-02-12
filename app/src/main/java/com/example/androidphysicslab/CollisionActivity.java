package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CollisionActivity extends AppCompatActivity
{
    CollisionView collisionView;
    double h1,h2,g;
    int planet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        h1=gi.getDoubleExtra("h1",0);
        h2=gi.getDoubleExtra("h2",0);
        planet=gi.getIntExtra("planet",0);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];

        double maxHeight=2*(h1+h2);
        double pixlsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/maxHeight;

        super.onCreate(savedInstanceState);
        collisionView=new CollisionView(this,h1,h2,g,pixlsPerMeter);
        setContentView(collisionView);
    }
}

class CollisionView extends SurfaceView
{
    double h1,h2,g,pixelsPerMeter;
    boolean started;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    int width,height;

    public CollisionView(Context context,double h1,double h2,double g,double pixelsPerMeter)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();

        this.h1=h1;
        this.h2=h2;
        this.g=g;
        this.pixelsPerMeter=pixelsPerMeter;

        started=false;
        width=Resources.getSystem().getDisplayMetrics().widthPixels;
        height=Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
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
                        canvas=surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        paint.setStrokeWidth(20);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(new Rect(0,height*3/4,width,height),paint);
                        canvas.drawRect(new Rect(0,height*3/4-(int)(h1*pixelsPerMeter),width/2,height*3/4),paint);
                        paint.setColor(Color.rgb(0,0,33));
                        canvas.drawLine(width/3,(float)(height*3/4-h1*pixelsPerMeter),0,(float)(height*3/4-(h1+h2)*pixelsPerMeter),paint);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                },5,5);
            }
        }
        return true;
    }
}