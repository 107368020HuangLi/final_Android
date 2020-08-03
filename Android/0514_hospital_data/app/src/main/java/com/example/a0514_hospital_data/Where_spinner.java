package com.example.a0514_hospital_data;

public class Where_spinner {

    public String Where_id;
    public String Where_name;

    public Where_spinner(String where_id, String where_name) {
        Where_id = where_id;
        Where_name = where_name;
    }

    @Override
    public String toString() {
        return Where_name;
    }
}
