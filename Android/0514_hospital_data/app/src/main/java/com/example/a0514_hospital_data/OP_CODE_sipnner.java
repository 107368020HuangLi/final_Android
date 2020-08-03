package com.example.a0514_hospital_data;

public class OP_CODE_sipnner {
    public String OP_CODE_id;
    public String OP_CODE_name;

    public OP_CODE_sipnner(String OP_CODE_id, String OP_CODE_name) {
        this.OP_CODE_id = OP_CODE_id;
        this.OP_CODE_name = OP_CODE_name;
    }

    @Override
    public String toString() {
        return OP_CODE_name;
    }
}
