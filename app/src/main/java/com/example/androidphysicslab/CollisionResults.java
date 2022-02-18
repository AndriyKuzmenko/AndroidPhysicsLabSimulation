package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CollisionResults extends AppCompatActivity
{
    TextView dataCollisionView;
    Button menuCollisionButton;
    double h1,h2,v,u,g;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_results);

        dataCollisionView=(TextView)findViewById(R.id.dataCollisionView);
        menuCollisionButton=(Button)findViewById(R.id.menuCollisionButton);

        Intent gi=getIntent();
        h1=gi.getDoubleExtra("h1",0);
        h2=gi.getDoubleExtra("h2",0);
        v=gi.getDoubleExtra("v",0);
        u=gi.getDoubleExtra("u",0);
        g=gi.getDoubleExtra("g",0);

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
}