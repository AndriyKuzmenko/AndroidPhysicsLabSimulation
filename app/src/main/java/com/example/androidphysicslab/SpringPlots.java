package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class SpringPlots extends AppCompatActivity
{
    double[] xList,vList,aList;
    DataPoint[] vPlot,xPlot,aPlot;
    TextView deltaXTime, velocitySpringTime,aTime;
    GraphView velocitySpringGraph,deltaXGraph,aSpringGraph;
    Button backSpringButton;
    double g,m,k;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_plots);

        deltaXTime=(TextView)findViewById(R.id.deltaXTime);
        velocitySpringTime=(TextView)findViewById(R.id.velocitySpringTime);
        aTime=(TextView)findViewById(R.id.aTime);
        backSpringButton=(Button)findViewById(R.id.backSpringButton);
        velocitySpringGraph=(GraphView)findViewById(R.id.velocitySpringGraph);
        deltaXGraph=(GraphView)findViewById(R.id.deltaXGraph);
        aSpringGraph=(GraphView)findViewById(R.id.aSpringGraph);


        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        aList=gi.getDoubleArrayExtra("aList");
        g=gi.getDoubleExtra("g",0);
        m=gi.getDoubleExtra("m",0);

        vPlot=new DataPoint[vList.length];
        xPlot=new DataPoint[vList.length];
        aPlot=new DataPoint[vList.length];

        //changeLanguage();

        for(int i=0;i<vList.length;i++)
        {
            vPlot[i]=new DataPoint((double)i/100,vList[i]);
            xPlot[i]=new DataPoint((double)i/100,xList[i]);
            aPlot[i]=new DataPoint((double)i/100,aList[i]);
        }

        try
        {
            LineGraphSeries<DataPoint> vSeries = new LineGraphSeries< >(vPlot);
            velocitySpringGraph.addSeries(vSeries);

            Log.w("TAG","vPlot done");

            LineGraphSeries<DataPoint> xSeries = new LineGraphSeries< >(xPlot);
            deltaXGraph.addSeries(xSeries);

            Log.w("TAG","hPlot done");

            LineGraphSeries<DataPoint> aSeries = new LineGraphSeries< >(aPlot);
            aSpringGraph.addSeries(aSeries);

            Log.w("TAG","hPlot done");
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(SpringPlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
