package com.example.androidphysicslab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ArrayList<String> experimentsList;
    ArrayList<FreeFallObject> freeFallList;
    ArrayList<SpringObject> springList;
    ArrayList<NewtonObject> newtonList;
    ArrayList<VoltageObject> voltageList;
    ArrayList<DiscObject> discList;
    ArrayList<CollisionObject> collisionList;
    ArrayList<GalvanometerObject> galvanometerList;
    ListView experimentsLV;
    Button backButton;
    AdView adView;
    int freeFallStart,springStart,newtonStart,voltageStart,discStart,collisionStart,galvanometerStart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        experimentsList=new ArrayList<>();
        freeFallList=new ArrayList<>();
        springList=new ArrayList<>();
        newtonList=new ArrayList<>();
        voltageList=new ArrayList<>();
        discList=new ArrayList<>();
        collisionList=new ArrayList<>();
        galvanometerList=new ArrayList<>();

        experimentsLV=(ListView)findViewById(R.id.experimentsLV);
        experimentsLV.setOnItemClickListener(this);
        experimentsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        backButton=(Button)findViewById(R.id.backButton);
        backButton.setText(Languages.back);
        adView=(AdView)findViewById(R.id.adView);

        showFreeFall();

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
     * Displays the list of experimemts on the listView
     */

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
     * @return Checks which experiment was pressed and sends the user to the results activity of this specific experiment
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position>=freeFallStart && position<springStart)
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
        else if(position>=springStart && position<newtonStart)
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
            si.putExtra("amplitude",results.getAmplitude());
            si.putExtra("periods",results.getPeriods());
            startActivity(si);
        }
        else if(position>=newtonStart && position<voltageStart)
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
        else if(position>=voltageStart && position<discStart)
        {
            VoltageObject results=voltageList.get(position-voltageStart);
            double[] rList=new double[10];
            double[] iList=new double[10];
            double[] vList=new double[10];

            for(int i=0; i<10; i++)
            {
                rList[i]=results.getRList().get(i);
                iList[i]=results.getIList().get(i);
                vList[i]=results.getVList().get(i);
            }

            Intent si=new Intent(this,VoltageResults.class);
            si.putExtra("rList",rList);
            si.putExtra("vList",vList);
            si.putExtra("iList",iList);
            si.putExtra("epsilon",results.getEpsilon());
            si.putExtra("internalR",results.getInternalR());
            si.putExtra("maxR",results.getMaxR());
            startActivity(si);
        }
        else if(position>=discStart && position<collisionStart)
        {
            DiscObject results=discList.get(position-discStart);

            double[] lList=new double[results.getVList().size()];
            double[] vList=new double[results.getVList().size()];

            for(int i=0; i<lList.length; i++)
            {
                Log.w("TAG",(results.getLList()==null)+"    "+(results.getVList()==null));
                lList[i]=results.getLList().get(i);
                vList[i]=results.getVList().get(i);
            }

            Intent si=new Intent(this,DiscResults.class);
            si.putExtra("lList",lList);
            si.putExtra("vList",vList);
            si.putExtra("g",results.getG());
            si.putExtra("m",results.getM());
            si.putExtra("mu",results.getMu());
            si.putExtra("k",results.getK());
            si.putExtra("v0",results.getV0());
            si.putExtra("l0",results.getL0());
            si.putExtra("deltax",results.getDeltax());
            startActivity(si);
        }
        else if(position>=collisionStart && position<galvanometerStart)
        {
            CollisionObject results=collisionList.get(position-collisionStart);

            Intent si=new Intent(this,CollisionResults.class);
            si.putExtra("g",results.getG());
            si.putExtra("v",results.getV());
            si.putExtra("u",results.getU());
            si.putExtra("h1",results.getH1());
            si.putExtra("h2",results.getH2());
            si.putExtra("tall",results.getTall());
            startActivity(si);
        }
        else if(position>=galvanometerStart)
        {
            GalvanometerObject results=galvanometerList.get(position-galvanometerStart);

            Intent si=new Intent(this,GalvanometerResults.class);
            double[] rList=new double[results.getRList().size()];
            double[] iList=new double[results.getIList().size()];
            double[] thetaList=new double[results.getThetaList().size()];
            double[] tgList=new double[results.getTgList().size()];

            for(int i=0; i<rList.length; i++)
            {
                rList[i]=results.getRList().get(i);
                iList[i]=results.getIList().get(i);
                thetaList[i]=results.getThetaList().get(i);
                tgList[i]=results.getTgList().get(i);
            }

            si.putExtra("rList",rList);
            si.putExtra("iList",iList);
            si.putExtra("thetaList",thetaList);
            si.putExtra("tgList",tgList);
            si.putExtra("epsilon",results.getEpsilon());
            si.putExtra("a",results.getArea());
            si.putExtra("n",results.getN());
            si.putExtra("hEarthMagneticField",results.getHEarthMagenticField());
            startActivity(si);
            startActivity(si);
        }
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
     * @return Reads all the data for free fall experiment
     */

    public void showFreeFall()
    {
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
                showSpring();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for spring experiment
     */

    public void showSpring()
    {
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
                showNewton();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for Second Newton's Law experiment
     */

    public void showNewton()
    {
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
                showVoltage();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for voltage experiment
     */

    public void showVoltage()
    {
        FBRef.myRef.child("Voltage").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                voltageStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    voltageList.add(data.getValue(VoltageObject.class));
                }
                showDisc();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for disc experiment
     */

    public void showDisc()
    {
        FBRef.myRef.child("Disc").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                discStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    discList.add(data.getValue(DiscObject.class));
                }
                showCollision();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for collision experiment
     */

    public void showCollision()
    {
        FBRef.myRef.child("Collision").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                collisionStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    collisionList.add(data.getValue(CollisionObject.class));
                }
                showGalvanometer();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @return Reads all the data for tangent galvanometer experiment
     */

    public void showGalvanometer()
    {
        FBRef.myRef.child("Galvanometer").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                galvanometerStart=experimentsList.size();
                for(DataSnapshot data : dS.getChildren())
                {
                    String name=data.getKey();
                    experimentsList.add(name);

                    galvanometerList.add(data.getValue(GalvanometerObject.class));
                }
                displayList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
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