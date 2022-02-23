package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GalvanometerPlots extends AppCompatActivity
{
    GraphView tgIGraph;
    double[] rList,iList,thetaList,tgList;
    double hEarthMagneticField,epsilon,a,n;
    DataPoint[] tgPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_plots);

        tgIGraph=(GraphView)findViewById(R.id.tgIGraph);

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        thetaList=gi.getDoubleArrayExtra("thetaList");
        tgList=gi.getDoubleArrayExtra("tgList");
        hEarthMagneticField=gi.getDoubleExtra("hEarthMagneticField",0);
        epsilon=gi.getDoubleExtra("epsilon",0);
        a=gi.getDoubleExtra("a",0);
        n=gi.getDoubleExtra("n",0);

        tgPlot=new DataPoint[10];

        for(int i=0;i<10;i++)
        {
            tgPlot[i]=new DataPoint(iList[i],tgList[i]);
        }

        try
        {
            LineGraphSeries<DataPoint> tgSeries = new LineGraphSeries< >(tgPlot);
            tgIGraph.addSeries(tgSeries);
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(GalvanometerPlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void back(View view)
    {
        Intent si=new Intent(this,GalvanometerResults.class);
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
}