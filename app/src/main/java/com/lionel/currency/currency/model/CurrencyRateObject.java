package com.lionel.currency.currency.model;

public class CurrencyRateObject {
    private String country, cashBuyRate, cashSellRate, spotBuyRate, spotSellRate;

    public CurrencyRateObject() {
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCashBuyRate(String cashBuyRate) {
        this.cashBuyRate = cashBuyRate;
    }

    public String getCashBuyRate() {
        return cashBuyRate;
    }

    public void setCashSellRate(String cashSellRate) {
        this.cashSellRate = cashSellRate;
    }

    public String getCashSellRate() {
        return cashSellRate;
    }

    public void setSpotBuyRate(String spotBuyRate) {
        this.spotBuyRate = spotBuyRate;
    }

    public String getSpotBuyRate() {
        return spotBuyRate;
    }

    public void setSpotSellRate(String spotSellRate) {
        this.spotSellRate = spotSellRate;
    }

    public String getSpotSellRate() {
        return spotSellRate;
    }
}
