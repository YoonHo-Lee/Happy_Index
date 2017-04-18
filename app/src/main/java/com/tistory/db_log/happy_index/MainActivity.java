package com.tistory.db_log.happy_index;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt_GPS_Status;
    //GPS
    double GPS_latitude, GPS_longitude, GPS_altitude;
    float GPS_accuracy;
    String GPS_provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Toast.makeText(view.getContext(), "123", Toast.LENGTH_SHORT).show();
                txt_GPS_Status.setText(GPS_latitude+"°, "+GPS_longitude+"°");
                Log.d("test", GPS_latitude+"°, "+GPS_longitude+"°");

            }
        });// end of Floating Button



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //6.0부터 필요한 권한 체크하고 요청하기
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck==PackageManager.PERMISSION_DENIED) {
            //권한 없음
        }else {
            //권한 있음
        }




//       --GPS Part--
        View hView = navigationView.getHeaderView(0);
        final Switch switch_GPS = (Switch) hView.findViewById(R.id.switch_GPS);
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        txt_GPS_Status = (TextView) hView.findViewById(R.id.txt_GPS_Status);

        switch_GPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1,mLocationListener);
                        Log.d("test", "GPS ON!!!!!!!!!!!");
                    }catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    switch_GPS.setText("GPStrue");
                }
                else {
                    locationManager.removeUpdates(mLocationListener);
                    switch_GPS.setText("GPSfalse");
                }
            }
        });// end of GPS


        Fragment fragment = new HomeFragment();
        String title = "null";
        if (fragment != null) {

            title = getString(R.string.menu_home);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

    } // end of onCreate



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

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        Intent intent = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            title = getString(R.string.menu_home);

        } else if (id == R.id.nav_temperature) {
            fragment = new TempFragment();
            title = getString(R.string.menu_temperature);

        } else if (id == R.id.nav_air) {
            fragment = new AirFragment();
            title = getString(R.string.menu_air);

        } else if (id == R.id.nav_setting) {
            intent = new Intent(this, SettingActivity.class);

            Toast.makeText(this, "setting clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            intent = new Intent(this, HelpActivity.class);

            Toast.makeText(this, "help clicked", Toast.LENGTH_SHORT).show();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        if (intent != null) {
            startActivity(intent);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되며 이벤트 발생
            //값은 Location형태로 리턴되고 좌표 출력 방법은 아래와 같다

            Log.d("test","onLocationChanged, location:" + location);
            double GPS_latitude = location.getLatitude();//위도
            double GPS_longitude = location.getLongitude();//경도
            double GPS_altitude = location.getAltitude();//고도
            float GPS_accuracy = location.getAccuracy();//정확도
            String GPS_provider = location.getProvider();//위치제공자
                //GPS 위치제공자에 의한 위치변화 오차범위가 좁다
                //Network 위치제공자에 의한 위치변화
                //Network 위치는 GPS에 비해 정확도가 많이 떨어진다

            Log.d("test", "GPS_latitude : " + GPS_latitude + ", " + "GPS_longitude : " + GPS_longitude);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //변경시
            txt_GPS_Status.setText(R.string.GPS_info_Receiving);

            Log.d("test", "onStatusChanged, provider:" + s + ", status:" + i + " ,Bundle:" + bundle);
        }

        @Override
        public void onProviderEnabled(String s) {
            //Enabled시
            txt_GPS_Status.setText(GPS_latitude+"°, "+GPS_longitude+"°");

            Log.d("test", "onProviderEnabled, provider:" + s);
        }

        @Override
        public void onProviderDisabled(String s) {
            //Disabled시
            Log.d("test", "onProviderDisabled, provider:" + s);

        }
    };
}
