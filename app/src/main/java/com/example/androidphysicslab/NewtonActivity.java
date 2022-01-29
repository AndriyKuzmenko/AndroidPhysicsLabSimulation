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

public class NewtonActivity extends AppCompatActivity
{
    NewtonView newtonView;
    int planet;
    double m1,m2,g,mu,pixelsPerMeter,a,maxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m1=gi.getDoubleExtra("m1",0);
        m2=gi.getDoubleExtra("m2",0);
        mu=gi.getDoubleExtra("mu",0);
        planet=gi.getIntExtra("planet",-2);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];

        Log.d("m1","="+m1);
        Log.d("m2","="+m2);
        Log.d("mu","="+mu);
        Log.d("g","="+g);

        a=(m2*g-mu*m1*g)/(m1+m2);
        a=Math.max(a,0);

        if(a==0)
        {
            maxLength=1;
        }
        else
        {
            maxLength=0.5*a*100;
            maxLength*=2.2;
        }

        pixelsPerMeter=pixelsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/(3*maxLength);

        super.onCreate(savedInstanceState);
        newtonView=new NewtonView(this,m1,m2,mu,g,a,pixelsPerMeter);
        setContentView(newtonView);
    }
}

class NewtonView extends SurfaceView
{
    double m1,m2,mu,g,a,pixelsPerMeter;
    int top,right,bottom;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    Canvas canvas;
    boolean started;

    public NewtonView(Context context,double m1,double m2,double mu,double g,double a,double pixelsPerMeter)
    {
        super(context);

        this.m1=m1;
        this.m2=m2;
        this.mu=mu;
        this.g=g;
        this.a=a;
        this.pixelsPerMeter=pixelsPerMeter;

        paint=new Paint();
        surfaceHolder=getHolder();
        started=false;

        top=3*Resources.getSystem().getDisplayMetrics().heightPixels/5;
        right=3*Resources.getSystem().getDisplayMetrics().widthPixels/4;
        bottom=Resources.getSystem().getDisplayMetrics().heightPixels;
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
                        canvas = surfaceHolder.lockCanvas();
                        drawDesk(canvas);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                    public void drawDesk(Canvas canvas)
                    {
                        canvas.drawColor(Color.YELLOW);
                        paint.setColor(Color.RED);
                        canvas.drawRect(new Rect(0,top,right,bottom),paint);
                        paint.setColor(Color.rgb(55,0,55));
                        canvas.drawCircle(right+30,top-30,42,paint);
                    }
                },5,5);
            }
        }
        return false;
    }
}