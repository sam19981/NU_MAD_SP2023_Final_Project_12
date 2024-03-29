package com.example.nu_mad_sp2023_final_project_12.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable {

    private String name;
    private String email;

    private String phoneNum;

    private String profilepicture;

    private List<String> postedJobs;

    private List<String> takenJobs;

    private List<String> friendList;

    public UserData(String name, String email, String profilepicture, List<String> postedJobs, List<String> takenJobs, List<String> friendList) {
        this.name = name;
        this.email = email;
        this.profilepicture = profilepicture;
        this.postedJobs = postedJobs;
        this.takenJobs = takenJobs;
        this.friendList = friendList;
    }

    public List<String> getTakenJobs() {
        return takenJobs;
    }

    public void setTakenJobs(List<String> takenJobs) {
        this.takenJobs = takenJobs;
    }

    public List<String> getPostedJobs() {
        return postedJobs;
    }

    public void setPostedJobs(List<String> postedJobs) {
        this.postedJobs = postedJobs;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public UserData() {
    }

    public UserData(String name, String email, String profilepicture)
    {
        this.name = name;
        this.email = email;
        this.profilepicture = profilepicture;
        this.postedJobs = new ArrayList<>();
    }



    public UserData(String name, String email, String profilepicture, List<String> postedJobs) {
        this.name = name;
        this.email = email;
        this.profilepicture = profilepicture;
        this.postedJobs = postedJobs;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profilepicture='" + profilepicture + '\'' +
                ", postedJobs=" + postedJobs +
                ", friendList=" + friendList +
                '}';
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

}
