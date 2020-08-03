package com.example.a0514_hospital_data;

public class Post_diag_spinner {

    public String Post_diag_id;
    public String Post_diag_name;

    public Post_diag_spinner(String post_diag_id, String post_diag_name) {
        Post_diag_id = post_diag_id;
        Post_diag_name = post_diag_name;
    }

    @Override
    public String toString() {
        return Post_diag_name;
    }
}
