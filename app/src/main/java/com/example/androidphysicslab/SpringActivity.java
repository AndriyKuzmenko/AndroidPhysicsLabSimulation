package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
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

public class SpringActivity extends AppCompatActivity
{
    SpringView springView;
    double m,g,k,pixelsPerMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("mass",0);
        int planet=gi.getIntExtra("planet",-2);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];
        k=gi.getDoubleExtra("k",0);

        double height=3.3*m*g/k;
        pixelsPerMeter=(double)Resources.getSystem().getDisplayMetrics().heightPixels/(height);

        Log.d("values","m="+m+"  k="+k+"  g="+g);
        Log.d("values","h="+height+"   meter="+pixelsPerMeter);

        super.onCreate(savedInstanceState);
        springView=new SpringView(this,m,g,k,pixelsPerMeter);
        setContentView(springView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Languages.results);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (springView.xList.contains(-1.1))
        {
            Log.i("TAG","here");
            springView.xList.remove(-1.1);
            SpringObject results=new SpringObject(springView.xList,springView.vList,springView.aList,springView.m,springView.g,springView.k);
            results.setName(springView.name);
            saveResults(results);

            Intent si=new Intent(this,SpringResults.class);
            double[] xList=new double[results.getXList().size()];
            double[] vList=new double[results.getVList().size()];
            double[] aList=new double[results.getAList().size()];

            for(int i=0; i<xList.length; i++)
            {
                xList[i]=results.getXList().get(i);
                vList[i]=results.getVList().get(i);
                aList[i]=results.getAList().get(i);
            }

            si.putExtra("xList",xList);
            si.putExtra("vList",vList);
            si.putExtra("aList",aList);
            si.putExtra("g",g);
            si.putExtra("m",m);
            si.putExtra("k",k);
            startActivity(si);
        }
        return true;
    }

    public void saveResults(SpringObject results)
    {
        Log.d("TAG",results.getName());
        FBRef.myRef.child("Spring").child(results.getName()).setValue(results);
    }
}

class SpringView extends SurfaceView
{
    private SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    double m,g,k,pixelsPerMeter;
    Rect ceiling;
    double drawingPosition,deltaX,a,v;
    int middle,left,right;
    boolean started,a0;
    int counter;
    public ArrayList<Double> xList,vList,aList;
    public String name;

    public SpringView(Context context, double m, double g, double k, double pixelsPerMeter)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();
        name="Spring "+ SystemClock.uptimeMillis();

        this.m=m;
        this.g=g;
        this.k=k;
        this.pixelsPerMeter=pixelsPerMeter;
        ceiling=new Rect(0,0,Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels/20);
        drawingPosition=ceiling.bottom+100;
        middle=Resources.getSystem().getDisplayMetrics().widthPixels/2;
        left=(int)(middle*0.8);
        right=(int)(middle*1.2);
        started=false;
        deltaX=a=v=0;
        counter=0;
        a0=false;

        xList=new ArrayList<>();
        vList=new ArrayList<>();
        aList=new ArrayList<>();
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
                        xList.add(deltaX);
                        vList.add(v);
                        aList.add(a);

                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        started=true;

                        int difference=(int)(drawingPosition-ceiling.bottom)/14;
                        int x1=middle;
                        int x2=right;
                        int y1=ceiling.bottom;
                        int y2=ceiling.bottom+difference;
                        paint.setStrokeWidth(5);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(ceiling,paint);

                        paint.setColor(Color.rgb(0,0,33));
                        for(int i=0;i<14;i++)
                        {
                            canvas.drawLine(x1,y1,x2,y2,paint);
                            y1+=difference;
                            y2+=difference;
                            if(i==12)
                            {
                                x1=right;
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

                        paint.setColor(Color.RED);
                        canvas.drawRect(new Rect(middle-30,(int)drawingPosition,middle+30,(int)drawingPosition+60),paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);

                        a=g-(k*deltaX)/m;
                        v+=a*0.01;
                        deltaX+=v*0.01;
                        drawingPosition=deltaX*pixelsPerMeter+ceiling.bottom+100;

                        if((int)a==0 && !a0)
                        {
                            counter++;
                            a0=true;
                            Log.d("counter",counter+"");

                            if(counter==10)
                            {
                                t.cancel();
                                xList.add(-1.1);
                            }
                        }
                        else if ((int)a!=0)
                        {
                            a0=false;
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}