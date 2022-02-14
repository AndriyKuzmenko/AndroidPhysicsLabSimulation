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
        Log.d("pixelspermeter=",""+pixlsPerMeter);

        super.onCreate(savedInstanceState);
        collisionView=new CollisionView(this,h1,h2,g,pixlsPerMeter);
        setContentView(collisionView);
    }
}

class CollisionView extends SurfaceView
{
    double h1,h2,g,pixelsPerMeter,a,alpha,sin,cos,vx,vy,ux,uy,x1,y1,x2,y2;
    boolean started;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    int width,height,m;

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

        y1=(int)(height*3/4-(h1+h2)*pixelsPerMeter-10);
        m=((int)y1-(int)(height*3/4))/(width/3);
        x1=(int)(y1-height*3/4+h1*pixelsPerMeter)/m;

        alpha=Math.atan(h2/(width/3/pixelsPerMeter));
        sin=Math.sin(alpha);
        cos=Math.cos(alpha);
        a=g*Math.sin(alpha);
        vx=vy=ux=uy=0;

        x2=width/2;
        y2=height*3/4-(int)(h1*pixelsPerMeter);
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
                        canvas.drawLine(width/3,(float)(height*3/4-h1*pixelsPerMeter),0,(float)(height*3/4-(h1+h2)*pixelsPerMeter-50),paint);
                        paint.setColor(Color.RED);
                        canvas.drawCircle((int)x1-20,(int)y1,20,paint);
                        canvas.drawCircle((int)x2,(int)y2-20,20,paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);

                        vy+=a*sin/100;
                        vx+=a*cos/100;
                        if(x1>=width/2-20 && y2<height*3/4)
                        {
                            uy+=g/100;
                        }
                        Log.d("TAG","vx="+vx+" vy="+vy+" x1="+x1+" ux="+ux+" x2="+x2);

                        x1+=vx*pixelsPerMeter/100;
                        y1+=vy*pixelsPerMeter/100;
                        x2+=ux*pixelsPerMeter/100;
                        y2+=uy*pixelsPerMeter/100;

                        if(y1>=height*3/4-(int)(h1*pixelsPerMeter)-20)
                        {
                            vx=Math.sqrt(vx*vx+vy*vy);
                            vy=0;
                            y1=height*3/4-(int)(h1*pixelsPerMeter)-20;
                            Log.d("TAG","v="+vx);

                            if(x1>width/2-20)
                            {
                                x1=width/2-20;
                                ux=vx;
                                vx=a=0;

                                if(y2>height*3/4)
                                {
                                    y2=height*3/4;
                                    ux=uy=0;
                                }
                                else if(y2==height*3/4)
                                {
                                    t.cancel();;
                                }
                            }
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}