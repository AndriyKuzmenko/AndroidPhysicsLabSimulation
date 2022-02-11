package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
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

public class DiscActivity extends AppCompatActivity
{
    DiscView discView;
    double m,mu,k,shift,g,v0,a,t,l0;
    int planet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("m",0);
        mu=gi.getDoubleExtra("mu",0);
        k=gi.getDoubleExtra("k",0);
        shift=gi.getDoubleExtra("shift",0);
        planet=gi.getIntExtra("planet",-2);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];

        v0=Math.sqrt(k*shift*shift/m);
        a=mu*g;
        t=v0/a;
        l0=v0*t-0.5*a*t*t;
        Log.d("TAG","v0="+v0+" a="+a+" t="+t+" l="+l0);

        double maxLength=5*l0;
        double pixlsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/maxLength;
        Log.d("TAG","maxlength="+maxLength+" pixelsPerMeter="+pixlsPerMeter);

        super.onCreate(savedInstanceState);
        discView=new DiscView(this,shift,v0,a,pixlsPerMeter);
        setContentView(discView);
    }
}

class DiscView extends SurfaceView
{
    double shift,v0,a,pixelsPerMeter,v,l,deltax;
    int rulerPosition,halfWidth;
    boolean started,hit;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;

    public DiscView(Context context,double shift,double v0,double a,double pixelsPerMeter)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();

        this.shift=shift;
        this.v0=v0;
        this.a=a;
        this.pixelsPerMeter=pixelsPerMeter;
        deltax=shift;
        l=0;

        started=false;
        hit=false;
        rulerPosition=Resources.getSystem().getDisplayMetrics().heightPixels/2;
        halfWidth=Resources.getSystem().getDisplayMetrics().widthPixels/2;
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
                        if(!hit)
                        {
                            canvas=surfaceHolder.lockCanvas();
                            canvas.drawColor(Color.YELLOW);
                            paint.setStrokeWidth(15);
                            paint.setColor(Color.rgb(0,0,33));

                            int y=(int)(deltax*pixelsPerMeter);
                            int x=(int)Math.sqrt(halfWidth*halfWidth-y*y);
                            canvas.drawLine(halfWidth/2,rulerPosition,halfWidth/2+x,rulerPosition+y,paint);

                            paint.setColor(Color.RED);
                            canvas.drawCircle(halfWidth,rulerPosition-(int)(l*pixelsPerMeter)-45,30, paint);

                            surfaceHolder.unlockCanvasAndPost(canvas);

                            deltax-=shift/5;
                            if(deltax<0) hit=true;
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}