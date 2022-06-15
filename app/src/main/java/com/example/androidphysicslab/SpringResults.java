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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SpringResults extends AppCompatActivity
{
    TextView mgkView;
    ListView resultsSpringLV;
    Button plotsSpringButton,menuSpringButton,animationSpringButton,xlButton;
    double[] xList,vList,aList;
    double m,g,k,amplitude,periods;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_results);

        mgkView=(TextView)findViewById(R.id.mgkView);
        resultsSpringLV=(ListView) findViewById(R.id.resultsSpringLV);
        plotsSpringButton=(Button)findViewById(R.id.plotsSpringButton);
        menuSpringButton=(Button)findViewById(R.id.menuSpringButton);
        animationSpringButton=(Button)findViewById(R.id.animationSpringButton);
        xlButton=(Button)findViewById(R.id.xlButton);
        changeLanguage();

        Intent gi=getIntent();
        xList=gi.getDoubleArrayExtra("xList");
        vList=gi.getDoubleArrayExtra("vList");
        aList=gi.getDoubleArrayExtra("aList");
        m=gi.getDoubleExtra("m",0);
        g=gi.getDoubleExtra("g",0);
        k=gi.getDoubleExtra("k",0);
        amplitude=gi.getDoubleExtra("amplitude",0);
        periods=gi.getDoubleExtra("periods",0);
        mgkView.setText("m="+m+" kg     "+"g="+g+" m/(sec^2)\nk="+k+" N/m");

        String[] list=new String[xList.length+1];
        list[0]="t(sec)    x(m)    v(m/sec)  a(m/sec^2)";

        for(int i=1; i<xList.length+1; i++)
        {
            String t=" ";
            String time=FBRef.df.format((double)(i-1)/100);
            t+=time+"      ";

            String x=FBRef.df.format(xList[i-1]);
            t+=x+"      ";

            String v=FBRef.df.format(vList[i-1]);
            t+=v+"        ";

            String a=FBRef.df.format(aList[i-1]);
            t+=a;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        resultsSpringLV.setAdapter(adp);
    }

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        plotsSpringButton.setText(Languages.plots);
        menuSpringButton.setText(Languages.backToMenu);
        animationSpringButton.setText(Languages.backToAnimation);
        xlButton.setText(Languages.createExcel);
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
        Intent si=new Intent(this,SpringPlots.class);

        si.putExtra("xList",xList);
        si.putExtra("vList",vList);
        si.putExtra("aList",aList);
        si.putExtra("g",g);
        si.putExtra("m",m);
        si.putExtra("k",k);
        si.putExtra("amplitude",amplitude);
        si.putExtra("periods",periods);

        startActivity(si);
    }

    /**
     * @param view - the button that the user pressed
     * @return - sends all the necessary data to the animation activity where the user can view the animation again.
     */

    public void animation(View view)
    {
        Intent si=new Intent(this, SpringActivity.class);

        si.putExtra("mass",m);
        si.putExtra("k",k);
        si.putExtra("amplitude",amplitude);
        si.putExtra("periods",periods);
        if(g==10) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));
        si.putExtra("rerun",true);
        startActivity(si);
    }

    /**
     * @param g - the gravity acceleation of a planet in the solar system
     * @return - returns which planet has the specified gravity acceleration. If none has, returns -1 which later on will mean g=10
     */

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
        firstRow.createCell(0).setCellValue("t (sec)");
        firstRow.createCell(1).setCellValue("x (m)");
        firstRow.createCell(2).setCellValue("v (m/sec)");
        firstRow.createCell(3).setCellValue("a (m/sec^2)");
        firstRow.createCell(6).setCellValue("m="+m+" kg    k="+k+"N/m    g="+g+"m/(sec^2)");

        for(int i=0;i<xList.length;i++)
        {
            HSSFRow row=sheet.createRow(i+1);
            row.createCell(0).setCellValue((double)i/100);
            row.createCell(1).setCellValue(xList[i]);
            row.createCell(2).setCellValue(vList[i]);
            row.createCell(3).setCellValue(aList[i]);
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