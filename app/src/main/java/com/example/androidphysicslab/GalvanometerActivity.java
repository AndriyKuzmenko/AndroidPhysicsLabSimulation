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
        hEarthMagneticField=gi.getDoubleExtra("hEarthMagneticField",0);

        super.onCreate(savedInstanceState);
        galvanometerView=new GalvanometerView(this,epsilon,maxR,n,hEarthMagneticField,a);
        galvanometerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(galvanometerView);
        layout.addView(adView);

        setContentView(layout);

        Toast.makeText(GalvanometerActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();

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
        if (galvanometerView.rList.size()==10)
        {
            GalvanometerObject results=new GalvanometerObject(epsilon,a,n,hEarthMagneticField,galvanometerView.iList,galvanometerView.rList,galvanometerView.tgList,galvanometerView.thetaList);
            results.setName(galvanometerView.name);

            if(FBRef.mUser!=null)
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

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

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

    /**
     *
     * @param context - the activity
     * @param epsilon - the electromotive force of the battery
     * @param maxR - the maximum resistance of the battery
     * @param n - number of windings
     * @param hEarthMagneticField - the magentic field of the earth
     * @param a - the area of the galvanometer
     */

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

                        paint.setStrokeWidth(70);
                        canvas.drawLine(width*2/5,height*7/10,width*3/5,height*7/10,paint);

                        paint.setColor(Color.BLUE);
                        canvas.drawCircle(width/5,height/2,50,paint);
                        canvas.drawCircle(width*4/5,height/3,50,paint);

                        paint.setColor(Color.WHITE);
                        paint.setTextSize(100);
                        canvas.drawText("A",width/5-36,height/2+36,paint);
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