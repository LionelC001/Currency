package com.lionel.currency.currency.presenter;

import android.view.View;

import com.takusemba.spotlight.SimpleTarget;

import java.util.List;

public interface ICurrencyPresenter {
    //請求從網路上抓取資料
    void requestData();

    //換算貨幣值
    void convert(String nt, String foreign, String buyRate, String sellRate);

    //確認是否第一次開啟APP
    boolean checkFirstTime();

    //製作Guide的指引目標
    List<SimpleTarget> needGuideTarget(View... views);
}
