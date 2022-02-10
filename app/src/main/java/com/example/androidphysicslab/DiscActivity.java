package com.example.androidphysicslab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class DiscActivity extends AppCompatActivity
{
    DiscView discView;
    double m,mu,k,shift,g,v0,a,t,l;
    int planet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent gi=getIntent();
        m=gi.getDoubleExtra("m",0);
        mu=gi.getDoubleExtra("mu",0);
        k=gi.getDoubleExtra("k",0);
        shift=gi.getDoubleExtra("shift",0);
        planet=gi.getIntExtra("planet",-2);
        if(planet==-1) g=10;
        else g=Languages.gravity[planet];

        v0=Math.sqrt(k*shift*shift/m);
        a=mu*g;
        t=v0/a;
        l=v0*t-0.5*a*t*t;
        Log.d("TAG","v0="+v0+" a="+a+" t="+t+" l="+l);

        super.onCreate(savedInstanceState);
        discView=new DiscView(this);
        setContentView(discView);
    }
}

class DiscView extends SurfaceView
{
    public DiscView(Context context)
    {
        super(context);
    }
}