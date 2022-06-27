package com.example.androidphysicslab;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FreeFallActivity extends AppCompatActivity
{
    double mass,height,gravity,accelaration;
    boolean rerun;
    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi = getIntent();
        mass=gi.getDoubleExtra("mass",0); //will be used in the table to calculate energy
        height=gi.getDoubleExtra("height",0);
        int planet=gi.getIntExtra("planet",-2);
        if(planet>=0) gravity=Languages.gravity[planet];
        else gravity=10;
        double meter=0.8*(double)Resources.getSystem().getDisplayMetrics().heightPixels/(height*1.3);
        accelaration=gravity*meter;
        rerun=gi.getBooleanExtra("rerun",false);
        Log.w("TAG","a="+accelaration+" meter="+meter+" g="+gravity+" h="+height);

        super.onCreate(savedInstanceState);
        drawingView=new DrawingView(this,accelaration,height*meter,meter);
        drawingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(drawingView);
        layout.addView(adView);

        setContentView(layout);

        Toast.makeText(FreeFallActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();

        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
        if(drawingView.vList.contains(-1.0))
        {
            drawingView.vList.remove(-1.0);
            Intent si = new Intent(this, FreeFallResults.class);
            Log.w("TAG", "" + drawingView.hList.size() + " " + drawingView.vList.size());
            double[] hList = new double[drawingView.hList.size()];
            double[] vList = new double[drawingView.vList.size()];

            for (int i = 0; i < hList.length; i++)
            {
                hList[i] = drawingView.hList.get(i);
                vList[i] = drawingView.vList.get(i);
            }

            if(!(rerun||FBRef.mUser==null))
            {
                saveResults(new FreeFallObject(drawingView.hList, drawingView.vList, drawingView.name, gravity, mass));
            }

            si.putExtra("hList",hList);
            si.putExtra("vList",vList);
            si.putExtra("g",gravity);
            si.putExtra("m",mass);
            startActivity(si);
        }

        return true;
    }

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

    public void saveResults(FreeFallObject results)
    {
        FBRef.myRef.child("Free Fall").child(results.getName()).setValue(results);
        Log.d("saved","results");
    }
}

class DrawingView extends SurfaceView
{

    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    double x,y,vx,vy,h,meter;
    double accelaration;
    Canvas canvas;
    boolean started;
    public ArrayList<Double> hList;
    public ArrayList<Double> vList;
    public String name;

    /**
     *
     * @param context - the activity
     * @param accelaration - the free fall acceleration in pixels/sec^2
     * @param h - the height in pixels
     * @param meter - the amount of pixels that represent one meter
     */

    public DrawingView(Context context, double accelaration, double h, double meter)
    {
        super(context);
        surfaceHolder = getHolder();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        x=y=0;
        vx=vy=10;
        this.accelaration=accelaration;
        this.h=h;
        this.meter=meter;
        started=false;
        hList=new ArrayList<>();
        vList=new ArrayList<>();
        name="Free Fall "+SystemClock.uptimeMillis();
        Log.d("TAG",name);
    }

    /**
     * @param event - the screen event that was triggered.
     * @return - if the screen was pressed, and the animation hasn't started yet, starts the animation. Does all the calculations for the drawing routines and saves the results in ArrayLists for Firebase.
     */

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (surfaceHolder.getSurface().isValid() && !started)
            {
                //position and velocity in pixels
                x=getWidth()/2-25;
                y=30;
                vy=0;
                started=true;

                Timer t=new Timer();
                t.scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        if(y>=h+30)
                        {
                            y=h+30;
                            vList.add(-1.0);
                            Log.i("TAG",vList.size()+"");
                            t.cancel();
                        }

                        //Height and velocity in meters
                        Log.w("TAG","y="+y+"  h="+(h-y+30)/meter+"  vy"+vy/meter);
                        hList.add((h-y+30)/meter);
                        vList.add(vy/meter);

                        //Drawing
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        paint.setColor(Color.RED);
                        canvas.drawCircle((int)x, (int)y, 30, paint);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(new Rect(0,(int)h+61,Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels),paint);
                        surfaceHolder.unlockCanvasAndPost(canvas);

                        //Updating values for next iteration
                        y+=vy/100;
                        vy+=accelaration/100;
                    }
                }, 5, 5);
            }
            else if(surfaceHolder.getSurface().isValid())
            {

            }
        }
        return false;
    }
}