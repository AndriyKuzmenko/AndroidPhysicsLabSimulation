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

public class NewtonActivity extends AppCompatActivity
{
    NewtonView newtonView;
    int planet;
    double m1,m2,g,mu,pixelsPerMeter,a,maxLength;
    final int seconds=5;
    boolean rerun;
    AlertDialog.Builder adb;

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
        rerun=gi.getBooleanExtra("rerun",false);

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
        newtonView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(newtonView);
        layout.addView(adView);

        setContentView(layout);

        Toast.makeText(NewtonActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();

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
     * @param menu  - the menu
     * @return      - shows the main menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Languages.results);

        return true;
    }

    /**
     * @param item - the item that was selected
     * @return     - Changes the language to the selected language
     */

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (newtonView.xList.contains(-1.1))
        {
            Intent si = new Intent(this, NewtonResults.class);
            newtonView.xList.remove(-1.1);
            if (!(rerun || FBRef.mUser == null))
            {
                NewtonObject results = new NewtonObject(m1, m2, g, mu, newtonView.xList, newtonView.vList);

                final String[] name = {newtonView.name};
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

                        double[] xList = new double[newtonView.xList.size()];
                        double[] vList = new double[newtonView.vList.size()];

                        for (int i = 0; i < xList.length; i++)
                        {
                            xList[i] = newtonView.xList.get(i);
                            vList[i] = newtonView.vList.get(i);
                        }
                        si.putExtra("xList", xList);
                        si.putExtra("vList", vList);
                        si.putExtra("g", g);
                        si.putExtra("m1", m1);
                        si.putExtra("m2", m2);
                        si.putExtra("mu", mu);
                        si.putExtra("a", a);
                        startActivity(si);
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
            else
            {
                double[] xList = new double[newtonView.xList.size()];
                double[] vList = new double[newtonView.vList.size()];

                for (int i = 0; i < xList.length; i++)
                {
                    xList[i] = newtonView.xList.get(i);
                    vList[i] = newtonView.vList.get(i);
                }
                si.putExtra("xList", xList);
                si.putExtra("vList", vList);
                si.putExtra("g", g);
                si.putExtra("m1", m1);
                si.putExtra("m2", m2);
                si.putExtra("mu", mu);
                si.putExtra("a", a);
                startActivity(si);
            }
        }
        return true;
    }

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

    public void saveResults(NewtonObject results)
    {
        FBRef.myRef.child("Newton").child(results.getName()).setValue(results);
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
    ArrayList<Double> xList,vList;
    String name;

    /**
     *
     * @param context - the activity
     * @param m1 - the mass of the body on the desk
     * @param m2 - the mass of the body in the air
     * @param mu - the coefficient of friction between the desk and the body on the desk
     * @param g - the free fall acceleration
     * @param a - the acceleration
     * @param pixelsPerMeter - the amount of pixels that represent one meter
     * @param maxLength - the maximum length that the bodies move
     * @param seconds - the amount of time the animation will run
     */

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
        name="Second Newton's Law "+ SystemClock.uptimeMillis();

        paint=new Paint();
        surfaceHolder=getHolder();
        started=false;

        top=2*Resources.getSystem().getDisplayMetrics().heightPixels/5;
        right=3*Resources.getSystem().getDisplayMetrics().widthPixels/4;
        bottom=Resources.getSystem().getDisplayMetrics().heightPixels;
        y=top+50;
        x=right-maxLength*pixelsPerMeter+25;
        xList=new ArrayList<>();
        vList=new ArrayList<>();
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
                        xList.add(xMeter);
                        vList.add(v);
                        v+=a*0.01;
                        xMeter+=v*0.01;
                        x+=v*pixelsPerMeter*0.01;
                        y+=v*pixelsPerMeter*0.01;

                        counter++;
                        if(counter==25)
                        {
                            xList.add(-1.1);
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