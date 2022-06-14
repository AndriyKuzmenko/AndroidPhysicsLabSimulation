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
    Button startVoltageButton,backToMenuButton;

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
        backToMenuButton=(Button)findViewById(R.id.backToMenuButton);

        changeLanguage();
    }

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        epsilonLabel.setText(Languages.epsilon);
        internalRLabel.setText(Languages.internalR);
        maxRLabel.setText(Languages.maxR);
        startVoltageButton.setText(Languages.start);
        backToMenuButton.setText(Languages.back);
    }

    /**
     * @param menu  - the menu
     * @return      - shows the main menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    /**
     * @param item - the item that was selected
     * @return     - Changes the language to the selected language
     */

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

    /**
     * @param view - the button pressed
     * @return Checks if the user entered all the necessary details. If he did, starts the animation.
     */

    public void start(View view)
    {
        Intent si=new Intent(this,VoltageActivity.class);

        String epsilonStr=epsilonET.getText().toString();
        String internalRStr=internalRET.getText().toString();
        String maxRStr=maxRET.getText().toString();

        if(epsilonStr.equals("") || internalRStr.equals("") || maxRStr.equals(""))
        {
            return;
        }

        double epsilon=Double.parseDouble(epsilonStr);
        double internalR=Double.parseDouble(internalRStr);
        double maxR=Double.parseDouble(maxRStr);

        si.putExtra("epsilon",epsilon);
        si.putExtra("internalR",internalR);
        si.putExtra("maxR",maxR);

        startActivity(si);
    }

    /**
     * @return - finishes the activity
     */

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the main menu
     */

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
        startActivity(si);
    }
}