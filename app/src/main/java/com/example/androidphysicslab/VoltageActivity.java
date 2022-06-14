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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VoltageActivity extends AppCompatActivity
{
    VoltageView voltageView;
    double epsilon,internalR,maxR;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        epsilon=gi.getDoubleExtra("epsilon",epsilon);
        internalR=gi.getDoubleExtra("internalR",internalR);
        maxR=gi.getDoubleExtra("maxR",maxR);

        super.onCreate(savedInstanceState);
        voltageView=new VoltageView(this,epsilon,internalR,maxR);
        setContentView(voltageView);

        Toast.makeText(VoltageActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param menu - the menu
     * @return dispays an OptionsMenu with the option to go to the results screen
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Languages.results);

        return true;
    }

    /**
     * @param item - the selected item from the menu
     * @return - If the animation is over, sends the user to the results screen and if necessary saves the results to firebase.
     */

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (voltageView.rList.size()==10)
        {
            VoltageObject results=new VoltageObject(voltageView.rList,voltageView.iList,voltageView.vList,epsilon,internalR,maxR);
            results.setName(voltageView.name);

            if(FBRef.mUser!=null)
                saveResults(results);

            Intent si=new Intent(this,VoltageResults.class);
            double[] rList=new double[results.getRList().size()];
            double[] vList=new double[results.getVList().size()];
            double[] iList=new double[results.getIList().size()];

            for(int i=0; i<rList.length; i++)
            {
                rList[i]=results.getRList().get(i);
                vList[i]=results.getVList().get(i);
                iList[i]=results.getIList().get(i);
            }

            si.putExtra("rList",rList);
            si.putExtra("vList",vList);
            si.putExtra("iList",iList);
            si.putExtra("epsilon",epsilon);
            si.putExtra("internalR",internalR);
            si.putExtra("maxR",maxR);
            startActivity(si);
        }
        return true;
    }

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

    public void saveResults(VoltageObject results)
    {
        Log.d("TAG",results.getName());
        FBRef.myRef.child("Voltage").child(results.getName()).setValue(results);
    }

    /**
     * @return - finishes the activity
     */

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}

class VoltageView extends SurfaceView
{
    SurfaceHolder surfaceHolder;
    Paint paint;
    String name;
    int width,height;
    boolean started;
    Canvas canvas;
    int counter=10;
    double epsilon,internalR,maxR;
    ArrayList<Double> rList,iList,vList;

    /**
     *
     * @param context - the activity
     * @param epsilon - the electromotive force of the battery
     * @param internalR - the internal resistance of the battery
     * @param maxR - the maximum resistance of the battery
     */

    public VoltageView(Context context,double epsilon,double internalR,double maxR)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();
        name="Voltage "+ SystemClock.uptimeMillis();
        width= Resources.getSystem().getDisplayMetrics().widthPixels;
        height=Resources.getSystem().getDisplayMetrics().heightPixels;
        started=false;

        this.epsilon=epsilon;
        this.internalR=internalR;
        this.maxR=maxR;
        rList=new ArrayList<>();
        iList=new ArrayList<>();
        vList=new ArrayList<>();
    }

    /**
     * @param event - the screen event that was triggered.
     * @return - if the screen was pressed, and the animation hasn't started yet, starts the animation. Does all the calculations for the drawing routines and saves the results in ArrayLists for Firebase.
     */

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

                        paint.setColor(Color.WHITE);
                        paint.setTextSize(100);
                        canvas.drawText("A",width/5-36,height/2+36,paint);
                        canvas.drawText("V",width/2-36,height/5-114,paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);

                        double r=maxR*counter/10;
                        double i=epsilon/(r+internalR);
                        double v=i*r;
                        Log.d("TAG","R="+r+"  I="+i+"  V="+v);

                        rList.add(r);
                        iList.add(i);
                        vList.add(v);

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