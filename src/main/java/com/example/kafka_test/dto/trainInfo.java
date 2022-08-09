package com.example.kafka_test.dto;

public class trainInfo {

    private int trainNum;

    private int lineNum;

    private String network;

    private String signal_strength;

    private String run_model;

    private String control_model;

    private String current_station;

    private String next_station;

    private String status;

    private String is_online;

    private String is_operation;

    private int total_train_length;


    public int getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(int trainNum) {
        this.trainNum = trainNum;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(String signal_strength) {
        this.signal_strength = signal_strength;
    }

    public String getRun_model() {
        return run_model;
    }

    public void setRun_model(String run_model) {
        this.run_model = run_model;
    }

    public String getControl_model() {
        return control_model;
    }

    public void setControl_model(String control_model) {
        this.control_model = control_model;
    }

    public String getCurrent_station() {
        return current_station;
    }

    public void setCurrent_station(String current_station) {
        this.current_station = current_station;
    }

    public String getNext_station() {
        return next_station;
    }

    public void setNext_station(String next_station) {
        this.next_station = next_station;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getIs_operation() {
        return is_operation;
    }

    public void setIs_operation(String is_operation) {
        this.is_operation = is_operation;
    }

    public int getTotal_train_length() {
        return total_train_length;
    }

    public void setTotal_train_length(int total_train_length) {
        this.total_train_length = total_train_length;
    }


    public trainInfo(int trainNum, int lineNum, String network, String signal_strength, String run_model, String control_model, String current_station, String next_station, String status, String is_online, String is_operation, int total_train_length) {
        this.trainNum = trainNum;
        this.lineNum = lineNum;
        this.network = network;
        this.signal_strength = signal_strength;
        this.run_model = run_model;
        this.control_model = control_model;
        this.current_station = current_station;
        this.next_station = next_station;
        this.status = status;
        this.is_online = is_online;
        this.is_operation = is_operation;
        this.total_train_length = total_train_length;
    }
}

















