package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

public class GalvanometerActivity extends AppCompatActivity
{
    GalvanometerView galvanometerView;
    double epsilon,maxR,a,hEarthMagneticField;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        epsilon=gi.getDoubleExtra("epsilon",0);
        maxR=gi.getDoubleExtra("maxR",0);
        a=gi.getDoubleExtra("a",0);
        n=gi.getIntExtra("n",0);
        hEarthMagneticField=50*Math.pow(10,-6)*Math.cos(Math.toRadians(31.253));

        super.onCreate(savedInstanceState);
        galvanometerView=new GalvanometerView(this,epsilon,maxR,n,hEarthMagneticField,a);
        setContentView(galvanometerView);
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
        if (galvanometerView.rList.size()==10)
        {
            GalvanometerObject results=new GalvanometerObject(epsilon,a,n,hEarthMagneticField,galvanometerView.iList,galvanometerView.rList,galvanometerView.tgList,galvanometerView.thetaList);
            results.setName(galvanometerView.name);
            saveResults(results);

            Intent si=new Intent(this,GalvanometerResults.class);
            double[] rList=new double[results.getRList().size()];
            double[] iList=new double[results.getIList().size()];
            double[] thetaList=new double[results.getThetaList().size()];
            double[] tgList=new double[results.getTgList().size()];

            for(int i=0; i<rList.length; i++)
            {
                rList[i]=results.getRList().get(i);
                iList[i]=results.getIList().get(i);
                thetaList[i]=results.getThetaList().get(i);
                tgList[i]=results.getTgList().get(i);
            }

            si.putExtra("rList",rList);
            si.putExtra("iList",iList);
            si.putExtra("thetaList",thetaList);
            si.putExtra("tgList",tgList);
            si.putExtra("epsilon",epsilon);
            si.putExtra("a",a);
            si.putExtra("n",n);
            si.putExtra("hEarthMagneticField",hEarthMagneticField);
            startActivity(si);
        }
        return true;
    }

    public void saveResults(GalvanometerObject results)
    {
        Log.d("TAG",results.getName());
        FBRef.myRef.child("Galvanometer").child(results.getName()).setValue(results);
    }
}

class GalvanometerView extends SurfaceView
{
    boolean started;
    Paint paint;
    SurfaceHolder surfaceHolder;
    int width,height,counter;
    Canvas canvas;
    double epsilon,maxR,n,hEarthMagneticField,a;
    ArrayList<Double> rList,iList,tgList,thetaList;
    String name;

    public GalvanometerView(Context context,double epsilon,double maxR,int n,double hEarthMagneticField,double a)
    {
        super(context);
        started=false;
        paint=new Paint();
        surfaceHolder=getHolder();
        width= Resources.getSystem().getDisplayMetrics().widthPixels;
        height=Resources.getSystem().getDisplayMetrics().heightPixels;
        counter=10;
        this.hEarthMagneticField=hEarthMagneticField;

        this.epsilon=epsilon;
        this.maxR=maxR;
        this.n=n;
        this.a=a;
        rList=new ArrayList<>();
        iList=new ArrayList<>();
        tgList=new ArrayList<>();
        thetaList=new ArrayList<>();

        name="Tangent Galvanometer "+SystemClock.uptimeMillis();
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
                    final double mu0=4*Math.PI*Math.pow(10,-7);

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

                        double r=maxR*counter/10;
                        double i=epsilon/(r);
                        double tg=mu0*n*i/(2*a*hEarthMagneticField);
                        double theta=Math.toDegrees(Math.atan(tg));

                        rList.add(r);
                        iList.add(i);
                        tgList.add(tg);
                        thetaList.add(theta);

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