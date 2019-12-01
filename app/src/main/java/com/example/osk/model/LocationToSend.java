package com.example.osk.model;

public class LocationToSend {

    private String nw;
    private String ns;
    private String time;
    private Integer sent;

    public LocationToSend(){}

    public LocationToSend(String GPS_NW, String GPS_NS, String time) {
        this.nw = GPS_NW;
        this.ns = GPS_NS;
        this.time = time;
    }

    public String getNw() {
        return nw;
    }

    public void setNw(String nw) {
        this.nw = nw;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public Integer isSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }
}
