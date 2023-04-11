package com.example.nu_mad_sp2023_final_project_12.models;

import java.io.Serializable;

public class UserData implements Serializable {

    private String name;
    private String email;
    private String profilepicture;

    public UserData() {
    }

    public UserData(String name, String email, String profilepicture) {
        this.name = name;
        this.email = email;
        this.profilepicture = profilepicture;
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

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profilepicture='" + profilepicture + '\'' +
                '}';
    }
}
