package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
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

public class GalvanometerPlots extends AppCompatActivity
{
    GraphView tgIGraph;
    double[] rList,iList,thetaList,tgList;
    double hEarthMagneticField,epsilon,a;
    int n;
    DataPoint[] tgPlot;
    TextView tgITV;
    Button backGalvanometerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_plots);

        tgIGraph=(GraphView)findViewById(R.id.tgIGraph);
        tgITV=(TextView)findViewById(R.id.tgITV);
        backGalvanometerButton=(Button)findViewById(R.id.backGalvanometerButton);

        changeLanguage();

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        thetaList=gi.getDoubleArrayExtra("thetaList");
        tgList=gi.getDoubleArrayExtra("tgList");
        hEarthMagneticField=gi.getDoubleExtra("hEarthMagneticField",0);
        epsilon=gi.getDoubleExtra("epsilon",0);
        a=gi.getDoubleExtra("a",0);
        n=gi.getIntExtra("n",0);

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
     * @param view - the button pressed
     * @return - goes back to the results screen
     */

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
        tgITV.setText(Languages.tgI);
        backGalvanometerButton.setText(Languages.back);
    }
}