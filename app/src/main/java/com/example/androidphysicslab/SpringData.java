package com.example.androidphysicslab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpringData extends AppCompatActivity
{
    TextView massSpringLabel,kLabel,startSpringButton;
    Spinner planetSpringSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_data);

        massSpringLabel=(TextView)findViewById(R.id.massSpringLabel);
        kLabel=(TextView)findViewById(R.id.kLabel);
        startSpringButton=(TextView)findViewById(R.id.startSpringButton);
        planetSpringSpinner=(Spinner)findViewById(R.id.planetSpringSpinner);

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
        massSpringLabel.setText(Languages.mass);
        kLabel.setText(Languages.springConstant);
        startSpringButton.setText(Languages.start);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetSpringSpinner.setAdapter(adp);
    }
}