package com.example.androidphysicslab;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class GalvanometerResults extends AppCompatActivity
{
    TextView dataTV;
    ListView resultsLV;
    Button plotsButton,menuButton,xlButton;
    AdView adView;

    double[] rList,iList,thetaList,tgList;
    double hEarthMagneticField,epsilon,a;
    int n;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galvanometer_results);

        dataTV=(TextView)findViewById(R.id.dataTV);
        resultsLV=(ListView)findViewById(R.id.resultsLV);
        plotsButton=(Button)findViewById(R.id.plotsButton);
        menuButton=(Button)findViewById(R.id.menuButton);
        xlButton=(Button)findViewById(R.id.xlButton);
        adView=(AdView)findViewById(R.id.adView);

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        thetaList=gi.getDoubleArrayExtra("thetaList");
        tgList=gi.getDoubleArrayExtra("tgList");
        hEarthMagneticField=gi.getDoubleExtra("hEarthMagneticField",0);
        epsilon=gi.getDoubleExtra("epsilon",0);
        a=gi.getDoubleExtra("a",0);
        n=gi.getIntExtra("n",0);

        dataTV.setText("Bh="+hEarthMagneticField+" T ϵ="+epsilon+" V A="+a+" m^2 N="+n);
        changeLanguage();

        String[] list=new String[11];
        list[0]="R(Ohm)    I(A)    θ(deg)    tg(θ)";

        for(int i=1; i<rList.length+1; i++)
        {
            String t=" ";
            String r=FBRef.df.format(rList[i-1]);
            t+=r+"      ";

            String iStr=FBRef.df.format(iList[i-1]);
            t+=iStr+"      ";

            String theta=FBRef.df.format(thetaList[i-1]);
            t+=theta+"        ";

            String tg=FBRef.df.format(tgList[i-1]);
            t+=tg;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsLV.setAdapter(adp);

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
     * @param view - the button pressed
     * @return - goes back to the main menu
     */

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
        startActivity(si);
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
     * @return - goes back to the plots activity
     */

    public void plots(View view)
    {
        Intent si=new Intent(this,GalvanometerPlots.class);
        si.putExtra("rList",rList);
        si.putExtra("iList",iList);
        si.putExtra("thetaList",thetaList);
        si.putExtra("tgList",tgList);
        si.putExtra("epsilon",epsilon);
        si.putExtra("a",a);
        si.putExtra("n",n);
        si.putExtra("hEarthMagneticField",hEarthMagneticField);
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
        plotsButton.setText(Languages.plots);
        menuButton.setText(Languages.backToMenu);
        xlButton.setText(Languages.createExcel);
    }

    /**
     * @param view - the button that the user pressed
     * @return Asks the user to choose a file name
     */

    public void createExcel(View view)
    {
        adb=new AlertDialog.Builder(this);
        adb.setTitle(Languages.choseFileName);

        final EditText et=new EditText(this);
        adb.setView(et);

        adb.setPositiveButton(Languages.save, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String str=et.getText().toString();
                if(!str.equals(""))
                {
                    createFile(str);
                }
            }
        });

        adb.setNeutralButton(Languages.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    /**
     * @param name - the name of the file
     * @return Creates an excel file with all the data
     */

    public void createFile(String name)
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        HSSFWorkbook file=new HSSFWorkbook();
        HSSFSheet sheet=file.createSheet();

        HSSFRow firstRow=sheet.createRow(0);
        firstRow.createCell(0).setCellValue("R (Ohm)");
        firstRow.createCell(1).setCellValue("I (A)");
        firstRow.createCell(2).setCellValue("θ (deg)");
        firstRow.createCell(3).setCellValue("tgθ");

        firstRow.createCell(6).setCellValue("Bh="+hEarthMagneticField+" T ϵ="+epsilon+" V A="+a+" m^2 N="+n);

        for(int i=0;i<rList.length;i++)
        {
            HSSFRow row=sheet.createRow(i+1);
            row.createCell(0).setCellValue(rList[i]);
            row.createCell(1).setCellValue(iList[i]);
            row.createCell(2).setCellValue(thetaList[i]);
            row.createCell(3).setCellValue(tgList[i]);
        }

        File directory=new File(Environment.getExternalStorageDirectory() + "/Download/");
        File filePath=new File(directory,name+".xls");

        if(filePath.exists())
        {
            Toast.makeText(this,filePath.getName()+" exists",Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                filePath.createNewFile();
                FileOutputStream fos=new FileOutputStream(filePath);
                file.write(fos);

                if(fos!=null)
                {
                    fos.flush();
                    fos.close();
                }
                Toast.makeText(this,filePath.getName()+" was created",Toast.LENGTH_LONG).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}