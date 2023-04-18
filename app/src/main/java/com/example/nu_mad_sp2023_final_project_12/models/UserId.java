package com.example.nu_mad_sp2023_final_project_12.models;

import java.util.List;

public class UserId {
    private List<String> members;

    public UserId(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
