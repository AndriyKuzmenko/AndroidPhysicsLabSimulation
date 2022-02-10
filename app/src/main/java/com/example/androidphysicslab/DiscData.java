package com.example.androidphysicslab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiscData extends AppCompatActivity
{
    TextView massDiscLabel,muDiscLabel,kDiscLabel;
    EditText massDiscET,muDiscET,kDiscET;
    Spinner planetDiscSpinner;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_data);

        massDiscLabel=(TextView)findViewById(R.id.massDiscLabel);
        muDiscLabel=(TextView)findViewById(R.id.muDiscLabel);
        kDiscLabel=(TextView)findViewById(R.id.kDiscLabel);
        massDiscET=(EditText)findViewById(R.id.massDiscET);
        muDiscET=(EditText)findViewById(R.id.muDiscET);
        kDiscET=(EditText)findViewById(R.id.kDiscET);
        planetDiscSpinner=(Spinner)findViewById(R.id.planetDiscSpinner);
        startButton=(Button)findViewById(R.id.startButton);
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
        massDiscLabel.setText(Languages.mass);
        muDiscLabel.setText(Languages.friction);
        kDiscLabel.setText(Languages.elasticConstant);
        startButton.setText(Languages.start);
    }
}