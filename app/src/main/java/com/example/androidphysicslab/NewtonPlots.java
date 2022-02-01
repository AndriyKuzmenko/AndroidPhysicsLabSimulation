package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class NewtonPlots extends AppCompatActivity
{
    double[] xList,vList;
    DataPoint[] vPlot,xPlot;
    TextView deltaXTimeNewton,velocityNewtonTime;
    GraphView deltaXGraphNewton,velocityNewtonGraph;
    Button backNewtonButton;
    double g,m1,m2,mu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_plots);

        deltaXTimeNewton=(TextView)findViewById(R.id.deltaXTimeNewton);
        velocityNewtonTime=(TextView)findViewById(R.id.velocityNewtonTime);
        deltaXGraphNewton=(GraphView)findViewById(R.id.deltaXGraphNewton);
        velocityNewtonGraph=(GraphView)findViewById(R.id.velocityNewtonGraph);
        backNewtonButton=(Button)findViewById(R.id.backNewtonButton);

        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        g=gi.getDoubleExtra("g",0);
        m1=gi.getDoubleExtra("m1",0);
        m2=gi.getDoubleExtra("m2",0);
        mu=gi.getDoubleExtra("mu",0);

        vPlot=new DataPoint[vList.length];
        xPlot=new DataPoint[vList.length];

        changeLanguage();

        for(int i=0;i<vList.length;i++)
        {
            vPlot[i]=new DataPoint((double)i/100,vList[i]);
            xPlot[i]=new DataPoint((double)i/100,xList[i]);
        }

        try
        {
            LineGraphSeries<DataPoint> vSeries = new LineGraphSeries< >(vPlot);
            velocityNewtonGraph.addSeries(vSeries);

            LineGraphSeries<DataPoint> xSeries = new LineGraphSeries< >(xPlot);
            deltaXGraphNewton.addSeries(xSeries);
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(NewtonPlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if(id==R.id.English)
        {
            Languages.toEnglish();
        }
        else if(id==R.id.Hebrew)
        {
            Languages.toHebrew();
        }

        changeLanguage();

        return true;
    }

    public void changeLanguage()
    {
        deltaXTimeNewton.setText(Languages.positionTime);
        velocityNewtonTime.setText(Languages.velocityTime);
        backNewtonButton.setText(Languages.back);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void back(View view)
    {
        Intent si=new Intent(this,NewtonResults.class);
        si.putExtra("xList",xList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m1",m1);
        si.putExtra("m2",m2);
        si.putExtra("mu",mu);
        startActivity(si);
    }
}