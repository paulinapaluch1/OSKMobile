package com.example.osk.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.osk.R;
import com.example.osk.model.LocationToSend;
import com.example.osk.model.Message;
import com.example.osk.remote.ApiUtils;
import com.example.osk.remote.UserService;
import com.example.osk.sqlite.DBManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFr extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location locationn;
    private LocationManager locationManager;
    private LocationListener listener;
    private DBManager dbManager;
    private UserService userService;
    private Integer currentLoggedInstructorId;
    private Button buttonSendData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        buttonSendData = rootView.findViewById(R.id.send);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        userService = ApiUtils.getUserService();
        getLastLocation();
        Button buttonStart = rootView.findViewById(R.id.buttonStart);
        Button buttonStop = rootView.findViewById(R.id.buttonStop);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            currentLoggedInstructorId = (Integer)extras.get("id");
        }

        dbManager = new DBManager(getActivity().getBaseContext());
        dbManager.open();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Date date = new Date();
                dbManager.insert(String.valueOf(location.getLongitude()),
                        String.valueOf(location.getLatitude()), date.toString(), 0);
                LatLng latLng = new LatLng(locationn.getLatitude(), locationn.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("osk");
               if(mMap!=null) {
                   mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
                   mMap.addMarker(markerOptions);
               }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Rozpoczęto zapis współrzędnych", Toast.LENGTH_SHORT).show();

                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager
                        .requestLocationUpdates("gps", 5000,
                                0, listener);
            }
        });

        buttonStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Cursor c = dbManager.getDatabase().rawQuery("select * from gpspoints ", null);
                if (c.getCount() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Brak danych", Toast.LENGTH_LONG).show();
                    return true;
                }
                StringBuilder buffer = new StringBuilder();
                while (c.moveToNext()) {
                    buffer.append("id:" + c.getString(0) + " \n");
                    buffer.append("location 1: " + c.getString(1) + " \n");
                    buffer.append("location 2: " + c.getString(2) + " \n");
                    buffer.append("time: " + c.getString(3) + " \n");
                    buffer.append("sent: " + c.getString(4) + " \n");
                }
                Toast.makeText(getActivity().getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Zatrzymano zapis współrzędnych", Toast.LENGTH_SHORT).show();
                dbManager.close();
            }
        });


        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList pointsToSend = getGpsPointsToSend();
                Call<Message> call = userService.sendCoordinates(pointsToSend, currentLoggedInstructorId);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()) {
                            Message resObj = response.body();
                            if (resObj.getMessage().equals("true")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Przesłano dane ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Nie zapisano", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Wystąpił błąd. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    LatLng latLng = new LatLng(locationn.getLatitude(), locationn.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("osk");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
                    mMap.addMarker(markerOptions);
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    if(locationn!=null) {
    LatLng latLng = new LatLng(locationn.getLatitude(), locationn.getLongitude());
    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("OSK");
    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
    mMap.addMarker(markerOptions);
}
    }


    private ArrayList getGpsPointsToSend() {
        String selectQuery = " select * from gpspoints where sent = 0";
        Cursor cursor = dbManager.getReadableDatabase().rawQuery(selectQuery, null, null);
        ArrayList pointsToSend = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                LocationToSend location = new LocationToSend();
                location.setNs(cursor.getString(cursor.getColumnIndex("ns")));
                location.setNw(cursor.getString(cursor.getColumnIndex("nw")));
                location.setTime(cursor.getString(cursor.getColumnIndex("time")));
                location.setSent(cursor.getInt(cursor.getColumnIndex("sent")));
                pointsToSend.add(location);

            } while (cursor.moveToNext());
        }
        cursor.close();
        dbManager.getDatabase().close();
        return pointsToSend;
    }





}