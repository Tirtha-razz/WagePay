package com.example.wagepay;

public class WorkRecyclerModel {
    String categoryName;

    WorkRecyclerModel(){

    }

    public WorkRecyclerModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
