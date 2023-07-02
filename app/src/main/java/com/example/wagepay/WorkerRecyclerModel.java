package com.example.wagepay;

public class WorkerRecyclerModel {
    int img;
    String name,phone;

    public WorkerRecyclerModel(int img,String name, String phone) {
        this.img = img;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
}
