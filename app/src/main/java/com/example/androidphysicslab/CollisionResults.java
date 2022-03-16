package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CollisionResults extends AppCompatActivity
{
    TextView dataCollisionView;
    Button menuCollisionButton,animationCollisionButton;
    double h1,h2,v,u,g;
    boolean tall;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_results);

        dataCollisionView=(TextView)findViewById(R.id.dataCollisionView);
        menuCollisionButton=(Button)findViewById(R.id.menuCollisionButton);
        animationCollisionButton=(Button)findViewById(R.id.animationCollisionButton);

        Intent gi=getIntent();
        h1=gi.getDoubleExtra("h1",0);
        h2=gi.getDoubleExtra("h2",0);
        v=gi.getDoubleExtra("v",0);
        u=gi.getDoubleExtra("u",0);
        g=gi.getDoubleExtra("g",0);
        tall=gi.getBooleanExtra("tall",false);

        dataCollisionView.setText("h1="+h1+" m\nh2="+h2+" m\nv="+v+" m/sec\nu="+u+" m/sec g="+g+" m/sec^2");
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

    public void changeLanguage()
    {
        menuCollisionButton.setText(Languages.backToMenu);
        animationCollisionButton.setText(Languages.backToAnimation);
    }

    public void animation(View view)
    {
        Intent si=new Intent(this, CollisionActivity.class);

        si.putExtra("h2",h2);
        si.putExtra("h1",h1);
        if(g==10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));

        si.putExtra("tall",tall);
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