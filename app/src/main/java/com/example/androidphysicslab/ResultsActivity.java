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
    ArrayList<FreeFallObject> freeFallList;
    ArrayList<SpringObject> springList;
    ArrayList<NewtonObject> newtonList;
    ListView experimentsLV;
    int freeFallStart,springStart,newtonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        experimentsList=new ArrayList<>();
        freeFallList=new ArrayList<>();
        springList=new ArrayList<>();
        newtonList=new ArrayList<>();
        experimentsLV=(ListView)findViewById(R.id.experimentsLV);
        experimentsLV.setOnItemClickListener(this);
        experimentsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        FBRef.myRef.child("Free Fall").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                freeFallStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    freeFallList.add(data.getValue(FreeFallObject.class));
                    Log.d("g=",freeFallList.get(freeFallList.size()-1).getG()+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        FBRef.myRef.child("Spring").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                springStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    springList.add(data.getValue(SpringObject.class));
                }
                displayList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        FBRef.myRef.child("Newton").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                newtonStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    newtonList.add(data.getValue(NewtonObject.class));
                }
                displayList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
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
            FreeFallObject results=freeFallList.get(position-freeFallStart);
            double[] hList=new double[results.getHList().size()];
            double[] vList=new double[results.getVList().size()];

            for(int i=0; i<hList.length; i++)
            {
                hList[i]=results.getHList().get(i);
                vList[i]=results.getVList().get(i);
            }

            Log.d("g=",""+results.getG());
            Log.d("m=",""+results.getM());

            Intent si=new Intent(this,FreeFallResults.class);
            si.putExtra("hList",hList);
            si.putExtra("vList",vList);
            si.putExtra("m",results.getM());
            si.putExtra("g",results.getG());
            startActivity(si);
        }
        else if(experimentsList.get(position).startsWith("Spring"))
        {
            SpringObject results=springList.get(position-springStart);
            double[] xList=new double[results.getVList().size()];
            double[] vList=new double[results.getVList().size()];
            double[] aList=new double[results.getVList().size()];

            for(int i=0; i<xList.length; i++)
            {
                xList[i]=results.getXList().get(i);
                vList[i]=results.getVList().get(i);
                aList[i]=results.getAList().get(i);
            }

            Log.d("g=",""+results.getG());
            Log.d("m=",""+results.getM());

            Intent si=new Intent(this,SpringResults.class);
            si.putExtra("xList",xList);
            si.putExtra("vList",vList);
            si.putExtra("aList",aList);
            si.putExtra("m",results.getM());
            si.putExtra("g",results.getG());
            si.putExtra("k",results.getK());
            startActivity(si);
        }
        else if(experimentsList.get(position).startsWith("Second Newton's Law"))
        {
            NewtonObject results=newtonList.get(position-newtonStart);
            double[] xList=new double[results.getVList().size()];
            double[] vList=new double[results.getVList().size()];

            for(int i=0; i<xList.length; i++)
            {
                xList[i]=results.getXList().get(i);
                vList[i]=results.getVList().get(i);
            }

            Intent si=new Intent(this,NewtonResults.class);
            si.putExtra("xList",xList);
            si.putExtra("vList",vList);
            si.putExtra("m1",results.getM1());
            si.putExtra("m2",results.getM2());
            si.putExtra("g",results.getG());
            si.putExtra("mu",results.getMu());
            startActivity(si);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}