package com.lionel.currency.currency.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lionel.currency.currency.CurrencyActivity;
import com.lionel.currency.currency.model.CurrencyModel;
import com.lionel.currency.currency.view.ICurrencyView;
import com.takusemba.spotlight.SimpleTarget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
                currencyView.onRequestDataResult(data);
            }
        }
    }

    public CurrencyPresenter(ICurrencyView view, Context context) {
        CurrencyHandler handler = new CurrencyHandler((CurrencyActivity) view);
        this.currencyView = view;
        this.currencyModel = new CurrencyModel(this, handler, context);
    }

    @Override
    public void requestData() {
        currencyModel.requestUData();
    }

    @Override
    public boolean checkFirstTime() {
        return currencyModel.checkFirstTime();
    }

    @Override
    public List<SimpleTarget> needGuideTarget(View... views) {
        List<SimpleTarget> targets = new ArrayList<>();
        //開場白
        SimpleTarget opening = new SimpleTarget.Builder((Activity) currencyView)
                .setTitle("新手指引")
                .setDescription("感謝您使用這款貨幣匯率轉換器，接下來將簡單為您介紹功能。")
                .setPoint(540, 700)
                .setRadius(1)
                .build();
        targets.add(opening);

        //製作要被介紹的Target和敘述
        String[] titles = new String[]{"匯率", "匯率", "匯率", "匯率", "換算", "換算", "換算", "換算", "換算", "換算", "換算"};
        String[] descriptions = new String[]{"這裡是顯示所選地區的匯率的地方。", "在這裡選擇地區，", "這裡會分別顯示現金匯率，", "以及即期匯率。",
                "這裡是操作換算幣值的地方。", "請先選擇要用現金匯率換算，", "或是即期匯率。", "在這裡輸入要換算的幣值，", "按下換算鈕，"
                , "換算後的幣值將出現在這裡。\n\n反之也可將外幣輸入在這，作台幣換算。", "按此鈕清除所有數值。"};
        float[] radius = new float[]{400, 200, 200, 200, 520, 150, 150, 300, 200, 300, 100};

        for (int i = 0; i < views.length - 1; i++) {
            SimpleTarget target = new SimpleTarget.Builder((Activity) currencyView)
                    .setTitle(titles[i])
                    .setDescription(descriptions[i])
                    .setPoint(views[i])
                    .setRadius(radius[i])
                    .build();
            targets.add(target);
        }

        SimpleTarget targetSetting = new SimpleTarget.Builder((Activity) currencyView)
                .setTitle("額外功能")
                .setDescription("您可以在這裡找到重新整理頁面以及新手指引。")
                .setPoint(views[11].getX() + views[11].getWidth() / 2, views[11].getY() + views[11].getHeight() / 2)
                .setRadius(100)
                .build();
        targets.add(targetSetting);

        return targets;
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
