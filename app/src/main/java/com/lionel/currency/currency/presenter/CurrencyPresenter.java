package com.lionel.currency.currency.presenter;

import android.os.Handler;
import android.os.Message;

import com.lionel.currency.currency.CurrencyActivity;
import com.lionel.currency.currency.model.CurrencyModel;
import com.lionel.currency.currency.view.ICurrencyView;

import java.lang.ref.WeakReference;

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
                String data = (String) msg.obj;
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
}
