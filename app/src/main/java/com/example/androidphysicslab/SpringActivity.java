package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class SpringActivity extends AppCompatActivity
{
    SpringView springView;
    double m,g,k,pixelsPerMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("mass",0);
        int planet=gi.getIntExtra("planet",0);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];
        k=gi.getDoubleExtra("k",0);

        double height=2.3*m*g/k;
        pixelsPerMeter=(double) Resources.getSystem().getDisplayMetrics().heightPixels/(height);

        Log.d("values","m="+m+"  k="+k+"  g="+g);
        Log.d("values","h="+height+"   meter="+pixelsPerMeter);

        super.onCreate(savedInstanceState);
        springView=new SpringView(this);
        setContentView(springView);
    }
}

class SpringView extends SurfaceView
{
    public SpringView(Context context)
    {
        super(context);
    }
}