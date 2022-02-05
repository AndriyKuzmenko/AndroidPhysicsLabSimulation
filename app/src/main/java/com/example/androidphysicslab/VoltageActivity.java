package com.example.androidphysicslab;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class VoltageActivity extends AppCompatActivity
{
    VoltageView voltageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        voltageView=new VoltageView(this);
        setContentView(voltageView);
    }
}

class VoltageView extends SurfaceView
{
    SurfaceHolder surfaceHolder;
    Paint paint;
    String name;

    public VoltageView(Context context)
    {
        super(context);
        surfaceHolder=getHolder();
        paint=new Paint();
        name="Spring "+ SystemClock.uptimeMillis();
    }
}