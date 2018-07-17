package com.lionel.currency.currency.model;

import android.os.Handler;

import com.lionel.currency.currency.presenter.ICurrencyPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class CurrencyModel implements ICurrencyModel {
    private static String url = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
    private final Handler currencyHandler;
    private ICurrencyPresenter currencyPresenter;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            String data = null;
            try {
                Document doc = Jsoup.connect(url).get();
                Elements time = doc.select("span.time");
                data = time.text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            currencyHandler.obtainMessage(1, data).sendToTarget();
        }
    };

    public CurrencyModel(ICurrencyPresenter presenter, Handler handler) {
        this.currencyPresenter = presenter;
        this.currencyHandler = handler;
    }

    @Override
    public void requestUData() {
        new Thread(run).start();
    }
}
