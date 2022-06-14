package com.example.androidphysicslab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity
{
    Button freeFallButton,springButton,newtonButton,resultsButton,creditsButton,logOutButton,voltageButton,discButton,collisionButton,galvanometerButton;
    TextView emailTV,experimentsLabel,otherLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        freeFallButton = (Button) findViewById(R.id.freeFallButton);
        springButton = (Button) findViewById(R.id.springButton);
        newtonButton = (Button) findViewById(R.id.newtonButton);
        resultsButton = (Button) findViewById(R.id.resultsButton);
        creditsButton = (Button) findViewById(R.id.creditsButton);
        discButton = (Button) findViewById(R.id.discButton);
        logOutButton = (Button) findViewById(R.id.logOutButton);
        voltageButton = (Button) findViewById(R.id.voltageButton);
        emailTV = (TextView) findViewById(R.id.emailTV);
        experimentsLabel = (TextView) findViewById(R.id.experimentsLabel);
        otherLabel = (TextView) findViewById(R.id.otherLabel);
        collisionButton = (Button) findViewById(R.id.collisionButton);
        galvanometerButton = (Button) findViewById(R.id.galvanometerButton);

        changeLanguage();
        if (FBRef.mUser != null)
        {
            emailTV.setText(FBRef.mUser.getEmail());
            FBRef.myRef=FBRef.database.getReference(FBRef.mUser.getUid());
        }
        else
        {
            emailTV.setText(Languages.guest);
            resultsButton.setEnabled(false);
        }
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the free fall input activity
     */

    public void freeFall(View view)
    {
        Intent si=new Intent(this, FreeFallData.class);
        startActivity(si);
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
        freeFallButton.setText(Languages.freeFall);
        springButton.setText(Languages.spring);
        resultsButton.setText(Languages.results);
        creditsButton.setText(Languages.credits);
        logOutButton.setText(Languages.logOut);
        newtonButton.setText(Languages.secondNewtonsLaw);
        experimentsLabel.setText(Languages.experiments);
        otherLabel.setText(Languages.other);
        voltageButton.setText(Languages.voltageExperiment);
        discButton.setText(Languages.discExperiment);
        collisionButton.setText(Languages.collision);
        galvanometerButton.setText(Languages.galvanometer);
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
     * @return - logs the user out and moves to the log in activity
     */

    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        FBRef.myRef=null;

        Intent si=new Intent(this, MainActivity.class);
        startActivity(si);
    }

    /**
     * @param view - the button pressed
     * @return - moves the user to the results activity to see a list of all previous experiments. Inactive for guests.
     */

    public void results(View view)
    {
        Intent si=new Intent(this, ResultsActivity.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the spring input activity
     */

    public void spring(View view)
    {
        Intent si=new Intent(this, SpringData.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the Secodn Newton's Law input activity
     */

    public void newton(View view)
    {
        Intent si=new Intent(this, NewtonData.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the credits activity
     */

    public void credits(View view)
    {
        Intent si=new Intent(this, CreditsActivity.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the voltage input activity
     */

    public void voltage(View view)
    {
        Intent si=new Intent(this, VoltageData.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the disc input activity
     */

    public void disc(View view)
    {
        Intent si=new Intent(this, DiscData.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the collision input activity
     */

    public void collision(View view)
    {
        Intent si=new Intent(this, CollisionData.class);
        startActivity(si);
    }

    /**
     * @param view - the buttton that was pressed
     * @return moves the user to the tangent galvanometer input activity
     */

    public void galvanometer(View view)
    {
        Intent si=new Intent(this, GalvanometerData.class);
        startActivity(si);
    }
}
