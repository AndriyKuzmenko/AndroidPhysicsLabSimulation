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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

public class NewtonPlots extends AppCompatActivity
{
    double[] xList,vList;
    DataPoint[] vPlot,xPlot;
    TextView deltaXTimeNewton,velocityNewtonTime;
    GraphView deltaXGraphNewton,velocityNewtonGraph;
    Button backNewtonButton;
    AdView adView;

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
        adView=(AdView)findViewById(R.id.adView);

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

        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
        deltaXTimeNewton.setText(Languages.positionTime);
        velocityNewtonTime.setText(Languages.velocityTime);
        backNewtonButton.setText(Languages.back);
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