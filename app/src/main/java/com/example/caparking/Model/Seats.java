package com.example.caparking.Model;

public class Seats {

    private int id;
    private String parkingName;
    private int parking_id;
    private String departure;
    private String arrival;
    private String date;
    private int seatNumber;
    private int seatStatus;
    private double totalAmount;


    public Seats() {

    }

    public Seats(int id,String departure, String parkingName, String arrival, String date, int seatNumber,int seatStatus,double totalAmount) {

        this.id=id;
        this.departure=departure;
        this.parkingName=parkingName;
        this.arrival=arrival;
        this.date=date;
        this.seatNumber=seatNumber;
        this.seatStatus=seatStatus;
        this.totalAmount= totalAmount;

    }


    public int getParking_id() {
        return parking_id;
    }

    public void setParking_id(int parking_id) {
        this.parking_id = parking_id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
