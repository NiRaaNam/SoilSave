package com.gistda.niraanam.soilsave;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
public class NewMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMyLocationButtonClickListener,
        OnMyLocationClickListener, OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;


    public GoogleMap mMap;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;

    String mapLat, mapLong = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        }else if (id == R.id.about) {

            Intent i = new Intent(getApplicationContext(), NewActivityAbout.class);
            startActivity(i);
            finish();
        }else if (id == R.id.contact) {

            Intent i = new Intent(getApplicationContext(), NewActivityContact.class);
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
                    NewMainActivity.this);
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

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraIdleListener(this);

        enableMyLocation();

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);



        LatLng point1 = new LatLng(14.8769626884178, 102.121092907826);
        mMap.addMarker(new MarkerOptions().position(point1).title("Point 1").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(point1));


        LatLng point2 = new LatLng(14.8774668972348, 102.12056091032);
        mMap.addMarker(new MarkerOptions().position(point2).title("Point 2").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point3 = new LatLng(14.8774734933468, 102.121086126366);
        mMap.addMarker(new MarkerOptions().position(point3).title("Point 3").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point4 = new LatLng(14.8774800882595, 102.121611342671);
        mMap.addMarker(new MarkerOptions().position(point4).title("Point 4").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point5 = new LatLng(14.8779777019015, 102.120554127367);
        mMap.addMarker(new MarkerOptions().position(point5).title("Point 5").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point6 = new LatLng(14.8779842982501, 102.121079344645);
        mMap.addMarker(new MarkerOptions().position(point6).title("Point 6").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point7 = new LatLng(14.8779908933993, 102.121604562183);
        mMap.addMarker(new MarkerOptions().position(point7).title("Point 7").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point8 = new LatLng(14.8779974873491, 102.122129779981);
        mMap.addMarker(new MarkerOptions().position(point8).title("Point 8").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point9 = new LatLng(14.8780040800994, 102.122654998038);
        mMap.addMarker(new MarkerOptions().position(point9).title("Point 9").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point10 = new LatLng(14.8784885065424, 102.120547344154);
        mMap.addMarker(new MarkerOptions().position(point10).title("Point 10").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));


        LatLng point11 = new LatLng(14.8784951031277, 102.121072562666);
        mMap.addMarker(new MarkerOptions().position(point11).title("Point 11").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point12 = new LatLng(14.8785016985134, 102.121597781437);
        mMap.addMarker(new MarkerOptions().position(point12).title("Point 12").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point13 = new LatLng(14.8785082926997, 102.122123000467);
        mMap.addMarker(new MarkerOptions().position(point13).title("Point 13").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point14 = new LatLng(14.8785148856865, 102.122648219757);
        mMap.addMarker(new MarkerOptions().position(point14).title("Point 14").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point15 = new LatLng(14.8789927131363, 102.120015341196);
        mMap.addMarker(new MarkerOptions().position(point15).title("Point 15").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point16 = new LatLng(14.8789993111576, 102.120540560682);
        mMap.addMarker(new MarkerOptions().position(point16).title("Point 16").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point17 = new LatLng(14.8790059079795, 102.121065780426);
        mMap.addMarker(new MarkerOptions().position(point17).title("Point 17").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point18 = new LatLng(14.8790125036018, 102.12159100043);
        mMap.addMarker(new MarkerOptions().position(point18).title("Point 18").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point19 = new LatLng(14.8790190980246, 102.122116220694);
        mMap.addMarker(new MarkerOptions().position(point19).title("Point 19").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point20 = new LatLng(14.8790256912478, 102.122641441217);
        mMap.addMarker(new MarkerOptions().position(point20).title("Point 20").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point21 = new LatLng(14.8795035174891, 102.120008556231);
        mMap.addMarker(new MarkerOptions().position(point21).title("Point 21").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point22 = new LatLng(14.8795101157471, 102.12053377695);
        mMap.addMarker(new MarkerOptions().position(point22).title("Point 22").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point23 = new LatLng(14.8795167128056, 102.121058997927);
        mMap.addMarker(new MarkerOptions().position(point23).title("Point 23").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point24 = new LatLng(14.8795233086644, 102.121584219165);
        mMap.addMarker(new MarkerOptions().position(point24).title("Point 24").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point25 = new LatLng(14.8795299033237, 102.122109440662);
        mMap.addMarker(new MarkerOptions().position(point25).title("Point 25").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point26 = new LatLng(14.8795364967834, 102.122634662418);
        mMap.addMarker(new MarkerOptions().position(point26).title("Point 26").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point27 = new LatLng(14.8795430890435, 102.123159884434);
        mMap.addMarker(new MarkerOptions().position(point27).title("Point 27").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point28 = new LatLng(14.8800209203109, 102.120526992958);
        mMap.addMarker(new MarkerOptions().position(point28).title("Point 28").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point29 = new LatLng(14.8800275176059, 102.121052215169);
        mMap.addMarker(new MarkerOptions().position(point29).title("Point 29").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point30 = new LatLng(14.8800341137014, 102.12157743764);
        mMap.addMarker(new MarkerOptions().position(point30).title("Point 30").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point31 = new LatLng(14.8800407085971, 102.12210266037);
        mMap.addMarker(new MarkerOptions().position(point31).title("Point 31").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point32 = new LatLng(14.8800473022933, 102.12262788336);
        mMap.addMarker(new MarkerOptions().position(point32).title("Point 32").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point33 = new LatLng(14.8800538947899, 102.123153106608);
        mMap.addMarker(new MarkerOptions().position(point33).title("Point 33").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));


        LatLng point34 = new LatLng(14.8805383223806, 102.121045432151);
        mMap.addMarker(new MarkerOptions().position(point34).title("Point 34").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point35 = new LatLng(14.8805449187125, 102.121570655855);
        mMap.addMarker(new MarkerOptions().position(point35).title("Point 35").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point36 = new LatLng(14.8805515138448, 102.122095879819);
        mMap.addMarker(new MarkerOptions().position(point36).title("Point 36").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point37 = new LatLng(14.8805581077775, 102.122621104042);
        mMap.addMarker(new MarkerOptions().position(point37).title("Point 37").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point38 = new LatLng(14.8805647005104, 102.123146328524);
        mMap.addMarker(new MarkerOptions().position(point38).title("Point 38").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point39 = new LatLng(14.8810623190668, 102.122089099008);
        mMap.addMarker(new MarkerOptions().position(point39).title("Point 39").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point40 = new LatLng(14.8810689132359, 102.122614324464);
        mMap.addMarker(new MarkerOptions().position(point40).title("Point 40").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        LatLng point41 = new LatLng(14.8810755062053, 102.12313955018);
        mMap.addMarker(new MarkerOptions().position(point41).title("Point 41").icon(getBitmapDescriptor(R.drawable.ic_fiber_manual_record_black_24dp)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point18, 17));

    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) getDrawable(id);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
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




    }

    @Override
    public void onMapClick(LatLng point) {
        //mTapTextView.setText("tapped, point=" + point);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //mTapTextView.setText("long pressed, point=" + point);

        mMap.addMarker(new MarkerOptions().position(point).title("X: "+ point.latitude)
                .snippet("Y: "+ point.longitude));

        mapLat = String.valueOf(point.latitude);
        mapLong = String.valueOf(point.longitude);

        /*Intent intent = new Intent(getBaseContext(), NewMainActivityAdd.class);
        intent.putExtra("THE_LAT", mapLat);
        startActivity(intent);*/

        Intent intent = new Intent(this, NewMainActivityAdd.class);
        Bundle extras = new Bundle();
        extras.putString("THE_LAT",mapLat);
        extras.putString("THE_LON",mapLong);
        intent.putExtras(extras);
        startActivity(intent);


    }

    @Override
    public void onCameraIdle() {
       // mCameraTextView.setText(mMap.getCameraPosition().toString());
    }
}

