package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

public class CreditsActivity extends AppCompatActivity
{
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        adView=(AdView)findViewById(R.id.adView);

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
     * @param view - the button pressed
     * @return - goes back to the main menu
     */

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
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
}