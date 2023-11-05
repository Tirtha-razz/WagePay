package com.example.wagepay;

public class SummaryDuePaymentModel {

    private String wName;
    private int WageDue;

    public SummaryDuePaymentModel() {
        // Default constructor required for Firebase
    }

    public SummaryDuePaymentModel(String wName, int WageDue) {
        this.wName = wName;
        this.WageDue = WageDue;
    }

    public String getWName() {
        return wName;
    }

    public double getWageDue() {
        return WageDue;
    }



}
