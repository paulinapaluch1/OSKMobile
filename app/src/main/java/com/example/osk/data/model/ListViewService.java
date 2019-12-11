package com.example.osk.data.model;

import com.example.osk.model.TimetableJson;

import java.util.ArrayList;

public class ListViewService {

    String[] name = {"Jan Nowak", "Sandra Kulik", "Weronika Waleczna", "Dominik Gnas"};
    String[] hours = {"6:00-8:00", "8:00-10:00", "10:00-12:00", "12:00-14:00"};
    String[] types = {"Jazda po mieście", "Plac", "Egzamin wewnętrzny", "Jazda po mieście"};
    ArrayList<TimetableJson> timetable;

    public ListViewService(ArrayList<TimetableJson> resObj) {
        this.timetable = resObj;

    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getHours() {
        return hours;
    }

    public void setHours(String[] hours) {
        this.hours = hours;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
