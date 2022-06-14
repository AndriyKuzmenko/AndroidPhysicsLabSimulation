package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DiscPlots extends AppCompatActivity
{
    double[] lList,vList;
    DataPoint[] vPlot,lPlot;
    double m,mu,g,k,deltax,v0,l0;
    GraphView lGraph,vGraph;
    TextView lTime,vTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_plots);

        Intent gi=getIntent();
        lList=gi.getDoubleArrayExtra("lList");
        vList=gi.getDoubleArrayExtra("vList");
        m=gi.getDoubleExtra("m",0);
        mu=gi.getDoubleExtra("mu",0);
        g=gi.getDoubleExtra("g",0);
        k=gi.getDoubleExtra("k",0);
        deltax=gi.getDoubleExtra("deltax",0);
        v0=gi.getDoubleExtra("v0",0);
        l0=gi.getDoubleExtra("l0",0);

        vGraph=(GraphView)findViewById(R.id.vGraph);
        lGraph=(GraphView)findViewById(R.id.lGraph);
        lTime=(TextView)findViewById(R.id.lTime);
        vTime=(TextView)findViewById(R.id.vTime);

        vPlot=new DataPoint[vList.length];
        lPlot=new DataPoint[vList.length];

        changeLanguage();

        for(int i=0;i<vList.length;i++)
        {
            vPlot[i]=new DataPoint((double)i/100,vList[i]);
        }

        for(int i=0;i<vList.length;i++)
        {
            lPlot[i]=new DataPoint((double)i/100,lList[i]);
        }

        try
        {
            LineGraphSeries<DataPoint> vSeries = new LineGraphSeries< >(vPlot);
            vGraph.addSeries(vSeries);

            Log.w("TAG","vPlot done");

            LineGraphSeries<DataPoint> hSeries = new LineGraphSeries< >(lPlot);
            lGraph.addSeries(hSeries);

            Log.w("TAG","hPlot done");
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(DiscPlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the results screen
     */

    public void back(View view)
    {
        Intent si=new Intent(this,DiscResults.class);
        si.putExtra("lList",lList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("v0",v0);
        si.putExtra("l0",l0);
        si.putExtra("deltax",deltax);
        startActivity(si);
    }

    /**
     * @return - finishes the activity
     */

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        vTime.setText(Languages.velocityTime);
        lTime.setText(Languages.distanceTime);
    }

    /**
     * @param menu  - the menu
     * @return      - shows the main menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    /**
     * @param item - the item that was selected
     * @return     - Changes the language to the selected language
     */

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
}