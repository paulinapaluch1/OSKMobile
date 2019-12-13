package com.example.osk.ui.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.osk.R;
import com.example.osk.ui.login.GetLocation;
import com.example.osk.ui.login.LoginActivity;

public class ProfileFragment extends Fragment {

    Button logoutButton;
    TextView average;
    TextView instructor;
    TextView email;
    TextView phone;
    TextView login;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        logoutButton = rootView.findViewById(R.id.logoutButton);
        average = rootView.findViewById(R.id.average);
        instructor = rootView.findViewById(R.id.instructor);
        email = rootView.findViewById(R.id.email);
        login = rootView.findViewById(R.id.login);
        phone = rootView.findViewById(R.id.phone);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            average.setText("Średnia ocen: " + extras.get("average"));
            instructor.setText((String)extras.get("instructor"));
            email.setText((String)extras.get("email"));
            login.setText((String)extras.get("login"));
            phone.setText((String)extras.get("phone"));
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }


}