package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CollisionResults extends AppCompatActivity
{
    TextView dataCollisionView;
    Button menuCollisionButton,animationCollisionButton;
    double h1,h2,v,u,g;
    boolean tall;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_results);

        dataCollisionView=(TextView)findViewById(R.id.dataCollisionView);
        menuCollisionButton=(Button)findViewById(R.id.menuCollisionButton);
        animationCollisionButton=(Button)findViewById(R.id.animationCollisionButton);

        Intent gi=getIntent();
        h1=gi.getDoubleExtra("h1",0);
        h2=gi.getDoubleExtra("h2",0);
        v=gi.getDoubleExtra("v",0);
        u=gi.getDoubleExtra("u",0);
        g=gi.getDoubleExtra("g",0);
        tall=gi.getBooleanExtra("tall",false);

        dataCollisionView.setText("h1="+h1+" m\nh2="+h2+" m\nv="+v+" m/sec\nu="+u+" m/sec\ng="+g+" m/sec^2");
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
        menuCollisionButton.setText(Languages.backToMenu);
        animationCollisionButton.setText(Languages.backToAnimation);
    }

    /**
     * @param view - the button that the user pressed
     * @return - sends all the necessary data to the animation activity where the user can view the animation again.
     */

    public void animation(View view)
    {
        Intent si=new Intent(this, CollisionActivity.class);

        si.putExtra("h2",h2);
        si.putExtra("h1",h1);
        if(g==10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));

        si.putExtra("tall",tall);
        si.putExtra("rerun",true);
        startActivity(si);
    }

    /**
     * @param g - the gravity acceleation of a planet in the solar system
     * @return - returns which planet has the specified gravity acceleration. If none has, returns -1 which later on will mean g=10
     */

    public int findPlanet(double g)
    {
        int n=-1;
        for(int i=0;i<Languages.gravity.length;i++)
        {
            if(Languages.gravity[i]==g)
            {
                n=i;
            }
        }
        return n;
    }
}