package com.example.wagepay;

public class WorkerRecyclerModel {
   String wImage, wName, wAddress, wNumber, wWageRate;
   String categoryId;
   String workerId;

    WorkerRecyclerModel ()
    {

    }

    public WorkerRecyclerModel(String wImage, String wName, String wAddress, String wNumber, String wWageRate, String categoryId, String workerId) {
        this.wImage = wImage;
        this.wName = wName;
        this.wAddress = wAddress;
        this.wNumber = wNumber;
        this.wWageRate = wWageRate;
        this.categoryId = categoryId;
        this.workerId = workerId;
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

    public String getwAddress() {
        return wAddress;
    }

    public void setwAddress(String wAddress) {
        this.wAddress = wAddress;
    }

    public String getwNumber() {
        return wNumber;
    }

    public void setwNumber(String wNumber) {
        this.wNumber = wNumber;
    }

    public String getwWageRate() {
        return wWageRate;
    }

    public void setwWageRate(String wWageRate) {
        this.wWageRate = wWageRate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }
}
