package com.tekarch.accountServiceMs.dto;


import lombok.Data;



import lombok.Data;

@Data
public class TransactionLimitsDTO {

    private double dailyLimitRemaining;
    private double weeklyLimitRemaining;
    private double monthlyLimitRemaining;

    // Constructor
    public TransactionLimitsDTO(double dailyLimitRemaining, double weeklyLimitRemaining, double monthlyLimitRemaining) {
        this.dailyLimitRemaining = dailyLimitRemaining;
        this.weeklyLimitRemaining = weeklyLimitRemaining;
        this.monthlyLimitRemaining = monthlyLimitRemaining;
    }

    @Override
    public String toString() {
        return "TransactionLimitsDTO{" +
                "dailyLimitRemaining=" + dailyLimitRemaining +
                ", weeklyLimitRemaining=" + weeklyLimitRemaining +
                ", monthlyLimitRemaining=" + monthlyLimitRemaining +
                '}';
    }

}

