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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.osk.R;
import com.example.osk.model.LocationToSend;
import com.example.osk.model.Message;
import com.example.osk.remote.ApiUtils;
import com.example.osk.remote.UserService;
import com.example.osk.sqlite.DBManager;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLocation extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonStop;
    private Button buttonSendData;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    private EditText instructor;
    private DBManager dbManager;
    private UserService userService;
    private Integer currentLoggedInstructorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop);
        t = (TextView) findViewById(R.id.textView);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonSendData = findViewById(R.id.send);
        instructor = (EditText) findViewById(R.id.instructor);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final Button logoutButton = findViewById(R.id.logout);
        dbManager = new DBManager(this);
        dbManager.open();
        userService = ApiUtils.getUserService();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                Date date = new Date();
                dbManager.insert(String.valueOf(location.getLongitude()),
                        String.valueOf(location.getLatitude()), date.toString(), 0);
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

        configure_button();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            instructor.setText(instructor.getText() + " " + extras.get("instructor"));
            currentLoggedInstructorId = (Integer)extras.get("id");
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = dbManager.getDatabase().rawQuery("select * from gpspoints ", null);
                if (c.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Brak danych", Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                while (c.moveToNext()) {
                    buffer.append("id:" + c.getString(0) + " \n");
                    buffer.append("location 1: " + c.getString(1) + " \n");
                    buffer.append("location 2: " + c.getString(2) + " \n");
                    buffer.append("time: " + c.getString(3) + " \n");
                    buffer.append("sent: " + c.getString(4) + " \n");
                }
                Toast.makeText(getApplicationContext(),buffer.toString(), Toast.LENGTH_LONG).show();
            }
        });

        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList pointsToSend = getGpsPointsToSend();

                Call<Message> call = userService.sendCoordinates(pointsToSend,1);//currentLoggedInstructorId);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()) {
                            Message resObj = response.body();
                            if(resObj.getMessage().equals("true")){
                                Toast.makeText(getApplicationContext(), "Przesłano dane ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(GetLocation.this,"Nie zapisano",Toast.LENGTH_SHORT).show();
                            }}else{
                            Toast.makeText(GetLocation.this,"Wystąpił błąd. Spróbuj ponownie",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(GetLocation.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    @Override
        public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case 10:
                    configure_button();
                    break;
                default:
                    break;
            }
        }

        void configure_button () {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            buttonStart.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager
                            .requestLocationUpdates("gps", 5000,
                                    0, listener);
                }
            });
        }
    }

