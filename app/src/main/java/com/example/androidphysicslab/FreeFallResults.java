package com.example.androidphysicslab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FreeFallResults extends AppCompatActivity
{
    ListView results;
    TextView mgView;
    Button plotsButton,menuButton,animationButton;
    AlertDialog.Builder adb;

    double[] hList,vList;
    double m,g;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final int digitsAfterDot=2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_fall_results);

        results=(ListView)findViewById(R.id.results);
        plotsButton=(Button)findViewById(R.id.plotsButton);
        menuButton=(Button)findViewById(R.id.menuButton);
        animationButton=(Button)findViewById(R.id.animationButton);
        mgView=(TextView)findViewById(R.id.mgView);
        changeLanguage();

        Intent gi=getIntent();
        hList=gi.getDoubleArrayExtra("hList");
        vList=gi.getDoubleArrayExtra("vList");
        m=gi.getDoubleExtra("m",0);
        g=gi.getDoubleExtra("g",0);
        mgView.setText("m="+m+" kg     "+"g="+g+" m/(sec^2)");

        Log.w("Tag",String.valueOf(hList==null));
        Log.w("TAG"," l="+hList.length);

        String[] list=new String[hList.length+1];
        list[0]="t(sec)    h(m)    v(m/sec)";

        for(int i=1; i<hList.length+1; i++)
        {
            String t=" ";
            String time=FBRef.df.format((double)(i-1)/100);
            t+=time+"      ";

            String h=FBRef.df.format(hList[i-1]);
            t+=h+"      ";

            String v=FBRef.df.format(vList[i-1]);
            t+=v;

            list[i]=t;
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        results.setAdapter(adp);
    }

    /**
     * @param view - the button pressed
     * @return - goes back to the plots activity
     */

    public void plots(View view)
    {
        Intent si=new Intent(this,FreeFallPlots.class);

        si.putExtra("hList",hList);
        si.putExtra("vList",vList);
        si.putExtra("g",g);
        si.putExtra("m",m);

        startActivity(si);
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
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        plotsButton.setText(Languages.plots);
        menuButton.setText(Languages.backToMenu);
        animationButton.setText(Languages.backToAnimation);
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
     * @param view - the button that the user pressed
     * @return - sends all the necessary data to the animation activity where the user can view the animation again.
     */

    public void animation(View view)
    {
        Intent si=new Intent(this, FreeFallActivity.class);

        si.putExtra("mass",m);
        si.putExtra("height",hList[0]);
        si.putExtra("rerun",true);
        if(g==-1) si.putExtra("planet",-1);
        else     si.putExtra("planet",findPlanet(g));

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
        firstRow.createCell(1).setCellValue("h (m)");
        firstRow.createCell(2).setCellValue("v (m/sec)");
        firstRow.createCell(5).setCellValue("m="+m+" kg    g="+g+"m/(sec^2)");

        for(int i=0;i<hList.length;i++)
        {
            HSSFRow row=sheet.createRow(i+1);
            row.createCell(0).setCellValue((double)i/100);
            row.createCell(1).setCellValue(hList[i]);
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