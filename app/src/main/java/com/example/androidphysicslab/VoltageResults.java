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

public class VoltageResults extends AppCompatActivity
{
    TextView voltageView;
    ListView resultsVoltageLV;
    Button plotsVoltageButton,menuVoltageButton;

    double[] rList,iList,vList;
    double epsilon,internalR,maxR;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voltage_results);

        voltageView=(TextView)findViewById(R.id.voltageView);
        resultsVoltageLV=(ListView) findViewById(R.id.resultsVoltageLV);
        plotsVoltageButton=(Button)findViewById(R.id.plotsVoltageButton);
        menuVoltageButton=(Button)findViewById(R.id.menuVoltageButton);

        Intent gi=getIntent();
        rList=gi.getDoubleArrayExtra("rList");
        iList=gi.getDoubleArrayExtra("iList");
        vList=gi.getDoubleArrayExtra("vList");
        epsilon=gi.getDoubleExtra("epsilon",0);
        internalR=gi.getDoubleExtra("internalR",0);
        maxR=gi.getDoubleExtra("maxR",0);

        String[] list=new String[11];
        list[0]="R(Ohm)    I(A)    V(V)";
        changeLanguage();
        voltageView.setText("ϵ="+epsilon+" V   r="+internalR+" Ω");

        for(int j=1; j<11; j++)
        {
            String t=" ";
            String r=FBRef.df.format(rList[j-1]);
            t+=r+"      ";

            String i=FBRef.df.format(iList[j-1]);
            t+=i+"      ";

            String v=FBRef.df.format(vList[j-1]);
            t+=v;

            list[j]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsVoltageLV.setAdapter(adp);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
        startActivity(si);
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,VoltagePlots.class);

        si.putExtra("rList",rList);
        si.putExtra("vList",vList);
        si.putExtra("iList",iList);
        si.putExtra("epsilon",epsilon);
        si.putExtra("internalR",internalR);
        si.putExtra("maxR",maxR);

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
        plotsVoltageButton.setText(Languages.plots);
        menuVoltageButton.setText(Languages.backToMenu);
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
        firstRow.createCell(2).setCellValue("V (V)");
        firstRow.createCell(5).setCellValue("epsilon="+epsilon+" V    r="+internalR+"Ohm");

        for(int i=0;i<rList.length;i++)
        {
            HSSFRow row=sheet.createRow(i+1);
            row.createCell(0).setCellValue(rList[i]);
            row.createCell(1).setCellValue(iList[i]);
            row.createCell(2).setCellValue(vList[i]);
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