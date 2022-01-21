package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SpringResults extends AppCompatActivity
{
    TextView mgkView;
    ListView resultsSpringLV;
    Button plotsSpringButton,menuSpringButton;
    double[] xList,vList,aList;
    double m,g,k;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_results);

        mgkView=(TextView)findViewById(R.id.mgkView);
        resultsSpringLV=(ListView) findViewById(R.id.resultsSpringLV);
        plotsSpringButton=(Button)findViewById(R.id.plotsSpringButton);
        menuSpringButton=(Button)findViewById(R.id.menuSpringButton);

        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        aList=gi.getDoubleArrayExtra("aList");
        m=gi.getDoubleExtra("m",0);
        g=gi.getDoubleExtra("g",0);
        k=gi.getDoubleExtra("k",0);
        mgkView.setText("m="+m+" kg     "+"g="+g+" m/(sec^2)\nk="+k+" N/m");

        String[] list=new String[xList.length+1];
        list[0]="t(sec)    x(m)    v(m/sec)  a(m/sec^2)";

        for(int i=1; i<xList.length+1; i++)
        {
            String t=" ";
            String time=String.valueOf((double)(i-1)/100);
            int dot=time.indexOf('.');
            if(time.length()>dot+digitsAfterDot+1)
            {
                time=time.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(time.length()<=dot+digitsAfterDot+1)
                {
                    time+=" ";
                }
                if(i%10==0)time+="  ";
            }
            t+=time+"      ";

            String x=String.valueOf(xList[i-1]);
            dot=x.indexOf('.');
            if(x.length()>dot+digitsAfterDot+1)
            {
                x=x.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(x.length()<=dot+digitsAfterDot+1)
                {
                    x+=" ";
                }
            }
            t+=x+"      ";

            String v=String.valueOf(vList[i-1]);
            dot=v.indexOf('.');
            if(v.length()>dot+digitsAfterDot+1)
            {
                v=v.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(v.length()<=dot+digitsAfterDot+1)
                {
                    v+=" ";
                }
            }
            t+=v+"        ";

            String a=String.valueOf(aList[i-1]);
            dot=a.indexOf('.');
            if(a.length()>dot+digitsAfterDot+1)
            {
                a=a.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(a.length()<=dot+digitsAfterDot+1)
                {
                    a+=" ";
                }
            }
            Log.w("a=",a);
            t+=a;
            Log.w("t=",t);

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsSpringLV.setAdapter(adp);
    }
}