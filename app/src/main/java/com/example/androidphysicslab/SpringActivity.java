package com.example.androidphysicslab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpringActivity extends AppCompatActivity
{
    SpringView springView;
    double m,g,k,amplit,periods,pixelsPerMeter;
    boolean rerun;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("mass",0);
        int planet=gi.getIntExtra("planet",-2);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];
        k=gi.getDoubleExtra("k",0);
        amplit=gi.getDoubleExtra("amplitude",0);
        periods=gi.getDoubleExtra("periods",0);
        rerun=gi.getBooleanExtra("rerun",false);

        double height=4.125*m*g/k;
        pixelsPerMeter=(double)Resources.getSystem().getDisplayMetrics().heightPixels/(height);

        Log.d("values","m="+m+"  k="+k+"  g="+g+"     ampl="+amplit+"     "+periods+" periods");
        Log.d("values","h="+height+"   meter="+pixelsPerMeter);

        super.onCreate(savedInstanceState);
        springView=new SpringView(this,m,g,k,amplit,periods,pixelsPerMeter);
        springView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(springView);
        layout.addView(adView);

        setContentView(layout);

        Toast.makeText(SpringActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();

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
        if (springView.xList.contains(-1.1))
        {
            Intent si = new Intent(this, SpringResults.class);
            Log.i("TAG","here");
            springView.xList.remove(-1.1);

            SpringObject results = new SpringObject(springView.xList, springView.vList, springView.aList, springView.m, springView.g, springView.k,amplit,periods);

            if(!(rerun||FBRef.mUser==null))
            {
                final String[] name = {springView.name};
                adb = new AlertDialog.Builder(this);
                adb.setCancelable(false);
                adb.setTitle("Please give a title for your results. Will override existing results in case of name collision");
                final EditText eT = new EditText(this);
                eT.setHint(name[0]);
                adb.setView(eT);
                adb.setPositiveButton("Confirm title", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (!eT.getText().toString().equals(""))
                        {
                            name[0] = eT.getText().toString();
                        }

                        results.setName(name[0]);
                        saveResults(results);

                        double[] xList = new double[results.getXList().size()];
                        double[] vList = new double[results.getVList().size()];
                        double[] aList = new double[results.getAList().size()];

                        for (int i = 0; i < xList.length; i++)
                        {
                            xList[i] = results.getXList().get(i);
                            vList[i] = results.getVList().get(i);
                            aList[i] = results.getAList().get(i);
                        }

                        si.putExtra("xList", xList);
                        si.putExtra("vList", vList);
                        si.putExtra("aList", aList);
                        si.putExtra("g", g);
                        si.putExtra("m", m);
                        si.putExtra("k", k);
                        si.putExtra("amplitude", amplit);
                        si.putExtra("periods", periods);
                        startActivity(si);
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
            else
            {
                double[] xList = new double[results.getXList().size()];
                double[] vList = new double[results.getVList().size()];
                double[] aList = new double[results.getAList().size()];

                for (int i = 0; i < xList.length; i++)
                {
                    xList[i] = results.getXList().get(i);
                    vList[i] = results.getVList().get(i);
                    aList[i] = results.getAList().get(i);
                }

                si.putExtra("xList", xList);
                si.putExtra("vList", vList);
                si.putExtra("aList", aList);
                si.putExtra("g", g);
                si.putExtra("m", m);
                si.putExtra("k", k);
                si.putExtra("amplitude", amplit);
                si.putExtra("periods", periods);
                startActivity(si);
            }
        }
        return true;
    }

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

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
    double drawingPosition,deltaX,a,v,periods;
    int middle,left,right;
    boolean started;
    int counter,time;
    public ArrayList<Double> xList,vList,aList;
    public String name;

    /**
     *
     * @param context - the activity
     * @param m - the mass of the body
     * @param g - the free fall acceleration
     * @param k - the spring constant
     * @param amplit - the amplitude of the spring
     * @param periods - the number of periods the animation should run
     * @param pixelsPerMeter - the amount of pixels that represent one meter
     */

    public SpringView(Context context, double m, double g, double k, double amplit, double periods, double pixelsPerMeter)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();
        name="Spring "+ SystemClock.uptimeMillis();

        this.m=m;
        this.g=g;
        this.k=k;
        this.periods=periods;
        this.pixelsPerMeter=pixelsPerMeter;

        ceiling=new Rect(0,0,Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels/20);
        middle=Resources.getSystem().getDisplayMetrics().widthPixels/2;
        left=(int)(middle*0.8);
        right=(int)(middle*1.2);
        started=false;
        v=0;
        deltaX=amplit;
        drawingPosition=deltaX*pixelsPerMeter+ceiling.bottom+100;
        a=g-(k*deltaX)/m;
        counter=0;
        time=(int)(periods*200*Math.PI*Math.sqrt(m/k));

        xList=new ArrayList<>();
        vList=new ArrayList<>();
        aList=new ArrayList<>();
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

                        int difference=(int)(drawingPosition-ceiling.bottom)/16;
                        int x1=middle;
                        int x2=right;
                        int y1=ceiling.bottom;
                        int y2=ceiling.bottom+difference;
                        paint.setStrokeWidth(5);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(ceiling,paint);

                        paint.setColor(Color.rgb(0,0,33));
                        for(int i=0;i<16;i++)
                        {
                            canvas.drawLine(x1,y1,x2,y2,paint);
                            y1+=difference;
                            y2+=difference;
                            if(i==14)
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

                        counter++;
                        if(counter>=time)
                        {
                            t.cancel();
                            xList.add(-1.1);
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}