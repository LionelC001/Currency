package com.lionel.currency.currency.view;

import java.util.List;

public interface ICurrencyView {
    //將網路抓取下來的資料顯示到View上
    void setData(List<Object> data);

    void onConvertResult(String result);
}
