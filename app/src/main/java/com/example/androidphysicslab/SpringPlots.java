package com.example.androidphysicslab;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SpringPlots extends AppCompatActivity
{
    double[] xList,vList,aList;
    DataPoint[] vPlot,xPlot,aPlot;
    TextView deltaXTime, velocitySpringTime,aTime;
    GraphView velocitySpringGraph,deltaXGraph,aSpringGraph;
    Button backSpringButton;
    double g,m,k,amplitude,periods;
    AlertDialog.Builder adb;

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
    }

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
        deltaXTime.setText(Languages.xTime);
        velocitySpringTime.setText(Languages.velocityTime);
        aTime.setText(Languages.aTime);
        backSpringButton.setText(Languages.back);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}
