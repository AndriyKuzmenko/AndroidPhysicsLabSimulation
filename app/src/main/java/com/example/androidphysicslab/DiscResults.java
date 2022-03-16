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

public class DiscResults extends AppCompatActivity
{
    TextView mgkDiscView;
    ListView resultsDiscLV;
    Button plotsDiscButton,menuDiscButton,animationDiscButton;
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
        animationDiscButton=(Button)findViewById(R.id.animationDiscButton);

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

        changeLanguage();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,DiscPlots.class);
        si.putExtra("lList",lList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("v0",v0);
        si.putExtra("l0",l0);
        si.putExtra("deltax",deltax);
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
        plotsDiscButton.setText(Languages.plots);
        menuDiscButton.setText(Languages.backToMenu);
        animationDiscButton.setText(Languages.backToAnimation);
    }

    public void animation(View view)
    {
        Intent si=new Intent(this,DiscActivity.class);

        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("shift",deltax);
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