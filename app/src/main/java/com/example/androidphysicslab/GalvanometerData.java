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

public class GalvanometerData extends AppCompatActivity
{
    TextView nLabel,aLabel,epsionLabel,rLabel,magneticFieldLabel;
    EditText nET,aET,epsilonET,rET,magenticFielsET;
    Button startButton,backToMenuButton;
    AdView adView;

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
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        nLabel.setText(Languages.n);
        aLabel.setText(Languages.area);
        epsionLabel.setText(Languages.epsilon);
        rLabel.setText(Languages.maxR);
        startButton.setText(Languages.start);
        backToMenuButton.setText(Languages.back);
    }

    /**
     * @param view - the button pressed
     * @return Checks if the user entered all the necessary details. If he did, starts the animation.
     */

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
            Toast.makeText(GalvanometerData.this, Languages.missingField, Toast.LENGTH_SHORT).show();
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

        if(epsilon==0||n==0||a==0||maxR==0)
        {
            Toast.makeText(GalvanometerData.this, Languages.invalidInnput, Toast.LENGTH_SHORT).show();
            return;
        }

        si.putExtra("epsilon",epsilon);
        si.putExtra("maxR",maxR);
        si.putExtra("n",n);
        si.putExtra("a",a);
        si.putExtra("hEarthMagneticField",hEarthMagneticField);
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