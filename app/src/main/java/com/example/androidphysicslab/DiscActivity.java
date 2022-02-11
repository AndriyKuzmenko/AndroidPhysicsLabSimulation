package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Languages.results);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(discView.vList.contains(-1.0))
        {
            discView.lList.remove(-1.1);
            Intent si=new Intent(this,FreeFallResults.class);
            double[] lList=new double[discView.lList.size()];
            double[] vList=new double[discView.vList.size()];

            for(int i=0; i<lList.length; i++)
            {
                lList[i]=discView.lList.get(i);
                vList[i]=discView.vList.get(i);
            }

            //saveResults(new FreeFallObject(drawingView.hList,drawingView.vList,drawingView.name,gravity,mass));

            si.putExtra("hList",lList);
            si.putExtra("vList",vList);
            si.putExtra("g",g);
            si.putExtra("m",m);
            //startActivity(si);
        }

        return true;
    }
}

class DiscView extends SurfaceView
{
    double shift,v0,a,pixelsPerMeter,v,l,deltax;
    int rulerPosition,halfWidth,rulerX,rulerY;
    boolean started,hit;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    ArrayList<Double> lList,vList;

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
        v=v0;

        started=false;
        hit=false;
        rulerPosition=Resources.getSystem().getDisplayMetrics().heightPixels/2;
        halfWidth=Resources.getSystem().getDisplayMetrics().widthPixels/2;

        lList=new ArrayList<>();
        vList=new ArrayList<>();
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
                        paint.setColor(Color.rgb(0,0,33));

                        if(!hit)
                        {
                            rulerY=(int)(deltax*pixelsPerMeter);
                            rulerX=(int)Math.sqrt(2*halfWidth*halfWidth-rulerY*rulerY);
                        }
                        canvas.drawLine(halfWidth/4,rulerPosition,halfWidth/4+rulerX,rulerPosition+rulerY,paint);

                        paint.setColor(Color.RED);
                        canvas.drawCircle(halfWidth,rulerPosition-(int)(l*pixelsPerMeter)-40,30, paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);

                        if(!hit)
                        {
                            deltax-=shift/5;
                            if (deltax<0) hit=true;
                        }
                        else
                        {
                            lList.add(l);
                            vList.add(v);

                            l+=v*0.01;
                            v-=a*0.01;

                            if(v<0)
                            {
                                lList.add(-1.1);
                                t.cancel();
                            }
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}