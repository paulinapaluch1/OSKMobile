package com.example.osk.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.material.tabs.TabLayout;

public class GetLocation extends FragmentActivity {


    Location locationn;
    private TextView t;
    private LocationManager locationManager;
    private DBManager dbManager;
    private UserService userService;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        t = (TextView) findViewById(R.id.textView);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //final Button logoutButton = findViewById(R.id.logout);
        dbManager = new DBManager(this);
        dbManager.open();
        userService = ApiUtils.getUserService();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        setContentView(R.layout.activity_start_stop);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        adapter.addFrag(new LocationFr(), "Mapa");
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

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.bringToFront();

    }


}

