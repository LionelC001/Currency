package com.lionel.currency.currency.view;

import java.util.List;

public interface ICurrencyView {
    //要求從網頁抓取資料
    void requestData();

    //將網路抓取下來的資料顯示到View上
    void setData(List<Object> data);

    //轉換幣值的結果
    void onConvertResult(String result);
}
