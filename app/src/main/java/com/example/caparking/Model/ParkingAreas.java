package com.example.caparking.Model;

public class ParkingAreas {
    private int id;
    private int total_seats;
    private int perHourPrice;
    private String parkingName;

    public ParkingAreas(){

    }

    public ParkingAreas(int id, int total_seats, int perHourPrice, String parkingName) {
        this.id = id;
        this.total_seats = total_seats;
        this.perHourPrice = perHourPrice;
        this.parkingName = parkingName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }

    public int getPerHourPrice() {
        return perHourPrice;
    }

    public void setPerHourPrice(int perHourPrice) {
        this.perHourPrice = perHourPrice;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
}
