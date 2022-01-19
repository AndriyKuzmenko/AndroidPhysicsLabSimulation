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
    Button freeFallButton;
    TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        freeFallButton=(Button)findViewById(R.id.freeFallButton);
        emailTV=(TextView)findViewById(R.id.emailTV);

        changeLanguage();
        emailTV.setText(FBRef.mUser.getEmail());
        FBRef.myRef=FBRef.database.getReference(FBRef.mUser.getUid());
    }

    public void freeFall(View view)
    {
        Intent si=new Intent(this, FreeFallData.class);
        startActivity(si);
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
        freeFallButton.setText(Languages.freeFall);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        FBRef.myRef=null;

        Intent si=new Intent(this, MainActivity.class);
        startActivity(si);
    }

    public void results(View view)
    {
        Intent si=new Intent(this, ResultsActivity.class);
        startActivity(si);
    }
}
