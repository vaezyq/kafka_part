package com.example.kafka_test.dto;

import java.util.Arrays;
import java.util.Objects;

public class TrainLocationAndTheta {
    private double[] coordsData;
    private String subwayNum;
    private double symbolRotateNum;

    public TrainLocationAndTheta(){

    }

    public TrainLocationAndTheta(double[] coordsData, String subwayNum, double symbolRotateNum) {
        this.coordsData = coordsData;
        this.subwayNum = subwayNum;
        this.symbolRotateNum = symbolRotateNum;
    }

    public double[] getCoordsData() {
        return coordsData;
    }

    public void setCoordsData(double[] coordsData) {
        this.coordsData = coordsData;
    }

    public String getSubwayNum() {
        return subwayNum;
    }

    public void setSubwayNum(String subwayNum) {
        this.subwayNum = subwayNum;
    }

    public double getSymbolRotateNum() {
        return symbolRotateNum;
    }

    public void setSymbolRotateNum(double symbolRotateNum) {
        this.symbolRotateNum = symbolRotateNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainLocationAndTheta that = (TrainLocationAndTheta) o;
        return subwayNum.equals(that.subwayNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subwayNum);
    }

    @Override
    public String toString() {
        return "{" +
                "coordsData=" + Arrays.toString(coordsData) +
                ", subwayNum='" + subwayNum + '\'' +
                ", symbolRotateNum=" + symbolRotateNum +
                '}';
    }
}
