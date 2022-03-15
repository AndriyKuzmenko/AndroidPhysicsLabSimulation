package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    Button plotsSpringButton,menuSpringButton,animationSpringButton;
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
        animationSpringButton=(Button)findViewById(R.id.animationSpringButton);
        changeLanguage();

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

    public void changeLanguage()
    {
        plotsSpringButton.setText(Languages.plots);
        menuSpringButton.setText(Languages.backToMenu);
        animationSpringButton.setText(Languages.backToAnimation);
    }

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
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

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,SpringPlots.class);

        si.putExtra("xList",xList);
        si.putExtra("vList",vList);
        si.putExtra("aList",aList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("k",k);

        startActivity(si);
    }

    public void animation(View view)
    {
        Intent si=new Intent(this, SpringActivity.class);

        si.putExtra("mass",m);
        si.putExtra("k",k);
        if(g==10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));
        si.putExtra("rerun",true);
        startActivity(si);
    }

    public int findPlanet(double g)
    {
        int n=-1;
        for(int i=0;i<Languages.gravity.length;i++)
        {
            if(Languages.gravity[i]==g)
            {
                n=i;
            }
        }
        return n;
    }
}