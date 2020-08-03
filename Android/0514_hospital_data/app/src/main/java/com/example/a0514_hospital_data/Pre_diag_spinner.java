package com.example.a0514_hospital_data;

public class Pre_diag_spinner {

    public String Pre_diag_id;
    public String Pre_diag_name;

    public Pre_diag_spinner(String pre_diag_id, String pre_diag_name) {
        Pre_diag_id = pre_diag_id;
        Pre_diag_name = pre_diag_name;
    }

    @Override
    public String toString() {
        return Pre_diag_name;
    }
}
