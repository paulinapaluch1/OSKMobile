package com.example.osk.ui.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.osk.R;

public class DrivingFragment extends Fragment {

    ListView listView;
    String[] name = {"Jan Nowak", "Sandra Kulik", "Weronika Waleczna", "Dominik Gnas"};
    String[] hours = {"6:00-8:00", "8:00-10:00", "10:00-12:00", "12:00-14:00"};
    String[] types = {"Jazda po mieście", "Plac", "Egzamin wewnętrzny", "Jazda po mieście"};
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.driving_fragment, container, false);
        listView = rootView.findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(getActivity(),name,hours,types);
        int alpha = 0;


        listView.setAdapter(adapter);

        return rootView;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rName[];
        String rHour[];
        String rType[];

        MyAdapter(Context c, String name[], String hour[], String type[]) {
            super(c, R.layout.row, R.id.textView, name);
            this.context = c;
            this.rName=name;
            this.rHour=hour;
            this.rType=type;

        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView name = row.findViewById(R.id.name);
            TextView hour = row.findViewById(R.id.hour);
            TextView type = row.findViewById(R.id.type);

            name.setText(rName[position]);
            hour.setText(rHour[position]);
            type.setText(rType[position]);

            return row;
        }
    }


}