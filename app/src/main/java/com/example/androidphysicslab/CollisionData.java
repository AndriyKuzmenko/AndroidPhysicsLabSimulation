package com.example.androidphysicslab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CollisionData extends AppCompatActivity
{
    TextView railHeightLabel,tableHeightLabel;
    EditText railHeightET,tableHeightET;
    Spinner planetCollisionSpinner;
    Button startCollisionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_data);
    }
}