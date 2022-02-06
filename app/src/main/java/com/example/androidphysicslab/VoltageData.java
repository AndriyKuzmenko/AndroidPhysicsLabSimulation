package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    public void start(View view)
    {
        Intent si=new Intent(this,VoltageActivity.class);

        double epsilon=Double.parseDouble(epsilonET.getText().toString());
        double internalR=Double.parseDouble(internalRET.getText().toString());
        double maxR=Double.parseDouble(maxRET.getText().toString());

        si.putExtra("epsilon",epsilon);
        si.putExtra("internalR",internalR);
        si.putExtra("maxR",maxR);

        startActivity(si);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}