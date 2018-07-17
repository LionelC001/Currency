package com.lionel.currency.currency;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lionel.currency.R;
import com.lionel.currency.currency.presenter.CurrencyPresenter;
import com.lionel.currency.currency.presenter.ICurrencyPresenter;
import com.lionel.currency.currency.view.ICurrencyView;

public class CurrencyActivity extends AppCompatActivity implements ICurrencyView {
    private ICurrencyPresenter currencyPresenter;
    private TextView mTxtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currencyPresenter = new CurrencyPresenter(this);

        initViews();
        currencyPresenter.requestData();
    }

    private void initViews() {
        mTxtResult = findViewById(R.id.txtResult);
    }

    @Override
    public void setData(String data) {
        mTxtResult.setText(data);
    }
}
