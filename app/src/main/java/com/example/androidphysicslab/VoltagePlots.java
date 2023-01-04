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

public class VoltagePlots extends AppCompatActivity
{
    TextView vAsI;
    GraphView vIGraph;
    Button backVoltageButton;
    AdView adView;

    double[] rList,iList,vList;
    double epsilon,internalR,maxR;
    DataPoint[] vPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voltage_plots);

        vAsI=(TextView)findViewById(R.id.vAsI);
        vIGraph=(GraphView)findViewById(R.id.vIGraph);
        backVoltageButton=(Button)findViewById(R.id.backVoltageButton);
        adView=(AdView)findViewById(R.id.adView);

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        vList=gi.getDoubleArrayExtra("vList");
        epsilon=gi.getDoubleExtra("epsilon",0);
        internalR=gi.getDoubleExtra("internalR",0);
        maxR=gi.getDoubleExtra("maxR",0);
        vPlot=new DataPoint[10];

        changeLanguage();

        for(int i=0;i<10;i++)
        {
            vPlot[i]=new DataPoint(iList[i],vList[i]);
            Log.d("TAG",iList[i]+" "+vList[i]+" "+(vPlot[i]==null));
        }

        try
        {
            LineGraphSeries<DataPoint> vSeries = new LineGraphSeries< >(vPlot);
            vIGraph.addSeries(vSeries);
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(VoltagePlots.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

        vIGraph.getViewport().setScalable(true);
        vIGraph.getViewport().setScrollable(true);
        vIGraph.getViewport().setScalableY(true);
        vIGraph.getViewport().setScrollableY(true);
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the results screen
     */

    public void back(View view)
    {
        Intent si=new Intent(this,VoltageResults.class);

        si.putExtra("rList",rList);
        si.putExtra("vList",vList);
        si.putExtra("iList",iList);
        si.putExtra("epsilon",epsilon);
        si.putExtra("internalR",internalR);
        si.putExtra("maxR",maxR);

        startActivity(si);
    }

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        backVoltageButton.setText(Languages.back);
        vAsI.setText(Languages.iVPlot);
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
     * @return - finishes the activity
     */

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}