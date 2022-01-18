package com.example.androidphysicslab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import org.w3c.dom.Text;

public class FreeFallData extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextView massLabel, heightLabel;
    Spinner planetSpinner;
    int planet;
    AlertDialog.Builder adb;
    EditText massET, heightET;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall_data);

        massLabel=(TextView)findViewById(R.id.massLabel);
        heightLabel=(TextView)findViewById(R.id.heightLabel);
        planetSpinner=(Spinner)findViewById(R.id.planetSpinner);
        massET=(EditText)findViewById(R.id.massET);
        heightET=(EditText)findViewById(R.id.heightET);
        startButton=(Button)findViewById(R.id.startButton);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetSpinner.setAdapter(adp);
        planetSpinner.setOnItemSelectedListener(this);
        planet=0;

        changeLanguage();
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
        Intent si=new Intent(this, FreeFallActivity.class);

        Log.i("TAG","planet="+planet+", g="+Languages.gravity[planet]);

        double m=Double.parseDouble(massET.getText().toString());
        double h=Double.parseDouble(heightET.getText().toString());

        si.putExtra("mass",m);
        si.putExtra("height",h);
        if(b) si.putExtra("planet",-1);
        else     si.putExtra("planet",planet);
        Log.w("TAG","m= "+m+" h="+h);

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
        massLabel.setText(Languages.mass);
        heightLabel.setText(Languages.height);
        startButton.setText(Languages.start);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,Languages.planets);
        planetSpinner.setAdapter(adp);
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
