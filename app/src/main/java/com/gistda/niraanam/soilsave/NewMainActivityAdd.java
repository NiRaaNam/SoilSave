package com.gistda.niraanam.soilsave;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.animation.Animator;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NewMainActivityAdd extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ActivityCompat.OnRequestPermissionsResultCallback {


    EditText EdtNum,EdtLat,EdtLon,Value1, Value2, Value3, Value4, Value5, Value6, Value7, Value8;
    String sNumMark,sLat,sLon;
    String Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder, Value8Holder;
    Button SendtoServ;

    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "http://150.107.31.104/soil_collecting/AddSoilData.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String txtLat = extras.getString("THE_LAT");
        String txtLon = extras.getString("THE_LON");

        EdtNum = (EditText) findViewById(R.id.edtNumpoint);
        EdtLat = (EditText) findViewById(R.id.edtLat);
        EdtLon = (EditText) findViewById(R.id.edtLong);
        Value1 = (EditText) findViewById(R.id.edtVal1);
        Value1 = (EditText) findViewById(R.id.edtVal1);
        Value2 = (EditText) findViewById(R.id.edtVal2);
        Value3 = (EditText) findViewById(R.id.edtVal3);
        Value4 = (EditText) findViewById(R.id.edtVal4);
        Value5 = (EditText) findViewById(R.id.edtVal5);
        Value6 = (EditText) findViewById(R.id.edtVal6);
        Value7 = (EditText) findViewById(R.id.edtVal7);
        Value8 = (EditText) findViewById(R.id.edtVal8);

        EdtLat.setText(txtLat);
        EdtLon.setText(txtLon);

        SendtoServ = (Button) findViewById(R.id.btnSend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //*** Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        // spinner1 from Server
        /*final Spinner spin = (Spinner) findViewById(R.id.simplespinner);

        String spinurl = "http://150.107.31.104/soil_collecting/getLatLon_spinner.php";

        try {

            JSONArray data = new JSONArray(getHttpGet(spinurl));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("id", c.getString("id"));
                map.put("num_mark", c.getString("num_mark"));

                MyArrList.add(map);

            }
            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(MainActivity.this, MyArrList, R.layout.point_num_mysql,
                    new String[]{"num_mark"}, new int[]{R.id.ColForNumSQL});
            spin.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
                                           int position, long id) {

                    //For get The Number of point that want to saving for DB MySQL
                    sNumMark = MyArrList.get(position).get("num_mark")
                            .toString();

                    *//*String sNumMark = MyArrList.get(position).get("num_mark")
                            .toString();

                    Toast.makeText(getApplicationContext(), sName + "is Clicked", Toast.LENGTH_SHORT).show();*//*

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    *//*Toast.makeText(MainActivity.this,
                            "Your Selected : Nothing",
                            Toast.LENGTH_SHORT).show();*//*
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/




        //Button Data to Server
        SendtoServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sNumMark = EdtNum.getText().toString();

                Value1Holder = Value1.getText().toString();
                Value2Holder = Value2.getText().toString();
                Value3Holder = Value3.getText().toString();
                Value4Holder = Value4.getText().toString();
                Value5Holder = Value5.getText().toString();
                Value6Holder = Value6.getText().toString();
                Value7Holder = Value7.getText().toString();
                Value8Holder = Value8.getText().toString();

                sLat = EdtLat.getText().toString();
                sLon = EdtLon.getText().toString();

                SendToServerClass(sNumMark, Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder, Value8Holder,sLat,sLon);
            }
        });

    }


    //Class for Send Data To Server
    public void SendToServerClass(final String S_num_mark,final String S_Value1, final String S_Value2, final String S_Value3, final String S_Value4, final String S_Value5, final String S_Value6, final String S_Value7, final String S_Value8,final String S_lat,final String S_lon) {

        class TheSendToServerClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NewMainActivityAdd.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(NewMainActivityAdd.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();


                //Clear EditTexet Fill after finished sending
                Value1.setText(null); Value2.setText(null); Value3.setText(null); Value4.setText(null);
                Value5.setText(null); Value6.setText(null); Value7.setText(null); Value8.setText(null);
                EdtLat.setText(null);EdtLon.setText(null);

                /*Intent i = new Intent(getApplicationContext(), NewMainActivity.class);
                startActivity(i);*/
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("add_num_mark", params[0]);

                hashMap.put("add_value1", params[1]);

                hashMap.put("add_value2", params[2]);

                hashMap.put("add_value3", params[3]);

                hashMap.put("add_value4", params[4]);

                hashMap.put("add_value5", params[5]);

                hashMap.put("add_value6", params[6]);

                hashMap.put("add_value7", params[7]);

                hashMap.put("add_value8", params[8]);

                hashMap.put("add_lat", params[9]);

                hashMap.put("add_lon", params[10]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        TheSendToServerClass AddSoilDataClass = new TheSendToServerClass();

        AddSoilDataClass.execute(S_num_mark, S_Value1, S_Value2, S_Value3, S_Value4, S_Value5, S_Value6, S_Value7, S_Value8,S_lat,S_lon);



    }


    //Set Helper of WerServer Side
    public static String getHttpGet(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else*/ if (id == R.id.nav_share) {



        } else if (id == R.id.nav_send) {

            /*Intent i = new Intent(getApplicationContext(), ShowAllSoilActivity.class);
            startActivity(i);
            finish();*/
        }else if (id == R.id.about) {

            /*Intent i = new Intent(getApplicationContext(), NewActivityAbout.class);
            startActivity(i);
            finish();*/
        }else if (id == R.id.contact) {

            /*Intent i = new Intent(getApplicationContext(), NewActivityContact.class);
            startActivity(i);
            finish();*/


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Set Back Button for Safe to Out of Application
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(
                    NewMainActivityAdd.this);
            // builder1.setTitle("");
            builder1.setMessage("Quit Application ?");
            builder1.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            builder1.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            builder1.show();

        }
        return super.onKeyDown(keyCode, event);
    }
}
