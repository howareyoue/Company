package com.example.company;

public class RegistrationInfo {
    private String driverName;
    private String departureTime;
    private String departureLocation;
    private String destination;
    public RegistrationInfo() {
    }

    public RegistrationInfo(String driverName, String departureTime, String departureLocation, String destination) {
        this.driverName = driverName;
        this.departureTime = departureTime;
        this.departureLocation = departureLocation;
        this.destination = destination;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}