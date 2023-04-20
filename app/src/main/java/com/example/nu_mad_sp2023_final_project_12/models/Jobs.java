package com.example.nu_mad_sp2023_final_project_12.models;
import com.google.firebase.firestore.DocumentId;

public class Jobs {

    @DocumentId
    private String job_id;
    private String name;
    private String description;
    private String postedBy;
    private String takenBy;
    private String imgUrl;
    private String status;
    private String location;




    private String x;
    private String y;
    private double wage;
    private String time;

    public Jobs(String name, String description, String postedBy, String taken, String imgUrl, String status, String location, double wage, String time, String x, String y) {
        this.name = name;
        this.description = description;
        this.postedBy = postedBy;
        this.takenBy = taken;
        this.imgUrl = imgUrl;
        this.status = status;
        this.location = location;
        this.wage = wage;
        this.time = time;
        this.x = x;
        this.y = y;
    }

    public Jobs() {

    }


    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getJob_id() {
        return job_id;
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
