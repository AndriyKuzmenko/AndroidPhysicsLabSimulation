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
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class CollisionActivity extends AppCompatActivity
{
    CollisionView collisionView;
    double h1,h2,g;
    int planet;
    boolean tall,rerun;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        h1=gi.getDoubleExtra("h1",0);
        h2=gi.getDoubleExtra("h2",0);
        planet=gi.getIntExtra("planet",0);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];
        tall=gi.getBooleanExtra("tall",false);
        rerun=gi.getBooleanExtra("rerun",false);

        double maxHeight=2*(h1+h2);
        double pixlsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/maxHeight;
        Log.d("pixelspermeter=",""+pixlsPerMeter);
        if(!tall)
        {
            pixlsPerMeter = Math.min(pixlsPerMeter, Resources.getSystem().getDisplayMetrics().widthPixels / maxHeight / 2);
            Log.d("pixelspermeter=", "" + pixlsPerMeter);
        }

        super.onCreate(savedInstanceState);
        collisionView=new CollisionView(this,h1,h2,g,pixlsPerMeter);
        collisionView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(collisionView);
        layout.addView(adView);

        setContentView(layout);

        Toast.makeText(CollisionActivity.this, Languages.clickToStart, Toast.LENGTH_SHORT).show();

        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
        if (collisionView.ended)
        {
            Intent si = new Intent(this, CollisionResults.class);
            if(!(rerun||FBRef.mUser==null))
            {
                final String[] name = {collisionView.name};
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

                        CollisionObject results=new CollisionObject(h1,h2,collisionView.v,collisionView.u,g,tall);
                        results.setName(name[0]);
                        saveResults(results);

                        si.putExtra("h1", h1);
                        si.putExtra("h2", h2);
                        si.putExtra("v", collisionView.v);
                        si.putExtra("u", collisionView.u);
                        si.putExtra("g", g);
                        si.putExtra("tall", tall);
                        startActivity(si);
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
            else
            {
                si.putExtra("h1", h1);
                si.putExtra("h2", h2);
                si.putExtra("v", collisionView.v);
                si.putExtra("u", collisionView.u);
                si.putExtra("g", g);
                si.putExtra("tall", tall);
                startActivity(si);
            }
        }
        return true;
    }

    /**
     * @param results - an object that stores the results of the experiment
     * @return - saves the results to firebase.
     */

    public void saveResults(CollisionObject results)
    {
        Log.d("TAG",results.getName());
        FBRef.myRef.child("Collision").child(results.getName()).setValue(results);
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

class CollisionView extends SurfaceView
{
    double h1,h2,g,pixelsPerMeter,a,alpha,sin,cos,vx,vy,ux,uy,x1,y1,x2,y2,m,v,u;
    boolean started;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Paint paint;
    int width,height;
    String name;
    boolean ended;

    /**
     *
     * @param context - the activity
     * @param h1 - the rail heigh in meters
     * @param h2 - the table height in meters
     * @param g - the gravity acceleration of the Earth (m/sec^2)
     * @param pixelsPerMeter - the amount of pixels that represent one meter in the drawing routine.
     * @return - Sets the inital values of all the variables.
     */

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
        m=(h2)/(width/3/pixelsPerMeter);
        Log.d("TAG","m="+m+" h2="+h2);
        x1=(int)(y1-(height*3/4-h1*pixelsPerMeter)+m*width/3)/(m)+70;

        alpha=Math.atan(m);
        sin=Math.sin(alpha);
        cos=Math.cos(alpha);
        a=g*Math.sin(alpha);
        vx=vy=ux=uy=0;

        x2=width/2;
        y2=height*3/4-(int)(h1*pixelsPerMeter);
        name="Collision "+ SystemClock.uptimeMillis();
        ended=false;
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
                            if(y1>height*3/4-(int)(h1*pixelsPerMeter)-20)
                                v=vx=Math.sqrt(vx*vx+vy*vy);
                            vy=0;
                            y1=height*3/4-(int)(h1*pixelsPerMeter)-20;
                            Log.d("TAG","v="+vx);

                            if(x1>width/2-20)
                            {
                                x1=width/2-20;
                                u=ux=vx;
                                vx=a=0;
                            }
                            else if(y2>=height*3/4)
                            {
                                y2=height*3/4;
                                ux=uy=0;
                                ended=true;
                                Log.d("ENDED","ENDED");
                                t.cancel();;
                            }
                        }
                    }
                },5,5);
            }
        }
        return true;
    }
}