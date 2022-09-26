package com.example.kafka_test.dto;

import java.util.Objects;

public class TrainStation {
    public String name;
    public double[] value;


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
