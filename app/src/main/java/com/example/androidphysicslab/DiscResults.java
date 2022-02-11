package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiscResults extends AppCompatActivity
{
    TextView mgkDiscView;
    ListView resultsDiscLV;
    Button plotsDiscButton,menuDiscButton;
    double[] lList,vList;
    double m,mu,g,k,deltax,v0,l0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_results);

        mgkDiscView=(TextView)findViewById(R.id.mgkDiscView);
        resultsDiscLV=(ListView)findViewById(R.id.resultsDiscLV);
        plotsDiscButton=(Button)findViewById(R.id.plotsDiscButton);
        menuDiscButton=(Button)findViewById(R.id.menuDiscButton);

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

        mgkDiscView.setText("m="+m+" kg  mu="+mu+"  g="+g+" m/sec^2  k="+k+" N/m  Δx="+deltax+" m  v0="+l0+" m");

        String[] list=new String[lList.length+1];
        list[0]="t(sec)    l(m)    v(m/sec)";

        for(int i=1; i<lList.length+1; i++)
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

            String l=String.valueOf(lList[i-1]);
            dot=l.indexOf('.');
            if(l.length()>dot+digitsAfterDot+1)
            {
                l=l.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(l.length()<=dot+digitsAfterDot+1)
                {
                    l+=" ";
                }
            }
            t+=l+"      ";

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
        resultsDiscLV.setAdapter(adp);
    }
}