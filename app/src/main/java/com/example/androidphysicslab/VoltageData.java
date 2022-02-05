package com.example.androidphysicslab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VoltageData extends AppCompatActivity
{
    TextView epsilonLabel,internalRLabel,maxRLabel;
    EditText epsilonET,internalRET,maxRET;
    Button startVoltageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voltage_data);

        epsilonLabel=(TextView)findViewById(R.id.epsilonLabel);
        internalRLabel=(TextView) findViewById(R.id.internalRLabel);
        maxRLabel=(TextView)findViewById(R.id.maxRLabel);
        epsilonET=(EditText)findViewById(R.id.epsilonET);
        internalRET=(EditText)findViewById(R.id.internalRET);
        maxRET=(EditText)findViewById(R.id.maxRET);
        startVoltageButton=(Button)findViewById(R.id.startVoltageButton);

        changeLanguage();
    }

    public void changeLanguage()
    {
        epsilonLabel.setText(Languages.epsilon);
        internalRLabel.setText(Languages.internalR);
        maxRLabel.setText(Languages.maxR);
        startVoltageButton.setText(Languages.start);
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