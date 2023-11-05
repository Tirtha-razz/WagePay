package com.example.wagepay;

public class PaymentModel {
    private String amount;
    private String date;

    public PaymentModel() {
        // Default constructor required for Firebase
    }

    public PaymentModel(String amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
