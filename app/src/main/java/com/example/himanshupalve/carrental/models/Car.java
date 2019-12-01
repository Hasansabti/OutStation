package com.example.himanshupalve.carrental.models;

public class Car {
    String name,uuid;
    int seats,rating;

    public Car() {
    }

    public Car(String name, String uuid, int seats, int rating) {
        this.name = name;
        this.uuid = uuid;
        this.seats = seats;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
