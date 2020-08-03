package com.example.a0514_hospital_data;

public class occupation_spinner {
    public String occupation_id;
    public String occupation_name;

    public occupation_spinner(String occupation_id,String occupation_name) {
        this.occupation_id = occupation_id;
        this.occupation_name = occupation_name;
    }

    @Override
    public String toString() {
        return occupation_name;
    }
}
