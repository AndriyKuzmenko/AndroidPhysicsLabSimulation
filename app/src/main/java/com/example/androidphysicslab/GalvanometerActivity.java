package com.example.androidphysicslab;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class GalvanometerActivity extends AppCompatActivity
{
    GalvanometerView galvanometerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        galvanometerView=new GalvanometerView(this);
        setContentView(galvanometerView);
    }
}

class GalvanometerView extends SurfaceView
{

    public GalvanometerView(Context context)
    {
        super(context);
    }
}