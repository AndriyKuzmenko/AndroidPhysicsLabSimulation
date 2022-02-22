package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GalvanometerData extends AppCompatActivity
{
    TextView nLabel,aLabel,epsionLabel,rLabel;
    EditText nET,aET,epsilonET,rET;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_data);

        nLabel=(TextView)findViewById(R.id.nLabel);
        aLabel=(TextView)findViewById(R.id.aLabel);
        epsionLabel=(TextView)findViewById(R.id.epsilonLabel);
        rLabel=(TextView)findViewById(R.id.rLabel);
        nET=(EditText)findViewById(R.id.nET);
        aET=(EditText)findViewById(R.id.aET);
        epsilonET=(EditText)findViewById(R.id.epsilonET);
        rET=(EditText)findViewById(R.id.rET);
        startButton=(Button)findViewById(R.id.startButton);

        changeLanguage();
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
        nLabel.setText(Languages.n);
        aLabel.setText(Languages.area);
        epsionLabel.setText(Languages.epsilon);
        rLabel.setText(Languages.maxR);
        startButton.setText(Languages.start);
    }

    public void start(View view)
    {
        Intent si=new Intent(this,GalvanometerActivity.class);
        startActivity(si);
    }
}