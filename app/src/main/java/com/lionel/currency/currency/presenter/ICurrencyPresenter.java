package com.lionel.currency.currency.presenter;

public interface ICurrencyPresenter {
    //請求從網路上抓取資料
    void requestData();

    //換算貨幣值
    void convert(String nt, String foreign, String buyRate, String sellRate);
}
