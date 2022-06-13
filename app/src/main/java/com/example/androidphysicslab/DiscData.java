package com.example.androidphysicslab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DiscData extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextView massDiscLabel,muDiscLabel,kDiscLabel,shiftDiscLabel;
    EditText massDiscET,muDiscET,kDiscET,shiftDiscET;
    Spinner planetDiscSpinner;
    Button startButton,backToMenuButton;
    AlertDialog.Builder adb;
    int planet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_data);

        massDiscLabel=(TextView)findViewById(R.id.massDiscLabel);
        muDiscLabel=(TextView)findViewById(R.id.muDiscLabel);
        kDiscLabel=(TextView)findViewById(R.id.kDiscLabel);
        massDiscET=(EditText)findViewById(R.id.massDiscET);
        muDiscET=(EditText)findViewById(R.id.muDiscET);
        kDiscET=(EditText)findViewById(R.id.kDiscET);
        planetDiscSpinner=(Spinner)findViewById(R.id.planetDiscSpinner);
        startButton=(Button)findViewById(R.id.startButton);
        shiftDiscLabel=(TextView)findViewById(R.id.shiftDiscLabel);
        shiftDiscET=(EditText)findViewById(R.id.shiftDiscET);
        backToMenuButton=(Button)findViewById(R.id.backToMenuButton);
        planet=0;
        planetDiscSpinner.setOnItemSelectedListener(this);

        changeLanguage();
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
        massDiscLabel.setText(Languages.mass);
        muDiscLabel.setText(Languages.friction);
        kDiscLabel.setText(Languages.elasticConstant);
        startButton.setText(Languages.start);
        shiftDiscLabel.setText(Languages.shiftDistance);
        backToMenuButton.setText(Languages.back);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetDiscSpinner.setAdapter(adp);
    }

    /**
     * @param view - the button pressed
     * @return - if the user selected a planet other than Earth, calls method startAnimation with parameter false. Else, the app asks the user does he want to use g=10 or 9=9.807. If the user chose 10, calls method startAnimation was parameter true. Otherwise calls it with parameter false.
     */

    public void start(View view)
    {
        if(planet==2)
        {
            adb=new AlertDialog.Builder(this);
            adb.setMessage(Languages.whatIsTheGravityOfEarth);

            adb.setPositiveButton("9.807", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    startAnimation(false);
                }
            });

            adb.setNegativeButton("10", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    startAnimation(true);
                }
            });

            AlertDialog ad=adb.create();
            ad.show();
        }
        else
        {
            startAnimation(false);
        }
    }

    /**
     * @param b - true if the user wanted g=10. Falso if the user wants the accurate g of the selected planet.
     * @return Checks if the user entered all the necessary details. If he did, starts the animation.
     */

    public void startAnimation(boolean b)
    {
        Intent si=new Intent(this,DiscActivity.class);

        Log.i("TAG","planet="+planet+", g="+Languages.gravity[planet]);

        String mStr=massDiscET.getText().toString();
        String muStr=muDiscET.getText().toString();
        String kStr=kDiscET.getText().toString();
        String shiftStr=shiftDiscET.getText().toString();

        if(mStr.equals("") || muStr.equals("") || kStr.equals("") || shiftStr.equals(""))
        {
            Toast.makeText(DiscData.this, Languages.missingField, Toast.LENGTH_SHORT).show();
            return;
        }

        double m=Double.parseDouble(mStr);
        double mu=Double.parseDouble(muStr);
        double k=Double.parseDouble(kStr);
        double shift=Double.parseDouble(shiftStr);

        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("shift",shift);
        if(b) si.putExtra("planet",-1);
        else     si.putExtra("planet",planet);

        startActivity(si);
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     * @return updates the variable planet which stores the index of the selected planet.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        planet=position;
        Log.d("planet=",""+planet);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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