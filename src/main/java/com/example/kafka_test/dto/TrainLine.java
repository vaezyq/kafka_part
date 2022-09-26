package com.example.kafka_test.dto;

import javax.print.DocFlavor;
import java.util.Objects;

public class TrainLine {
    public String source;
    public String target;
    public double distance;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
