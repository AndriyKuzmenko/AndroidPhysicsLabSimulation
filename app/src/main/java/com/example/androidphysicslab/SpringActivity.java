package com.example.androidphysicslab;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class SpringActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
}

class SpringView extends SurfaceView
{
    public SpringView(Context context)
    {
        super(context);
    }
}