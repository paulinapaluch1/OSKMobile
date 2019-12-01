package com.example.osk.model;

import java.time.LocalDateTime;

public class Location {

    private String nw;
    private String ns;
    private LocalDateTime time;
    private boolean sent;

    public Location(String GPS_NW, String GPS_NS, LocalDateTime time) {
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
