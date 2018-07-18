package com.lionel.currency.currency;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.currency.R;
import com.lionel.currency.currency.adapter.CurrencySpinnerAdapter;
import com.lionel.currency.currency.model.CurrencyRate;
import com.lionel.currency.currency.presenter.CurrencyPresenter;
import com.lionel.currency.currency.presenter.ICurrencyPresenter;
import com.lionel.currency.currency.view.ICurrencyView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity implements ICurrencyView, View.OnClickListener {
    private ICurrencyPresenter currencyPresenter;
    private TextView mTxtTime, mTxtCashBuy, mTxtCashSell, mTxtSpotBuy, mTxtSpotSell, mTxtForeighName;
    private Spinner mSpinCountry;
    private List<CurrencyRate> mCurrencyRateList;
    private RadioGroup mRadGroupRate;
    private EditText mEdtNTCurrency, mEdtForeignCurrency;
    private ImageButton mBtnConvert;


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
        mTxtForeighName = findViewById(R.id.txt_foreign_name);
        mSpinCountry = findViewById(R.id.spin_country);
        mRadGroupRate = findViewById(R.id.rad_group_rate);
        mRadGroupRate.setOnCheckedChangeListener(new CheckedChangeListener());
        mEdtNTCurrency = findViewById(R.id.edt_nt_currency);
        mEdtForeignCurrency = findViewById(R.id.edt_foreign_currency);
        mBtnConvert = findViewById(R.id.btn_convert);
        mBtnConvert.setOnClickListener(CurrencyActivity.this);

        //限制只能輸入正常數字, 例如不能同時有兩個小數點
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mEdtNTCurrency.setKeyListener(DigitsKeyListener.getInstance(null, true, true));
            mEdtForeignCurrency.setKeyListener(DigitsKeyListener.getInstance(null, true, true));
        } else {
            mEdtNTCurrency.setKeyListener(DigitsKeyListener.getInstance(true, true));
            mEdtForeignCurrency.setKeyListener(DigitsKeyListener.getInstance(true, true));
        }
    }

    @Override
    public void setData(List<Object> data) {
        //設定更新時間
        String time = getString(R.string.update_time) + (String) data.get(1);
        mTxtTime.setText(time);

        //取得匯率List
        mCurrencyRateList = (List<CurrencyRate>) data.get(0);

        //製作傳給Spinner的國名清單
        List<String> countryList = new ArrayList<>();
        for (int i = 0; i < mCurrencyRateList.size(); i++) {
            countryList.add(mCurrencyRateList.get(i).getCountry());
        }

        mSpinCountry.setAdapter(new CurrencySpinnerAdapter(this, countryList));
        mSpinCountry.setOnItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_convert:
                // 取得輸入的錢幣數值
                String sNtCurrency = mEdtNTCurrency.getText().toString();
                String sForeignCurrency = mEdtForeignCurrency.getText().toString();
                //錢幣數值必不為空
                if (!sNtCurrency.equals("") || !sForeignCurrency.equals("")) {
                    //根據所選牌告利率, 傳送對應利率
                    if (mRadGroupRate.getCheckedRadioButtonId() == R.id.rad_btn_cash) {
                        currencyPresenter.convert(
                                sNtCurrency,
                                sForeignCurrency,
                                mTxtCashBuy.getText().toString(),
                                mTxtCashSell.getText().toString());
                    } else if (mRadGroupRate.getCheckedRadioButtonId() == R.id.rad_btn_spot) {
                        currencyPresenter.convert(
                                sNtCurrency,
                                sForeignCurrency,
                                mTxtSpotBuy.getText().toString(),
                                mTxtSpotSell.getText().toString());
                    } else {
                        Toast.makeText(this, "請選擇一種牌告利率", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onConvertResult(String result) {
        if (!mEdtNTCurrency.getText().toString().equals("")) {
            mEdtForeignCurrency.setText(result);
        } else {
            mEdtNTCurrency.setText(result);
        }
    }

    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //點選Spinner裡的國家,會出現相對應的匯率
            mTxtCashBuy.setText(mCurrencyRateList.get(position).getCashBuyRate());
            mTxtCashSell.setText(mCurrencyRateList.get(position).getCashSellRate());
            mTxtSpotBuy.setText(mCurrencyRateList.get(position).getSpotBuyRate());
            mTxtSpotSell.setText(mCurrencyRateList.get(position).getSpotSellRate());

            //顯示相對應的國家名
            mTxtForeighName.setText(mCurrencyRateList.get(position).getCountry());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

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
}
