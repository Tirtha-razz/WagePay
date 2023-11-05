package com.example.wagepay;

import java.util.Map;

import java.util.HashMap;
import java.util.Map;

public class AttendanceRecyclerModel {
    String wImage, wName;
    String workerId;

    private Map<String, String> attendance;
    public AttendanceRecyclerModel() {
        // Default constructor required for Firebase
    }

    public AttendanceRecyclerModel(String wImage, String wName, String wNumber, String workerId, String categoryId) {
        this.wImage = wImage;
        this.wName = wName;
        this.workerId = workerId;
        this.attendance = new HashMap<>();
    }



    public String getwImage() {
        return wImage;
    }

    public void setwImage(String wImage) {
        this.wImage = wImage;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }


    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Map<String, String> getAttendance() {
        return attendance;
    }

    public void setAttendance(Map<String, String> attendance) {
        this.attendance = attendance;
    }
}
