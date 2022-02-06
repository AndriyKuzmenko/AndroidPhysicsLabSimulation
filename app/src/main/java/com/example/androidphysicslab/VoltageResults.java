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

public class VoltageResults extends AppCompatActivity
{
    TextView voltageView;
    ListView resultsVoltageLV;
    Button plotsVoltageButton,menuVoltageButton;

    double[] rList,iList,vList;
    double epsilon,internalR,maxR;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voltage_results);

        voltageView=(TextView)findViewById(R.id.voltageView);
        resultsVoltageLV=(ListView) findViewById(R.id.resultsVoltageLV);
        plotsVoltageButton=(Button)findViewById(R.id.plotsVoltageButton);
        menuVoltageButton=(Button)findViewById(R.id.menuVoltageButton);

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        vList=gi.getDoubleArrayExtra("vList");
        epsilon=gi.getDoubleExtra("epsilon",0);
        internalR=gi.getDoubleExtra("internalR",0);
        maxR=gi.getDoubleExtra("maxR",0);

        String[] list=new String[11];
        list[0]="R(Ohm)    I(A)    V(V)";
        changeLanguage();
        voltageView.setText("ϵ="+epsilon+" V   r="+internalR+" Ω");

        for(int j=1; j<11; j++)
        {
            String t=" ";
            String r=rList[j-1]+"";
            int dot=r.indexOf('.');
            if(r.length()>dot+digitsAfterDot+1)
            {
                r=r.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(r.length()<=dot+digitsAfterDot+1)
                {
                    r+=" ";
                }
            }
            t+=r+"      ";

            String i=String.valueOf(iList[j-1]);
            dot=i.indexOf('.');
            if(i.length()>dot+digitsAfterDot+1)
            {
                i=i.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(i.length()<=dot+digitsAfterDot+1)
                {
                    i+=" ";
                }
            }
            t+=i+"      ";

            String v=String.valueOf(vList[j-1]);
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
            t+=v;

            list[j]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsVoltageLV.setAdapter(adp);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
        startActivity(si);
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,VoltagePlots.class);

        si.putExtra("rList",rList);
        si.putExtra("vList",vList);
        si.putExtra("iList",iList);
        si.putExtra("epsilon",epsilon);
        si.putExtra("internalR",internalR);
        si.putExtra("maxR",maxR);

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
        plotsVoltageButton.setText(Languages.plots);
        menuVoltageButton.setText(Languages.backToMenu);
    }
}