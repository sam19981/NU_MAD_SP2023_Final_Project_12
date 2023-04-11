package com.example.nu_mad_sp2023_final_project_12.models;

public class Jobs {
    private String name;
    private String description;
    private String postedBy;
    private String takenBy;
    private String imgUrl;
    private String status;
    private String location;
    private double wage;
    private String time;

    public Jobs(String name, String description, String postedBy, String taken, String imgUrl, String status, String location, double wage, String time) {
        this.name = name;
        this.description = description;
        this.postedBy = postedBy;
        this.takenBy = taken;
        this.imgUrl = imgUrl;
        this.status = status;
        this.location = location;
        this.wage = wage;
        this.time = time;
    }

    public Jobs() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getTaken() {
        return takenBy;
    }

    public void setTaken(String taken) {
        this.takenBy = taken;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
