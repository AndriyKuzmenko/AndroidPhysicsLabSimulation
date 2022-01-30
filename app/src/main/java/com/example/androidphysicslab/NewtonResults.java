package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewtonResults extends AppCompatActivity
{
    TextView m1m2gaView;
    ListView resultsNewtonLV;
    Button plotsNewtonButton,menuNewtonButton;

    double[] hList,vList;
    double m,g;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_results);

        m1m2gaView=(TextView)findViewById(R.id.m1m2gaView);
        resultsNewtonLV=(ListView)findViewById(R.id.resultsNewtonLV);
        plotsNewtonButton=(Button)findViewById(R.id.plotsNewtonButton);
        menuNewtonButton=(Button)findViewById(R.id.menuNewtonButton);
        changeLanguage();

        Intent gi=getIntent();
        hList=gi.getDoubleArrayExtra("hList");
        vList=gi.getDoubleArrayExtra("vList");
        m=gi.getDoubleExtra("m",0);
        g=gi.getDoubleExtra("g",0);
        m1m2gaView.setText("m="+m+" kg     "+"g="+g+"m/(sec^2)");

        Log.w("Tag",String.valueOf(hList==null));
        Log.w("TAG"," l="+hList.length);

        String[] list=new String[hList.length+1];
        list[0]="t(sec)    h(m)    v(m/sec)";

        for(int i=1; i<hList.length+1; i++)
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

            String h=String.valueOf(hList[i-1]);
            dot=h.indexOf('.');
            if(h.length()>dot+digitsAfterDot+1)
            {
                h=h.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(h.length()<=dot+digitsAfterDot+1)
                {
                    h+=" ";
                }
            }
            t+=h+"      ";

            String v=String.valueOf(vList[i-1]);
            dot=v.indexOf('.');
            if(v.length()>dot+digitsAfterDot+3 && !v.contains("E"))
            {
                v=v.substring(0,dot+digitsAfterDot+3);
            }
            else
            {
                while(v.length()<=dot+digitsAfterDot+1)
                {
                    v+=" ";
                }
            }

            t+=v;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsNewtonLV.setAdapter(adp);
    }

    public void changeLanguage()
    {
        plotsNewtonButton.setText(Languages.plots);
        menuNewtonButton.setText(Languages.backToMenu);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}