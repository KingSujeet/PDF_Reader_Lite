package com.fikkarnot.pdfreaderlite;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    // variables and instances of class
    ListView listView;
    public static ArrayList<File> fileList = new ArrayList<>();
    PDFAdapter pdfAdapter;
    public  static int REQUEST_PERMISSION = 1;
    boolean boolean_permission;
    File dir;
    MenuItem checkable;
    boolean check;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing Instances
        listView = findViewById(R.id.listview_pdf);
        searchView = findViewById(R.id.searchView);
        //getting storage
        dir = new File(new File(String.valueOf(Environment.getExternalStorageDirectory())).toString());
        permission_fn();

        // adding onclickListnenr to Listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),PDFViewerActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        // adding onQuery listener to searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                pdfAdapter.getFilter().filter(newText);


                try {
                    ArrayList<File> result = new ArrayList<>();

                    for (File f : fileList) {

                        if (f.getName().contains(newText)) {

                            result.add(f);
                        }

                    }

                    ((PDFAdapter) listView.getAdapter()).update(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });



    }
// this method will check permission
    private void permission_fn() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))){



            }else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
            }
        }else {

            boolean_permission = true;
            getfile(dir);
            pdfAdapter = new PDFAdapter(getApplicationContext(), fileList);
            listView.setAdapter(pdfAdapter);
            Toast.makeText(MainActivity.this,"Please wait your files is loading",Toast.LENGTH_LONG).show();


        }


    }
    // will get Permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION){

            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                boolean_permission = true;
                getfile(dir);
                pdfAdapter = new PDFAdapter(getApplicationContext(), fileList);
                listView.setAdapter(pdfAdapter);

            }
            else {

                Toast.makeText(getApplicationContext(), "Please Allow the Permission", Toast.LENGTH_SHORT).show();
            }
        }


    }

    // getting  Pdf file from device and show it to listView
    private ArrayList<File> getfile(File dir) {

        File listFile[] = dir.listFiles();

        if (listFile!=null && listFile.length>0){

            for (int i=0;i<listFile.length;i++){

                if (listFile[i].isDirectory()){

                    getfile(listFile[i]);
                }else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")){

                        for (int j =0; j<fileList.size();j++){

                            if (fileList.get(j).getName().equals(listFile[i].getName())){

                                booleanpdf = true;
                            }else{


                            }
                        }

                        if (booleanpdf){

                            booleanpdf = false;
                        }else{

                            fileList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    // creating menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_xm,menu);

        checkable = menu.findItem(R.id.dark_mode);

        SharedPreferences preferences = this.getSharedPreferences("checkState", Context.MODE_PRIVATE);
        check= preferences.getBoolean("check",false);

        checkable.setChecked(check);


        return true;
    }

    // click listener method
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {


        switch (item.getItemId()){

            case R.id.dark_mode:
//                isChecked = !item.isChecked();
//                item.setChecked(isChecked);

                    if (item.isChecked()) {
                        SharedPreferences preferences = this.getSharedPreferences("darkMode", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("key", false);
                        editor.commit();



                    }
                    else{

                        SharedPreferences preferences = this.getSharedPreferences("darkMode", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("key",true);
                        editor.commit();



                    }

                    if (item.isChecked())
                    {

                        SharedPreferences preferences1 = this.getSharedPreferences("checkState", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putBoolean("check", false);
                        editor1.commit();
                        checkable.setChecked(false);
                    }else {

                        SharedPreferences preferences1 = this.getSharedPreferences("checkState", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putBoolean("check", true);
                        editor1.commit();
                        checkable.setChecked(true);
                    }


                return true;
                default:
                    return false;
        }


    }
}
