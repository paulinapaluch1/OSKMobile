package com.example.osk.ui.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.osk.R;
import com.example.osk.data.model.ListViewService;
import com.example.osk.model.TimetableJson;
import com.example.osk.remote.ApiUtils;
import com.example.osk.remote.UserService;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrivingFragment extends Fragment {

    View rootView;
    UserService userService;
    Integer id;
    private ListView listView ;
    private ArrayAdapter<String> adapter;
    ArrayList<TimetableJson> resObj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.driving_fragment, container, false);
        listView = rootView.findViewById(R.id.listView);
        userService= ApiUtils.getUserService();
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            id= (Integer)extras.get("id");
        }
        else id=1;
        getTimetable();
        ListViewService service = new ListViewService(resObj);
        MyAdapter adapter = new MyAdapter(getActivity(),service.getName(),service.getHours(),service.getTypes());
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
            LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                    .getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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




    public void  getTimetable(){
        Call<ArrayList<TimetableJson>> call = userService.getTodayTimetable(id);
        call.enqueue(new Callback<ArrayList<TimetableJson>>() {
            @Override
            public void onResponse(Call<ArrayList<TimetableJson>> call, Response<ArrayList<TimetableJson>> response) {
                if (response.isSuccessful()) {
                    resObj = response.body();
                  // int size= resObj.size();
                   // name = new String[size];
                   // hours= new String[size];
                   //  types = new String[size];
                 //   for(int i =0;i<size;i++){
                      // name[i] = resObj.get(i).getStudentNameAndSurname();
                       // hours[i] = resObj.get(i).getHours();
                       // types[i] = resObj.get(i).getType();
                  //  }
                        Toast.makeText(getActivity(),"Wystąpił błąd.",Toast.LENGTH_SHORT).show();
                    }else{
                    Toast.makeText(getActivity(),"Wystąpił błąd. Spróbuj ponownie",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TimetableJson>> call, Throwable t) {

            }


        });




    }

}