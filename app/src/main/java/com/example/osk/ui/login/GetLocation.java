package com.example.osk.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.osk.R;
import com.example.osk.remote.ApiUtils;
import com.example.osk.remote.UserService;
import com.example.osk.sqlite.DBManager;
import com.example.osk.ui.login.ui.DrivingFragment;
import com.example.osk.ui.login.ui.ProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

public class GetLocation extends FragmentActivity implements OnMapReadyCallback {

    //private Button buttonStop;
    // private Button buttonSendData;
    Location locationn;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    private EditText instructor;
    private DBManager dbManager;
    private UserService userService;
    private Integer currentLoggedInstructorId;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code = 101;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        t = (TextView) findViewById(R.id.textView);
        //final Button buttonStart = (Button) findViewById(R.id.buttonStart);
        //buttonStop = (Button) findViewById(R.id.buttonStop);
        // final Button buttonSendData = findViewById(R.id.send);
        //instructor = (EditText) findViewById(R.id.instructor);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //final Button logoutButton = findViewById(R.id.logout);
        dbManager = new DBManager(this);
        dbManager.open();
        userService = ApiUtils.getUserService();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
       // getLastLocation();



        configure_button();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // instructor.setText(instructor.getText() + " " + extras.get("instructor"));
            currentLoggedInstructorId = (Integer) extras.get("id");
        }
/*

*/
      /*  buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList pointsToSend = getGpsPointsToSend();

                Call<Message> call = userService.sendCoordinates(pointsToSend, 1);//currentLoggedInstructorId);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()) {
                            Message resObj = response.body();
                            if (resObj.getMessage().equals("true")) {
                                Toast.makeText(getApplicationContext(), "Przesłano dane ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(GetLocation.this, "Nie zapisano", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GetLocation.this, "Wystąpił błąd. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(GetLocation.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }); */
        setContentView(R.layout.activity_start_stop);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        // LocationFragment fragment = (LocationFragment) getSupportFragmentManager().findFragmentById(R.id.location);

        adapter.addFrag(new Test(), "Mapa");
        adapter.addFrag(new DrivingFragment(), "Grafik");
        adapter.addFrag(new ProfileFragment(), "Profil");

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Lokalizacja");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.location, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Grafik");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.calendar, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Profil");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.profile, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locationn = location;
                  final  SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
                    supportMapFragment.getMapAsync(GetLocation.this);



                }
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(locationn.getLatitude(), locationn.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
        googleMap.addMarker(markerOptions);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            case Request_Code:
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    getLastLocation();
                }
                break;
            default:
                break;
        }
    }

    private void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
    }
}

