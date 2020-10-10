package com.sy.vo;

public class ChartVo {

    private String date;

    private String powerValue;

    private String rateValue;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPowerValue() {
        return powerValue;
    }

    public void setPowerValue(String powerValue) {
        this.powerValue = powerValue;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    @Override
    public String toString() {
        return "ChartVo{" +
                "date='" + date + '\'' +
                ", powerValue='" + powerValue + '\'' +
                ", rateValue='" + rateValue + '\'' +
                '}';
    }
}
