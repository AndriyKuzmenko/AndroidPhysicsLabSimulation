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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class NewtonActivity extends AppCompatActivity
{
    NewtonView newtonView;
    int planet;
    double m1,m2,g,mu,pixelsPerMeter,a,maxLength;
    final int seconds=5;

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
            maxLength=0.5*a*seconds*seconds;
            maxLength*=0.005;
        }

        pixelsPerMeter=pixelsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/(3*maxLength);

        Log.d("a=",""+a);
        Log.d("pixles",""+pixelsPerMeter);

        super.onCreate(savedInstanceState);
        newtonView=new NewtonView(this,m1,m2,mu,g,a,pixelsPerMeter,maxLength,seconds);
        setContentView(newtonView);
    }
}

class NewtonView extends SurfaceView
{
    double m1,m2,mu,g,a,pixelsPerMeter,x,y,maxLength,v,xMeter;
    int top,right,bottom,counter,seconds;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    Canvas canvas;
    boolean started;

    public NewtonView(Context context,double m1,double m2,double mu,double g,double a,double pixelsPerMeter,double maxLength,int seconds)
    {
        super(context);

        this.m1=m1;
        this.m2=m2;
        this.mu=mu;
        this.g=g;
        this.a=a;
        this.pixelsPerMeter=pixelsPerMeter;
        this.maxLength=maxLength;
        this.seconds=seconds;
        v=0;
        xMeter=0;
        counter=0;

        paint=new Paint();
        surfaceHolder=getHolder();
        started=false;

        top=3*Resources.getSystem().getDisplayMetrics().heightPixels/5;
        right=3*Resources.getSystem().getDisplayMetrics().widthPixels/4;
        bottom=Resources.getSystem().getDisplayMetrics().heightPixels;
        y=top+50;
        x=right-maxLength*pixelsPerMeter+25;
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
                        drawBoxes(canvas);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        v+=a*0.01;
                        xMeter+=v*0.01;
                        x+=v*pixelsPerMeter*0.01;
                        y+=v*pixelsPerMeter*0.01;

                        counter++;
                        if(counter==25)
                        {
                            t.cancel();
                        }
                    }

                    public void drawDesk(Canvas canvas)
                    {
                        canvas.drawColor(Color.YELLOW);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(new Rect(0,top,right,bottom),paint);
                        paint.setColor(Color.rgb(55,0,55));
                        canvas.drawCircle(right+30,top-30,42,paint);
                        paint.setColor(Color.rgb(0,0,55));
                        paint.setStrokeWidth(5);
                        canvas.drawLine((int)x-2,top-72,right+23,top-72,paint);
                        canvas.drawLine(right+72,top-30,right+72,(int)y+5,paint);
                    }

                    public void drawBoxes(Canvas canvas)
                    {
                        paint.setColor(Color.RED);
                        canvas.drawRect(new Rect((int)x-178,top-144,(int)x,top),paint);
                        canvas.drawRect(new Rect(right+5,(int)y,right+139,(int)y+100),paint);
                    }
                },5,5);
            }
        }
        return false;
    }
}