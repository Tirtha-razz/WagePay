package com.example.wagepay;

public class UserHelperClass {

    String name, address, business, phoneNo;

    public UserHelperClass(){}

    public UserHelperClass(String name, String address, String business, String phoneNo) {
        this.name = name;
        this.address = address;
        this.business = business;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
