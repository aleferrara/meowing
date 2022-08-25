package com.example.meowing;

public class Reservation {

    String data;
    String timeSlot;
    String user;

    public Reservation() {

    }

    public Reservation(String data, String timeSlot, String user) {
        this.data = data;
        this.timeSlot = timeSlot;
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
