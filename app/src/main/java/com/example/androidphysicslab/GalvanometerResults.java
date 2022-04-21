package com.example.androidphysicslab;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GalvanometerResults extends AppCompatActivity
{
    TextView dataTV;
    ListView resultsLV;
    Button plotsButton,menuButton;
    double[] rList,iList,thetaList,tgList;
    double hEarthMagneticField,epsilon,a,n;
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

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        thetaList=gi.getDoubleArrayExtra("thetaList");
        tgList=gi.getDoubleArrayExtra("tgList");
        hEarthMagneticField=gi.getDoubleExtra("hEarthMagneticField",0);
        epsilon=gi.getDoubleExtra("epsilon",0);
        a=gi.getDoubleExtra("a",0);
        n=gi.getDoubleExtra("n",0);

        dataTV.setText("Bh="+hEarthMagneticField+" T ϵ="+epsilon+" V A="+a+" m^2 N="+n);
        changeLanguage();

        String[] list=new String[11];
        list[0]="R(Ohm)    I(A)    θ(deg)    tg(θ)";

        for(int i=1; i<rList.length+1; i++)
        {
            String t=" ";
            String r=String.valueOf((double)(rList[i-1]));
            int dot=r.indexOf('.');
            if(r.length()>dot+digitsAfterDot+1)
            {
                r=r.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(r.length()<=dot+digitsAfterDot+1)
                {
                    r+=" ";
                }
                if(i%10==0)r+="  ";
            }
            t+=r+"      ";

            String iStr=String.valueOf(iList[i-1]);
            dot=iStr.indexOf('.');
            if(iStr.length()>dot+digitsAfterDot+1)
            {
                iStr=iStr.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(iStr.length()<=dot+digitsAfterDot+1)
                {
                    iStr+=" ";
                }
            }
            t+=iStr+"      ";

            String theta=String.valueOf(thetaList[i-1]);
            dot=theta.indexOf('.');
            if(theta.length()>dot+digitsAfterDot+1)
            {
                theta=theta.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(theta.length()<=dot+digitsAfterDot+1)
                {
                    theta+=" ";
                }
            }
            t+=theta+"        ";

            String tg=String.valueOf(tgList[i-1]);
            dot=tg.indexOf('.');
            if(tg.length()>dot+digitsAfterDot+1)
            {
                tg=tg.substring(0,dot+digitsAfterDot+1);
            }
            else
            {
                while(tg.length()<=dot+digitsAfterDot+1)
                {
                    tg+=" ";
                }
            }
            t+=tg;
            Log.w("t=",t);

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsLV.setAdapter(adp);
    }

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
        startActivity(si);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

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
        plotsButton.setText(Languages.plots);
        menuButton.setText(Languages.backToMenu);
    }

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

        File filePath=new File(Environment.getExternalStorageDirectory()+"/"+name+".xls");

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