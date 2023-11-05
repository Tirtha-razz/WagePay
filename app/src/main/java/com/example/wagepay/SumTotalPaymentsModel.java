package com.example.wagepay;

public class SumTotalPaymentsModel {
    private String WName;

    private int TotalPayments; // New field to store total payments

    public SumTotalPaymentsModel() {
        // Default constructor required for Firebase
    }

    public SumTotalPaymentsModel(String WName, int TotalPayments) {
        this.WName = WName;
        this.TotalPayments = TotalPayments;
    }

    public String getWName() {
        return WName;
    }



    public int getTotalPayments() {
        return TotalPayments;
    }
}
