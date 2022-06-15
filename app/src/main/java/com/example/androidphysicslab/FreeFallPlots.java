package com.example.androidphysicslab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FreeFallPlots extends AppCompatActivity
{
    double[] hList,vList;
    DataPoint[] vPlot,hPlot;
    TextView heightTime, velocityTime;
    Button backButton;
    double g,m;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall_plots);

        GraphView heightPlot=(GraphView)findViewById(R.id.heightPlot);
        GraphView velocityPlot=(GraphView)findViewById(R.id.velocityPlot);
        heightTime=(TextView)findViewById(R.id.heightTime);
        velocityTime=(TextView)findViewById(R.id.velocityTime);
        backButton=(Button)findViewById(R.id.backButton);

        Intent gi=getIntent();
        hList=gi.getDoubleArrayExtra("hList");
        vList=gi.getDoubleArrayExtra("vList");
        g=gi.getDoubleExtra("g",0);
        m=gi.getDoubleExtra("m",0);

        vPlot=new DataPoint[vList.length];
        hPlot=new DataPoint[vList.length];

        changeLanguage();

        for(int i=0;i<vList.length;i++)
        {
            vPlot[i]=new DataPoint((double)i/100,vList[i]);
        }

        for(int i=0;i<vList.length;i++)
        {
            hPlot[i]=new DataPoint((double)i/100,hList[i]);
        }

        try
        {
            LineGraphSeries<DataPoint> vSeries = new LineGraphSeries< >(vPlot);
            velocityPlot.addSeries(vSeries);

            Log.w("TAG","vPlot done");

            LineGraphSeries<DataPoint> hSeries = new LineGraphSeries< >(hPlot);
            heightPlot.addSeries(hSeries);

            Log.w("TAG","hPlot done");
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(FreeFallPlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the results screen
     */

    public void back(View view)
    {
        Intent si=new Intent(this,FreeFallResults.class);

        si.putExtra("hList",hList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m",m);

        startActivity(si);
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

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        heightTime.setText(Languages.heightTime);
        velocityTime.setText(Languages.velocityTime);
        backButton.setText(Languages.back);
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
}
