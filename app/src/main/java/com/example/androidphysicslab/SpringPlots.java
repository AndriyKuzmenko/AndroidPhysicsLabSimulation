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

public class SpringPlots extends AppCompatActivity
{
    double[] xList,vList,aList;
    DataPoint[] vPlot,xPlot,aPlot;
    TextView deltaXTime, velocitySpringTime,aTime;
    GraphView velocitySpringGraph,deltaXGraph,aSpringGraph;
    Button backSpringButton;
    AdView adView;
    double g,m,k,amplitude,periods;

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
        adView=(AdView)findViewById(R.id.adView);

        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        aList=gi.getDoubleArrayExtra("aList");
        g=gi.getDoubleExtra("g",0);
        m=gi.getDoubleExtra("m",0);
        k=gi.getDoubleExtra("k",0);
        amplitude=gi.getDoubleExtra("amplitude",0);
        periods=gi.getDoubleExtra("periods",0);

        vPlot=new DataPoint[vList.length];
        xPlot=new DataPoint[vList.length];
        aPlot=new DataPoint[vList.length];

        changeLanguage();

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

        velocitySpringGraph.getViewport().setScalable(true);
        velocitySpringGraph.getViewport().setScrollable(true);
        velocitySpringGraph.getViewport().setScalableY(true);
        velocitySpringGraph.getViewport().setScrollableY(true);

        deltaXGraph.getViewport().setScalable(true);
        deltaXGraph.getViewport().setScrollable(true);
        deltaXGraph.getViewport().setScalableY(true);
        deltaXGraph.getViewport().setScrollableY(true);

        aSpringGraph.getViewport().setScalable(true);
        aSpringGraph.getViewport().setScrollable(true);
        aSpringGraph.getViewport().setScalableY(true);
        aSpringGraph.getViewport().setScrollableY(true);
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the results screen
     */

    public void back(View view)
    {
        Intent si=new Intent(this,SpringResults.class);

        si.putExtra("aList",aList);
        si.putExtra("vList",vList);
        si.putExtra("xList",xList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("k",k);
        si.putExtra("amplitude",amplitude);
        si.putExtra("periods",periods);
        Log.d("ampl","="+amplitude);
        Log.d("peri","="+periods);
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
        deltaXTime.setText(Languages.xTime);
        velocitySpringTime.setText(Languages.velocityTime);
        aTime.setText(Languages.aTime);
        backSpringButton.setText(Languages.back);
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
