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

public class DiscResults extends AppCompatActivity
{
    TextView mgkDiscView;
    ListView resultsDiscLV;
    Button plotsDiscButton,menuDiscButton,animationDiscButton;
    double[] lList,vList;
    double m,mu,g,k,deltax,v0,l0;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_results);

        mgkDiscView=(TextView)findViewById(R.id.mgkDiscView);
        resultsDiscLV=(ListView)findViewById(R.id.resultsDiscLV);
        plotsDiscButton=(Button)findViewById(R.id.plotsDiscButton);
        menuDiscButton=(Button)findViewById(R.id.menuDiscButton);
        animationDiscButton=(Button)findViewById(R.id.animationDiscButton);

        Intent gi=getIntent();
        lList=gi.getDoubleArrayExtra("lList");
        vList=gi.getDoubleArrayExtra("vList");
        m=gi.getDoubleExtra("m",0);
        mu=gi.getDoubleExtra("mu",0);
        g=gi.getDoubleExtra("g",0);
        k=gi.getDoubleExtra("k",0);
        deltax=gi.getDoubleExtra("deltax",0);
        v0=gi.getDoubleExtra("v0",0);
        l0=gi.getDoubleExtra("l0",0);

        mgkDiscView.setText("m="+m+" kg  mu="+mu+"  g="+g+" m/sec^2  k="+k+" N/m  Δx="+deltax+" m  l0="+FBRef.df.format(l0)+" m    v0="+FBRef.df.format(v0)+" m/sec");

        String[] list=new String[lList.length+1];
        list[0]="t(sec)    l(m)    v(m/sec)";

        for(int i=1; i<lList.length+1; i++)
        {
            String t=" ";
            String time=FBRef.df.format((double)i/100);
            t+=time+"      ";

            String l=FBRef.df.format(lList[i-1]);
            t+=l+"      ";

            String v=FBRef.df.format(vList[i-1]);
            t+=v;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsDiscLV.setAdapter(adp);

        changeLanguage();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public void plots(View view)
    {
        Intent si=new Intent(this,DiscPlots.class);
        si.putExtra("lList",lList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("v0",v0);
        si.putExtra("l0",l0);
        si.putExtra("deltax",deltax);
        startActivity(si);
    }

    public void back(View view)
    {
        Intent si=new Intent(this,MenuActivity.class);
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
        plotsDiscButton.setText(Languages.plots);
        menuDiscButton.setText(Languages.backToMenu);
        animationDiscButton.setText(Languages.backToAnimation);
    }

    public void animation(View view)
    {
        Intent si=new Intent(this,DiscActivity.class);

        si.putExtra("m",m);
        si.putExtra("mu",mu);
        si.putExtra("k",k);
        si.putExtra("shift",deltax);
        if(g==10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));
        si.putExtra("rerun",true);
        startActivity(si);
    }

    public int findPlanet(double g)
    {
        int n=-1;
        for(int i=0;i<Languages.gravity.length;i++)
        {
            if(Languages.gravity[i]==g)
            {
                n=i;
            }
        }
        return n;
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
        firstRow.createCell(0).setCellValue("t (sec)");
        firstRow.createCell(1).setCellValue("l (m)");
        firstRow.createCell(2).setCellValue("v (m/sec)");
        firstRow.createCell(5).setCellValue("m="+m+" kg  mu="+mu+"  g="+g+" m/sec^2  k="+k+" N/m  Δx="+deltax+" m  l0="+l0+" m    v0="+v0+" m/sec");

        for(int i=0;i<lList.length;i++)
        {
            HSSFRow row=sheet.createRow(i+1);
            row.createCell(0).setCellValue((double)i/100);
            row.createCell(1).setCellValue(lList[i]);
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