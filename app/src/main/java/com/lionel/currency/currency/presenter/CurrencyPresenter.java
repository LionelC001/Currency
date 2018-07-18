package com.lionel.currency.currency.presenter;

import android.os.Handler;
import android.os.Message;

import com.lionel.currency.currency.CurrencyActivity;
import com.lionel.currency.currency.model.CurrencyModel;
import com.lionel.currency.currency.view.ICurrencyView;

import java.lang.ref.WeakReference;
import java.util.List;

public class CurrencyPresenter implements ICurrencyPresenter {
    private ICurrencyView currencyView;
    private CurrencyModel currencyModel;

    private static class CurrencyHandler extends Handler {
        private final WeakReference<CurrencyActivity> weakReference;

        CurrencyHandler(CurrencyActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CurrencyActivity currencyView = weakReference.get();
            if (currencyView != null) {
                List<Object> data = (List<Object>) msg.obj;
                currencyView.setData(data);
            }
        }
    }

    public CurrencyPresenter(ICurrencyView view) {
        CurrencyHandler handler = new CurrencyHandler((CurrencyActivity) view);
        this.currencyView = view;
        this.currencyModel = new CurrencyModel(this, handler);
    }

    @Override
    public void requestData() {
        currencyModel.requestUData();
    }

    @Override
    public void convert(String nt, String foreign, String buyRate, String sellRate) {
        float result;
        //如果沒有匯率, 則不計算
        if (buyRate.equals("-")) {
            currencyView.onConvertResult("-");
        } else {
            //判斷是台幣換成外幣,或是相反
            if (!nt.equals("")) {
                result = Float.parseFloat(nt) / Float.parseFloat(sellRate);
            } else {
                result = Float.parseFloat(foreign) * Float.parseFloat(buyRate);
            }
            currencyView.onConvertResult(String.format("%.3f", result));
        }
    }
}
