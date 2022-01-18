package com.example.androidphysicslab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FreeFallResults extends AppCompatActivity
{
    ListView results;
    double[] hList,vList;
    Button plotsButton,menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot=2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall_results);

        results=(ListView)findViewById(R.id.results);
        plotsButton=(Button)findViewById(R.id.plotsButton);
        menuButton=(Button)findViewById(R.id.menuButton);
        changeLanguage();

        Intent gi=getIntent();
        hList=gi.getDoubleArrayExtra("hList");
        vList=gi.getDoubleArrayExtra("vList");

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
            if(v.length()>dot+digitsAfterDot+1)
            {
                v=v.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(h.length()<=dot+digitsAfterDot+1)
                {
                    v+=" ";
                }
            }

            t+=v;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        results.setAdapter(adp);
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,FreeFallPlots.class);

        si.putExtra("hList",hList);
        si.putExtra("vList",vList);

        startActivity(si);
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

    public void changeLanguage()
    {
        plotsButton.setText(Languages.plots);
        menuButton.setText(Languages.backToMenu);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}