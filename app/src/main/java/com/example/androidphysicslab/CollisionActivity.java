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

public class CollisionActivity extends AppCompatActivity
{
    CollisionView collisionView;
    double h1,h2,g;
    int planet;
    boolean tall,rerun;

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
        setContentView(collisionView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Languages.results);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (collisionView.ended)
        {
            CollisionObject results=new CollisionObject(h1,h2,collisionView.v,collisionView.u,g,tall);
            if(!(rerun||FBRef.mUser==null))
            {
                results.setName(collisionView.name);
                saveResults(results);
            }

            Intent si=new Intent(this,CollisionResults.class);

            si.putExtra("h1",h1);
            si.putExtra("h2",h2);
            si.putExtra("v",results.getV());
            si.putExtra("u",results.getU());
            si.putExtra("g",g);
            si.putExtra("tall",tall);
            startActivity(si);
        }
        return true;
    }

    public void saveResults(CollisionObject results)
    {
        Log.d("TAG",results.getName());
        FBRef.myRef.child("Collision").child(results.getName()).setValue(results);
    }

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