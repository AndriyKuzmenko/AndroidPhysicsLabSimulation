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

public class SpringData extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextView massSpringLabel,kLabel,amplitudeLabel,periodsLabel;
    Button startSpringButton,backToMenuButton;
    EditText massSpringET,kET,amplitudeET,periodsET;
    Spinner planetSpringSpinner;
    AlertDialog.Builder adb;
    int planet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_data);

        massSpringLabel=(TextView)findViewById(R.id.massSpringLabel);
        kLabel=(TextView)findViewById(R.id.kLabel);
        startSpringButton=(Button)findViewById(R.id.startSpringButton);
        backToMenuButton=(Button)findViewById(R.id.backToMenuButton);
        planetSpringSpinner=(Spinner)findViewById(R.id.planetSpringSpinner);
        massSpringET=(EditText)findViewById(R.id.massSpringET);
        kET=(EditText)findViewById(R.id.kET);
        amplitudeLabel=(TextView)findViewById(R.id.amplitudeLabel);
        periodsLabel=(TextView)findViewById(R.id.periodsLabel);
        amplitudeET=(EditText)findViewById(R.id.amplitudeET);
        periodsET=(EditText)findViewById(R.id.periodsET);

        planet=0;
        planetSpringSpinner.setOnItemSelectedListener(this);

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
        massSpringLabel.setText(Languages.mass);
        kLabel.setText(Languages.springConstant);
        startSpringButton.setText(Languages.start);
        backToMenuButton.setText(Languages.back);
        amplitudeLabel.setText(Languages.amplitude);
        periodsLabel.setText(Languages.periods);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetSpringSpinner.setAdapter(adp);
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
        Intent si=new Intent(this, SpringActivity.class);

        Log.i("TAG","planet="+planet+", g="+Languages.gravity[planet]);

        String mStr=massSpringET.getText().toString();
        String kStr=kET.getText().toString();
        String aStr=amplitudeET.getText().toString();
        String pStr=periodsET.getText().toString();

        if(mStr.equals("") || kStr.equals("") || aStr.equals("") || pStr.equals(""))
        {
            Toast.makeText(SpringData.this, Languages.missingField, Toast.LENGTH_SHORT).show();
            return;
        }

        double m=Double.parseDouble(mStr);
        double k=Double.parseDouble(kStr);
        double a=Double.parseDouble(aStr);
        double p=Double.parseDouble(pStr);

        si.putExtra("mass",m);
        si.putExtra("k",k);
        si.putExtra("amplitude",a);
        si.putExtra("periods",p);
        if(b) si.putExtra("planet",-1);
        else     si.putExtra("planet",planet);
        Log.w("TAG","m= "+m+" k="+k);

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