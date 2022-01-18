package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ArrayList<String> experimentsList;
    ArrayList<Experiment> resultsList;
    ListView experimentsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        experimentsList=new ArrayList<>();
        resultsList=new ArrayList<>();
        experimentsLV=(ListView)findViewById(R.id.experimentsLV);
        experimentsLV.setOnItemClickListener(this);
        experimentsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ValueEventListener stuListener = new ValueEventListener()
        {

            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                experimentsList.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Log.d("TAG","Added an experiment");
                    String name=data.getKey();
                    experimentsList.add(data.getKey());

                    resultsList.add(data.getValue(Experiment.class));
                }

                displayList();
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase Database rules. For more information on
             * securing your data, see: <a
             * href="https://firebase.google.com/docs/database/security/quickstart" target="_blank"> Security
             * Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };
        FBRef.myRef.addValueEventListener(stuListener);
    }

    public void displayList()
    {
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, experimentsList);
        experimentsLV.setAdapter(adp);
        Log.d("Set","Adapter");
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(experimentsList.get(position).startsWith("Free Fall"))
        {
            FreeFallObject results=(FreeFallObject)(resultsList.get(position));
            double[] hList=new double[results.getHList().size()];
            double[] vList=new double[results.getVList().size()];

            for(int i=0; i<hList.length; i++)
            {
                hList[i]=results.getHList().get(i);
                vList[i]=results.getVList().get(i);
            }

            Intent si=new Intent(this,FreeFallResults.class);
            si.putExtra("hList",hList);
            si.putExtra("vList",vList);
            startActivity(si);
        }
    }
}