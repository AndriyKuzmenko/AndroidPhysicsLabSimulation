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

public class NewtonResults extends AppCompatActivity
{
    TextView m1m2gaView;
    ListView resultsNewtonLV;
    Button plotsNewtonButton,menuNewtonButton,animationNewtonButton;

    double[] xList,vList;
    double m1,m2,g,mu;

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
        animationNewtonButton=(Button)findViewById(R.id.animationNewtonButton);
        changeLanguage();

        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        m1=gi.getDoubleExtra("m1",0);
        m2=gi.getDoubleExtra("m2",0);
        g=gi.getDoubleExtra("g",0);
        m1m2gaView.setText("m1="+m1+" m2="+m2+" g="+g+" mu="+mu);

        Log.w("Tag",String.valueOf(xList==null));
        Log.w("TAG"," l="+xList.length);

        String[] list=new String[xList.length+1];
        list[0]="t(sec)    x(m)    v(m/sec)";

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
            Log.d("TAG",x);
            dot=x.indexOf('.');
            if(x.length()>dot+digitsAfterDot+1)
            {
                x=x.substring(0,dot+digitsAfterDot+1);
                Log.d("TAG",x);
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
        animationNewtonButton.setText(Languages.backToAnimation);
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

    public void plots(View view)
    {
        Intent si=new Intent(this,NewtonPlots.class);

        si.putExtra("xList",xList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m1",m1);
        si.putExtra("m2",m2);
        si.putExtra("mu",mu);

        startActivity(si);
    }

    public void animation(View view)
    {
        Intent si=new Intent(this,NewtonActivity.class);

        si.putExtra("m1",m1);
        si.putExtra("m2",m2);
        si.putExtra("mu",mu);
        if(g==-10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));
        si.putExtra("rerun",true);
        Log.w("TAG","m1= "+m1+" m2="+m2+" mu="+mu);

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