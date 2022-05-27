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
    TextView nLabel,aLabel,epsionLabel,rLabel,magneticFieldLabel;
    EditText nET,aET,epsilonET,rET,magenticFielsET;
    Button startButton,backToMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_data);

        nLabel=(TextView)findViewById(R.id.nLabel);
        aLabel=(TextView)findViewById(R.id.aLabel);
        epsionLabel=(TextView)findViewById(R.id.epsilonLabel);
        rLabel=(TextView)findViewById(R.id.rLabel);
        magneticFieldLabel=(TextView) findViewById(R.id.magneticFieldLabel);
        nET=(EditText)findViewById(R.id.nET);
        aET=(EditText)findViewById(R.id.aET);
        epsilonET=(EditText)findViewById(R.id.epsilonET);
        rET=(EditText)findViewById(R.id.rET);
        magenticFielsET=(EditText)findViewById(R.id.magneticFieldET);
        startButton=(Button)findViewById(R.id.startButton);
        backToMenuButton=(Button)findViewById(R.id.backToMenuButton);

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
        backToMenuButton.setText(Languages.back);
    }

    public void start(View view)
    {
        Intent si=new Intent(this,GalvanometerActivity.class);

        String epsilonStr=epsilonET.getText().toString();
        String maxRStr=rET.getText().toString();
        String nStr=nET.getText().toString();
        String aStr=aET.getText().toString();
        String hEarthMagneticFieldStr=magenticFielsET.getText().toString();

        if(epsilonStr.equals("") || maxRStr.equals("") || nStr.equals("") || aStr.equals(""))
        {
            return;
        }

        double hEarthMagneticField;
        if(hEarthMagneticFieldStr.equals(""))
        {
            hEarthMagneticField=50*Math.pow(10,-6);
        }
        else
        {
            hEarthMagneticField=Double.parseDouble(hEarthMagneticFieldStr);
        }

        double epsilon=Double.parseDouble(epsilonStr);
        double maxR=Double.parseDouble(maxRStr);
        int n=Integer.parseInt(nStr);
        double a=Double.parseDouble(aStr);

        si.putExtra("epsilon",epsilon);
        si.putExtra("maxR",maxR);
        si.putExtra("n",n);
        si.putExtra("a",a);
        si.putExtra("hEarthMagneticField",hEarthMagneticField);
        startActivity(si);
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