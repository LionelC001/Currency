package com.lionel.currency.currency.model;

public interface ICurrencyModel {
    //請求抓取網頁資料
    void requestUData();

    //確認是否為第一次開啟APP
    boolean checkFirstTime();
}
