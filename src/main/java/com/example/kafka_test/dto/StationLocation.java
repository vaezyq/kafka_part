package com.example.kafka_test.dto;

public class StationLocation {
    private String id;
    private String name;
    double[] value;

    public StationLocation(){

    }

    public StationLocation(String id, String name, double[] value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }


}

//    "id": "25",
//    "name": "尧化新村",
//    "value": [
//    1185,
//    164
//    ]