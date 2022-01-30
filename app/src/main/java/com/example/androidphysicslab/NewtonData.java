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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NewtonData extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextView mass1Label,mass2Label,frictionLabel;
    EditText mass1ET,mass2ET,frictionET;
    Spinner planetNewtonSpinner;
    Button startNewtonButton;
    int planet;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_data);

        mass1Label=(TextView)findViewById(R.id.mass1Label);
        mass2Label=(TextView)findViewById(R.id.mass2Label);
        frictionLabel=(TextView)findViewById(R.id.frictionLabel);
        mass1ET=(EditText)findViewById(R.id.mass1ET);
        mass2ET=(EditText)findViewById(R.id.mass2ET);
        frictionET=(EditText)findViewById(R.id.frictionET);
        planetNewtonSpinner=(Spinner)findViewById(R.id.planetNewtonSpinner);
        startNewtonButton=(Button)findViewById(R.id.startNewtonButton);

        changeLanguage();
        planet=0;
    }

    public void changeLanguage()
    {
        mass1Label.setText(Languages.mass1);
        mass2Label.setText(Languages.mass2);
        frictionLabel.setText(Languages.friction);
        startNewtonButton.setText(Languages.start);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetNewtonSpinner.setAdapter(adp);
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

    public void startAnimation(boolean b)
    {
        Intent si=new Intent(this,NewtonActivity.class);

        Log.i("TAG","planet="+planet+", g="+Languages.gravity[planet]);

        double m1=Double.parseDouble(mass1ET.getText().toString());
        double m2=Double.parseDouble(mass2ET.getText().toString());
        double mu=Double.parseDouble(frictionET.getText().toString());

        si.putExtra("m1",m1);
        si.putExtra("m2",m2);
        si.putExtra("mu",mu);
        if(b) si.putExtra("planet",-1);
        else     si.putExtra("planet",planet);
        Log.w("TAG","m1= "+m1+" m2="+m2+" mu="+mu);

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

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}