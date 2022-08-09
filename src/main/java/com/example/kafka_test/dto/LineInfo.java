package com.example.kafka_test.dto;


import java.util.Date;

public class LineInfo {

    private String city;

    private String project;

    private Integer lineNum;

    private Date insert_time;

    private Date collect_time;

    private String base;

    private Date opening_time;

    private String power;

    private Integer station_num;

    private Integer line_config_num;

    private Integer total_line_length;

    private Integer transfer_points_num;

    private Integer transfer_line_num;

    private Integer train_num;

    public LineInfo(String city, String project, Integer lineNum, Date insert_time, Date collect_time, String base, Date opening_time, String power, Integer station_num, Integer line_config_num, Integer total_line_length, Integer transfer_points_num, Integer transfer_line_num, Integer train_num) {

        this.city = city;
        this.project = project;
        this.lineNum = lineNum;
        this.insert_time = insert_time;
        this.collect_time = collect_time;
        this.base = base;
        this.opening_time = opening_time;
        this.power = power;
        this.station_num = station_num;
        this.line_config_num = line_config_num;
        this.total_line_length = total_line_length;
        this.transfer_points_num = transfer_points_num;
        this.transfer_line_num = transfer_line_num;
        this.train_num = train_num;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Date getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Date insert_time) {
        this.insert_time = insert_time;
    }

    public Date getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(Date collect_time) {
        this.collect_time = collect_time;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(Date opening_time) {
        this.opening_time = opening_time;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Integer getStation_num() {
        return station_num;
    }

    public void setStation_num(Integer station_num) {
        this.station_num = station_num;
    }

    public Integer getLine_config_num() {
        return line_config_num;
    }

    public void setLine_config_num(Integer line_config_num) {
        this.line_config_num = line_config_num;
    }

    public Integer getTotal_line_length() {
        return total_line_length;
    }

    public void setTotal_line_length(Integer total_line_length) {
        this.total_line_length = total_line_length;
    }

    public Integer getTransfer_points_num() {
        return transfer_points_num;
    }

    public void setTransfer_points_num(Integer transfer_points_num) {
        this.transfer_points_num = transfer_points_num;
    }

    public Integer getTransfer_line_num() {
        return transfer_line_num;
    }

    public void setTransfer_line_num(Integer transfer_line_num) {
        this.transfer_line_num = transfer_line_num;
    }

    public Integer getTrain_num() {
        return train_num;
    }

    public void setTrain_num(Integer train_num) {
        this.train_num = train_num;
    }


    @Override
    public String toString() {
        return "LineInfo{" +
                "city='" + city + '\'' +
                ", project='" + project + '\'' +
                ", lineNum=" + lineNum +
                ", insert_time=" + insert_time +
                ", collect_time=" + collect_time +
                ", base='" + base + '\'' +
                ", opening_time=" + opening_time +
                ", power='" + power + '\'' +
                ", station_num=" + station_num +
                ", line_config_num=" + line_config_num +
                ", total_line_length=" + total_line_length +
                ", transfer_points_num=" + transfer_points_num +
                ", transfer_line_num=" + transfer_line_num +
                ", train_num=" + train_num +
                '}';
    }
}