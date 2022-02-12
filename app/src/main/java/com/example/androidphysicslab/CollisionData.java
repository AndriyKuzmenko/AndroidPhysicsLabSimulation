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

public class CollisionData extends AppCompatActivity
{
    TextView railHeightLabel,tableHeightLabel;
    EditText railHeightET,tableHeightET;
    Spinner planetCollisionSpinner;
    Button startCollisionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_data);

        railHeightLabel=(TextView)findViewById(R.id.railHeightLabel);
        tableHeightLabel=(TextView)findViewById(R.id.tableHeightLabel);
        railHeightET=(EditText) findViewById(R.id.railHeightET);
        tableHeightET=(EditText) findViewById(R.id.tableHeightET);
        planetCollisionSpinner=(Spinner) findViewById(R.id.planetCollisionSpinner);
        startCollisionButton=(Button)findViewById(R.id.startCollisionButton);

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
        railHeightLabel.setText(Languages.railHeight);
        tableHeightLabel.setText(Languages.tableHeight);
        startCollisionButton.setText(Languages.start);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetCollisionSpinner.setAdapter(adp);
    }
}