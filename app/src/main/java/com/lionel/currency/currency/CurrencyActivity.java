package com.lionel.currency.currency;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.currency.R;
import com.lionel.currency.currency.model.CurrencyRate;
import com.lionel.currency.currency.presenter.CurrencyPresenter;
import com.lionel.currency.currency.presenter.ICurrencyPresenter;
import com.lionel.currency.currency.view.ICurrencyView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity implements ICurrencyView, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    private ICurrencyPresenter currencyPresenter;
    private TextView mTxtTime, mTxtCashBuy, mTxtCashSell, mTxtSpotBuy, mTxtSpotSell;
    private Spinner mSpinCountry;
    private List<CurrencyRate> mCurrencyRateList;
    private RadioGroup mRadGroupRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currencyPresenter = new CurrencyPresenter(this);

        initViews();
        currencyPresenter.requestData();
    }

    private void initViews() {
        mTxtTime = findViewById(R.id.txt_time);
        mTxtCashBuy = findViewById(R.id.txt_cash_buy);
        mTxtCashSell = findViewById(R.id.txt_cash_sell);
        mTxtSpotBuy = findViewById(R.id.txt_spot_buy);
        mTxtSpotSell = findViewById(R.id.txt_spot_sell);
        mSpinCountry = findViewById(R.id.spin_country);
        mRadGroupRate = findViewById(R.id.rad_group_rate);
        mRadGroupRate.setOnCheckedChangeListener(CurrencyActivity.this);
    }

    @Override
    public void setData(List<Object> data) {
        //設定更新時間
        mTxtTime.setText(((String) data.get(1)));

        //取得匯率List
        mCurrencyRateList = (List<CurrencyRate>) data.get(0);

        //製作傳給Spinner的國名清單
        List<String> countryList = new ArrayList<>();
        for (int i = 0; i < mCurrencyRateList.size(); i++) {
            countryList.add(mCurrencyRateList.get(i).getCountry());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrencyActivity.this,
                R.layout.spinner_item_country, countryList);
        mSpinCountry.setAdapter(adapter);
        mSpinCountry.setOnItemSelectedListener(CurrencyActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //點選Spinner裡的國家,會出現相對應的匯率
        mTxtCashBuy.setText(mCurrencyRateList.get(position).getCashBuyRate());
        mTxtCashSell.setText(mCurrencyRateList.get(position).getCashSellRate());
        mTxtSpotBuy.setText(mCurrencyRateList.get(position).getSpotBuyRate());
        mTxtSpotSell.setText(mCurrencyRateList.get(position).getSpotSellRate());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //Highlight選取的匯率
        if (checkedId == R.id.rad_btn_cash) {
            mTxtCashBuy.setBackgroundResource(R.drawable.bg_currency_table_top_selected);
            mTxtCashSell.setBackgroundResource(R.drawable.bg_currency_table_bottom_selected);
            mTxtSpotBuy.setBackgroundResource(R.drawable.bg_currency_table_top_unselected);
            mTxtSpotSell.setBackgroundResource(R.drawable.bg_currency_table_bottom_unselected);
        } else {
            mTxtSpotBuy.setBackgroundResource(R.drawable.bg_currency_table_top_selected);
            mTxtSpotSell.setBackgroundResource(R.drawable.bg_currency_table_bottom_selected);
            mTxtCashBuy.setBackgroundResource(R.drawable.bg_currency_table_top_unselected);
            mTxtCashSell.setBackgroundResource(R.drawable.bg_currency_table_bottom_unselected);
        }
    }
}
