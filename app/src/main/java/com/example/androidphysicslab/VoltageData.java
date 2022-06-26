package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

public class VoltageData extends AppCompatActivity
{
    TextView epsilonLabel,internalRLabel,maxRLabel;
    EditText epsilonET,internalRET,maxRET;
    Button startVoltageButton,backToMenuButton;
    AdView adView;

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
        adView=(AdView)findViewById(R.id.adView);

        changeLanguage();

        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
            Toast.makeText(VoltageData.this, Languages.missingField, Toast.LENGTH_SHORT).show();
            return;
        }

        double epsilon=Double.parseDouble(epsilonStr);
        double internalR=Double.parseDouble(internalRStr);
        double maxR=Double.parseDouble(maxRStr);

        if(epsilon==0||(internalR==0&&maxR==0))
        {
            Toast.makeText(VoltageData.this, Languages.invalidInnput, Toast.LENGTH_SHORT).show();
            return;
        }

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