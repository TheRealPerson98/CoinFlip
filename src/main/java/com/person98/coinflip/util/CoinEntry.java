package com.person98.coinflip.util;

public class CoinEntry {
    private double amount;

    private boolean side;

    public CoinEntry(double amount, boolean side) {
        this.amount = amount;
        this.side = side;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean getSide() {
        return this.side;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSide(boolean side) {
        this.side = side;
    }
}
