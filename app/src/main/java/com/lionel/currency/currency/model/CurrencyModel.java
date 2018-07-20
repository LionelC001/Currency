package com.lionel.currency.currency.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.lionel.currency.currency.presenter.ICurrencyPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CurrencyModel implements ICurrencyModel {
    private static String url = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
    private final Handler currencyHandler;
    private final Context mContext;
    private ICurrencyPresenter currencyPresenter;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            String sTime = null;
            List<CurrencyRateObject> currencyRateObjectList = new ArrayList<>();
            int index = 0;
            try {
                Document doc = Jsoup.connect(url).get();
                // 抓取國家幣別和買進賣出各兩種後, set進CurrencyRate物件, 再存入List中
                for (Element eCountry : doc.select("div.hidden-phone.print_show")) {
                    CurrencyRateObject currencyRateObject = new CurrencyRateObject();
                    //抓取國家幣別
                    currencyRateObject.setCountry(eCountry.text());

                    //抓取現金買進賣出匯率
                    Elements eCashRate = doc.select("td.rate-content-cash.text-right.print_hide");
                    currencyRateObject.setCashBuyRate(eCashRate.eq(index).text());
                    currencyRateObject.setCashSellRate(eCashRate.eq(index + 1).text());
                    //抓取即期買進賣出匯率
                    Elements eSpotRate = doc.select("td.rate-content-sight.text-right.print_hide");
                    currencyRateObject.setSpotBuyRate(eSpotRate.eq(index).text());
                    currencyRateObject.setSpotSellRate(eSpotRate.eq(index + 1).text());
                    index += 2;

                    //將製作好的CurrencyRate物件存入List中
                    currencyRateObjectList.add(currencyRateObject);
                }
                // 抓取更新時間
                Elements eTime = doc.select("span.time");
                sTime = eTime.text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //將剛剛取得的兩種物件存入List, 方便Message傳送
            List<Object> data = new ArrayList<>();
            data.add(currencyRateObjectList);
            data.add(sTime);
            currencyHandler.obtainMessage(1, data).sendToTarget();
        }
    };

    public CurrencyModel(ICurrencyPresenter presenter, Handler handler, Context context) {
        this.currencyPresenter = presenter;
        this.mContext = context;
        this.currencyHandler = handler;
    }

    @Override
    public void requestUData() {
        new Thread(run).start();
    }

    @Override
    public boolean checkFirstTime() {
        SharedPreferences sp = mContext.getSharedPreferences("checkFirstTime", Context.MODE_PRIVATE);
        if (sp.getBoolean("first_time", true)) {
            sp.edit().putBoolean("first_time", false).apply();
            return true;
        } else {
            return false;
        }
    }
}
