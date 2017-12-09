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
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMyLocationButtonClickListener,
        OnMyLocationClickListener, OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;

    public TabHost myTabHost;

    public GoogleMap mMap;

    // Latitude & Longitude
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    ArrayList<HashMap<String, String>> location = null;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;

    EditText EdtNum,Value1, Value2, Value3, Value4, Value5, Value6, Value7, Value8;
    String sNumMark;
    String Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder, Value8Holder;
    Button SendtoServ;

    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "http://150.107.31.104/soil_collecting/AddSoilData.php";

    final Context context = this;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EdtNum = (EditText) findViewById(R.id.edtNumpoint);
        Value1 = (EditText) findViewById(R.id.edtVal1);
        Value1 = (EditText) findViewById(R.id.edtVal1);
        Value2 = (EditText) findViewById(R.id.edtVal2);
        Value3 = (EditText) findViewById(R.id.edtVal3);
        Value4 = (EditText) findViewById(R.id.edtVal4);
        Value5 = (EditText) findViewById(R.id.edtVal5);
        Value6 = (EditText) findViewById(R.id.edtVal6);
        Value7 = (EditText) findViewById(R.id.edtVal7);
        Value8 = (EditText) findViewById(R.id.edtVal8);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //*** Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Set Tabhost Activity
        myTabHost = (TabHost) findViewById(R.id.myTabHost);
        myTabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = myTabHost.newTabSpec("Add Soil Data");
        tabSpec.setIndicator("Add Soil Data",
                getResources().getDrawable(R.drawable.ic_menu_camera));
        tabSpec.setContent(R.id.tab1_all_ref);
        myTabHost.addTab(tabSpec);

        tabSpec = myTabHost.newTabSpec("Map View");
        tabSpec.setIndicator("Map View",
                getResources().getDrawable(R.drawable.ic_menu_camera));
        tabSpec.setContent(R.id.tab2_all_ref);
        myTabHost.addTab(tabSpec);

        myTabHost.setCurrentTab(1);

        //Set All Floating Activity
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fabBGLayout = findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click for Manage The Locations

                Toast.makeText(getApplicationContext(), "Manage Locations Clicked", Toast.LENGTH_SHORT).show();
                closeFABMenu();

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click for Base Map
                Toast.makeText(getApplicationContext(), "Base Map Clicked", Toast.LENGTH_SHORT).show();
                closeFABMenu();
            }
        });


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

                SendToServerClass(sNumMark, Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder, Value8Holder);
            }
        });

    }

    //Class for Send Data To Server
    public void SendToServerClass(final String S_num_mark,final String S_Value1, final String S_Value2, final String S_Value3, final String S_Value4, final String S_Value5, final String S_Value6, final String S_Value7, final String S_Value8) {

        class TheSendToServerClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();


                //Clear EditTexet Fill after finished sending
                Value1.setText(null); Value2.setText(null); Value3.setText(null); Value4.setText(null);
                Value5.setText(null); Value6.setText(null); Value7.setText(null); Value8.setText(null);

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

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        TheSendToServerClass AddSoilDataClass = new TheSendToServerClass();

        AddSoilDataClass.execute(S_num_mark, S_Value1, S_Value2, S_Value3, S_Value4, S_Value5, S_Value6, S_Value7, S_Value8);



    }


    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
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

        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }

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

            Intent i = new Intent(getApplicationContext(), ShowAllSoilActivity.class);
            startActivity(i);
            finish();


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
                    MainActivity.this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        enableMyLocation();

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng point1 = new LatLng(14.8769626884178, 102.121092907826);
        mMap.addMarker(new MarkerOptions().position(point1).title("Point 1"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(point1));


        LatLng point2 = new LatLng(14.8774668972348, 102.12056091032);
        mMap.addMarker(new MarkerOptions().position(point2).title("Point 2"));

        LatLng point3 = new LatLng(14.8774734933468, 102.121086126366);
        mMap.addMarker(new MarkerOptions().position(point3).title("Point 3"));

        LatLng point4 = new LatLng(14.8774800882595, 102.121611342671);
        mMap.addMarker(new MarkerOptions().position(point4).title("Point 4"));

        LatLng point5 = new LatLng(14.8779777019015, 102.120554127367);
        mMap.addMarker(new MarkerOptions().position(point5).title("Point 5"));

        LatLng point6 = new LatLng(14.8779842982501, 102.121079344645);
        mMap.addMarker(new MarkerOptions().position(point6).title("Point 6"));

        LatLng point7 = new LatLng(14.8779908933993, 102.121604562183);
        mMap.addMarker(new MarkerOptions().position(point7).title("Point 7"));

        LatLng point8 = new LatLng(14.8779974873491, 102.122129779981);
        mMap.addMarker(new MarkerOptions().position(point8).title("Point 8"));

        LatLng point9 = new LatLng(14.8780040800994, 102.122654998038);
        mMap.addMarker(new MarkerOptions().position(point9).title("Point 9"));

        LatLng point10 = new LatLng(14.8784885065424, 102.120547344154);
        mMap.addMarker(new MarkerOptions().position(point10).title("Point 10"));


        LatLng point11 = new LatLng(14.8784951031277, 102.121072562666);
        mMap.addMarker(new MarkerOptions().position(point11).title("Point 11"));

        LatLng point12 = new LatLng(14.8785016985134, 102.121597781437);
        mMap.addMarker(new MarkerOptions().position(point12).title("Point 12"));

        LatLng point13 = new LatLng(14.8785082926997, 102.122123000467);
        mMap.addMarker(new MarkerOptions().position(point13).title("Point 13"));

        LatLng point14 = new LatLng(14.8785148856865, 102.122648219757);
        mMap.addMarker(new MarkerOptions().position(point14).title("Point 14"));

        LatLng point15 = new LatLng(14.8789927131363, 102.120015341196);
        mMap.addMarker(new MarkerOptions().position(point15).title("Point 15"));

        LatLng point16 = new LatLng(14.8789993111576, 102.120540560682);
        mMap.addMarker(new MarkerOptions().position(point16).title("Point 16"));

        LatLng point17 = new LatLng(14.8790059079795, 102.121065780426);
        mMap.addMarker(new MarkerOptions().position(point17).title("Point 17"));

        LatLng point18 = new LatLng(14.8790125036018, 102.12159100043);
        mMap.addMarker(new MarkerOptions().position(point18).title("Point 18"));

        LatLng point19 = new LatLng(14.8790190980246, 102.122116220694);
        mMap.addMarker(new MarkerOptions().position(point19).title("Point 19"));

        LatLng point20 = new LatLng(14.8790256912478, 102.122641441217);
        mMap.addMarker(new MarkerOptions().position(point20).title("Point 20"));

        LatLng point21 = new LatLng(14.8795035174891, 102.120008556231);
        mMap.addMarker(new MarkerOptions().position(point21).title("Point 21"));

        LatLng point22 = new LatLng(14.8795101157471, 102.12053377695);
        mMap.addMarker(new MarkerOptions().position(point22).title("Point 22"));

        LatLng point23 = new LatLng(14.8795167128056, 102.121058997927);
        mMap.addMarker(new MarkerOptions().position(point23).title("Point 23"));

        LatLng point24 = new LatLng(14.8795233086644, 102.121584219165);
        mMap.addMarker(new MarkerOptions().position(point24).title("Point 24"));

        LatLng point25 = new LatLng(14.8795299033237, 102.122109440662);
        mMap.addMarker(new MarkerOptions().position(point25).title("Point 25"));

        LatLng point26 = new LatLng(14.8795364967834, 102.122634662418);
        mMap.addMarker(new MarkerOptions().position(point26).title("Point 26"));

        LatLng point27 = new LatLng(14.8795430890435, 102.123159884434);
        mMap.addMarker(new MarkerOptions().position(point27).title("Point 27"));

        LatLng point28 = new LatLng(14.8800209203109, 102.120526992958);
        mMap.addMarker(new MarkerOptions().position(point28).title("Point 28"));

        LatLng point29 = new LatLng(14.8800275176059, 102.121052215169);
        mMap.addMarker(new MarkerOptions().position(point29).title("Point 29"));

        LatLng point30 = new LatLng(14.8800341137014, 102.12157743764);
        mMap.addMarker(new MarkerOptions().position(point30).title("Point 30"));

        LatLng point31 = new LatLng(14.8800407085971, 102.12210266037);
        mMap.addMarker(new MarkerOptions().position(point31).title("Point 31"));

        LatLng point32 = new LatLng(14.8800473022933, 102.12262788336);
        mMap.addMarker(new MarkerOptions().position(point32).title("Point 32"));

        LatLng point33 = new LatLng(14.8800538947899, 102.123153106608);
        mMap.addMarker(new MarkerOptions().position(point33).title("Point 33"));


        LatLng point34 = new LatLng(14.8805383223806, 102.121045432151);
        mMap.addMarker(new MarkerOptions().position(point34).title("Point 34"));

        LatLng point35 = new LatLng(14.8805449187125, 102.121570655855);
        mMap.addMarker(new MarkerOptions().position(point35).title("Point 35"));

        LatLng point36 = new LatLng(14.8805515138448, 102.122095879819);
        mMap.addMarker(new MarkerOptions().position(point36).title("Point 36"));

        LatLng point37 = new LatLng(14.8805581077775, 102.122621104042);
        mMap.addMarker(new MarkerOptions().position(point37).title("Point 37"));

        LatLng point38 = new LatLng(14.8805647005104, 102.123146328524);
        mMap.addMarker(new MarkerOptions().position(point38).title("Point 38"));

        LatLng point39 = new LatLng(14.8810623190668, 102.122089099008);
        mMap.addMarker(new MarkerOptions().position(point39).title("Point 39"));

        LatLng point40 = new LatLng(14.8810689132359, 102.122614324464);
        mMap.addMarker(new MarkerOptions().position(point40).title("Point 40"));

        LatLng point41 = new LatLng(14.8810755062053, 102.12313955018);
        mMap.addMarker(new MarkerOptions().position(point41).title("Point 41"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point18, 17));



        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);*/

       /* String url = "http://150.107.31.104/soil_collecting/getLatLon_map.php";
        try {

            JSONArray data = new JSONArray(getHttpGet(url));

            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("id", c.getString("id"));
                map.put("num_mark", c.getString("num_mark"));
                map.put("lat", c.getString("lat"));
                map.put("lon", c.getString("lon"));
                location.add(map);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // *** Focus & Zoom
        Latitude = Double.parseDouble(location.get(0).get("lat").toString());
        Longitude = Double.parseDouble(location.get(0).get("lon").toString());
        LatLng coordinate = new LatLng(Latitude, Longitude);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));

        // *** Marker (Loop)
        for (int i = 0; i < location.size(); i++) {
            Latitude = Double.parseDouble(location.get(i).get("lat").toString());
            Longitude = Double.parseDouble(location.get(i).get("lon").toString());
            String name = location.get(i).get("num_mark").toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Point: " + name);
            mMap.addMarker(marker);
        }
*/
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        /*Double my_lat = Double.parseDouble(Lat_Map);
        Double my_lon = Double.parseDouble(Long_Map);
        LatLng my_location = new LatLng(my_lat,my_lon);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(my_location));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        myMap.addMarker(new MarkerOptions().position(my_location).title(Name_Map));*/



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

// set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                EdtNum.setText(userInput.getText());
                                myTabHost.setCurrentTab(0);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();








    }
}
