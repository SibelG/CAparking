package com.example.caparking.Model;

public class Seats {


    private ParkingAreas parkingAreas;
    private int parking_id;
    private User user;
    private int user_id;
    private String departure;
    private String arrival;
    private String date;
    private int seatNumber;
    private int seatStatus;


    public Seats() {

    }

    public Seats(String departure, ParkingAreas parkingAreas,User user,int userId, String arrival, int parking_id, String date, int seatNumber,int seatStatus) {

        this.departure=departure;
        this.parkingAreas=parkingAreas;
        this.user=user;
        this.arrival=arrival;
        this.parking_id=parking_id;
        this.date=date;
        this.seatNumber=seatNumber;
        this.seatStatus=seatStatus;
        this.user_id=userId;
    }

    public ParkingAreas getParkingAreas() {
        return parkingAreas;
    }

    public void setParkingAreas(ParkingAreas parkingAreas) {
        this.parkingAreas = parkingAreas;
    }

    public int getParking_id() {
        return parking_id;
    }

    public void setParking_id(int parking_id) {
        this.parking_id = parking_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }
}
