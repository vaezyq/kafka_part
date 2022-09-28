package com.example.kafka_test.dto;

import java.util.Objects;

public class Distance {
    private String current_station_id;
    private String next_station_id;
    private double distance;

    public Distance(){

    }

    public Distance(String current_station_id, String next_station_id) {
        this.current_station_id = current_station_id;
        this.next_station_id = next_station_id;
    }

    public Distance(String current_station_id, String next_station_id, double distance) {
        this.current_station_id = current_station_id;
        this.next_station_id = next_station_id;
        this.distance = distance;
    }

    public String getCurrent_station_id() {
        return current_station_id;
    }

    public void setCurrent_station_id(String current_station_id) {
        this.current_station_id = current_station_id;
    }

    public String getNext_station_id() {
        return next_station_id;
    }

    public void setNext_station_id(String next_station_id) {
        this.next_station_id = next_station_id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return current_station_id.equals(distance.current_station_id) && next_station_id.equals(distance.next_station_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current_station_id, next_station_id);
    }
}

//        "current_station_id": "15",
//        "next_station_id": "14",
//        "distance": 1032.7