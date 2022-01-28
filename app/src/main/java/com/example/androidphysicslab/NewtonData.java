package com.example.androidphysicslab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewtonData  extends AppCompatActivity
{
    TextView mass1Label,mass2Label,frictionLabel;
    EditText mass1ET,mass2ET,frictionET;
    Spinner planetNewtonSpinner;
    Button startNewtonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_data);

        mass1Label=(TextView)findViewById(R.id.mass1Label);
        mass2Label=(TextView)findViewById(R.id.mass2Label);
        frictionLabel=(TextView)findViewById(R.id.frictionLabel);
        mass1ET=(EditText)findViewById(R.id.mass1ET);
        mass2ET=(EditText)findViewById(R.id.mass2ET);
        frictionET=(EditText)findViewById(R.id.frictionET);
        planetNewtonSpinner=(Spinner)findViewById(R.id.planetNewtonSpinner);
        startNewtonButton=(Button)findViewById(R.id.startNewtonButton);

        changeLanguage();
    }

    public void changeLanguage()
    {
        mass1Label.setText(Languages.mass1);
        mass2Label.setText(Languages.mass2);
        frictionLabel.setText(Languages.friction);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetNewtonSpinner.setAdapter(adp);
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
}