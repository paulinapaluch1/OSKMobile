package com.example.osk.model;


public class TimetableJson {

    private String studentNameAndSurname;
    private String hours;
    private String type;
    private String date;

    public TimetableJson(String studentNameAndSurname, String hours, String type, String date) {
        super();
        this.studentNameAndSurname = studentNameAndSurname;
        this.hours = hours;
        this.type = type;
        this.date = date;
    }


    public String getStudentNameAndSurname() {
        return studentNameAndSurname;
    }
    public void setStudentNameAndSurname(String studentNameAndSurname) {
        this.studentNameAndSurname = studentNameAndSurname;
    }
    public String getHours() {
        return hours;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }







}
