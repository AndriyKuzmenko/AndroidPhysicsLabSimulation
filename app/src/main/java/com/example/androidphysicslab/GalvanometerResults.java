package com.example.androidphysicslab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GalvanometerResults extends AppCompatActivity
{
    TextView dataTV;
    ListView resultsLV;
    Button plotsButton,menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_results);

        dataTV=(TextView)findViewById(R.id.dataTV);
        resultsLV=(ListView)findViewById(R.id.resultsLV);
        plotsButton=(Button)findViewById(R.id.plotsButton);
        menuButton=(Button)findViewById(R.id.menuButton);
    }
}