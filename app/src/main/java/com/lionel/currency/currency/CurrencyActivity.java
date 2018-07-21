package com.lionel.currency.currency;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.currency.R;
import com.lionel.currency.currency.adapter.CurrencySpinnerAdapter;
import com.lionel.currency.currency.model.CurrencyRateObject;
import com.lionel.currency.currency.presenter.CurrencyPresenter;
import com.lionel.currency.currency.presenter.ICurrencyPresenter;
import com.lionel.currency.currency.view.DialogSetting;
import com.lionel.currency.currency.view.ICurrencyView;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity implements ICurrencyView, View.OnClickListener {
    private ICurrencyPresenter currencyPresenter;
    private View mLayoutLoading;
    private ImageView mImgLoading, mImgClearTalk;
    private TextView mTxtTime, mTxtCashBuy, mTxtCashSell, mTxtSpotBuy, mTxtSpotSell, mTxtForeignName, mTxtLoading;
    private Spinner mSpinCountry;
    private List<CurrencyRateObject> mCurrencyRateObjectList;
    private RadioGroup mRadGroupRate;
    private RadioButton mRadBtnCash, mRadBtnSpot;
    private EditText mEdtNTCurrency, mEdtForeignCurrency;
    private ImageButton mBtnConvert, mBtnClear, mBtnSetting, mBtnLoadingCancel;
    private Animator loadingAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currencyPresenter = new CurrencyPresenter(CurrencyActivity.this, this);

        initViews();
        requestData();
    }

    @Override
    public void requestData() {
        if (currencyPresenter.isNetworkAvailable()) {
            currencyPresenter.requestData();
            //開啟讀取畫面
            showLoading("loading");
        } else {
            Toast.makeText(this, "網路訊號不穩，請確認網路連線狀態。", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        mLayoutLoading = findViewById(R.id.layout_loading);
        mImgLoading = findViewById(R.id.img_loading);
        mTxtLoading = findViewById(R.id.txt_loading);
        mBtnLoadingCancel = findViewById(R.id.btn_loading_cancel);
        mBtnLoadingCancel.setOnClickListener(CurrencyActivity.this);
        mTxtTime = findViewById(R.id.txt_time);
        mTxtCashBuy = findViewById(R.id.txt_cash_buy);
        mTxtCashSell = findViewById(R.id.txt_cash_sell);
        mTxtSpotBuy = findViewById(R.id.txt_spot_buy);
        mTxtSpotSell = findViewById(R.id.txt_spot_sell);
        mTxtForeignName = findViewById(R.id.txt_foreign_name);
        mSpinCountry = findViewById(R.id.spin_country);
        mRadGroupRate = findViewById(R.id.rad_group_rate);
        mRadGroupRate.setOnCheckedChangeListener(new CheckedChangeListener());
        mRadBtnCash = findViewById(R.id.rad_btn_cash);
        mRadBtnSpot = findViewById(R.id.rad_btn_spot);
        mEdtNTCurrency = findViewById(R.id.edt_nt_currency);
        mEdtForeignCurrency = findViewById(R.id.edt_foreign_currency);
        mBtnConvert = findViewById(R.id.btn_convert);
        mBtnConvert.setOnClickListener(CurrencyActivity.this);
        mImgClearTalk = findViewById(R.id.img_clear_talk);
        mBtnClear = findViewById(R.id.btn_clear);
        mBtnClear.setOnClickListener(CurrencyActivity.this);
        mBtnSetting = findViewById(R.id.btn_setting);
        mBtnSetting.setOnClickListener(CurrencyActivity.this);

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
    public void onRequestDataResult(List<Object> data) {
        //設定更新時間
        String time = getString(R.string.update_time) + (String) data.get(1);
        mTxtTime.setText(time);

        //取得匯率List
        mCurrencyRateObjectList = (List<CurrencyRateObject>) data.get(0);

        //製作傳給Spinner的國名清單
        List<String> countryList = new ArrayList<>();
        for (int i = 0; i < mCurrencyRateObjectList.size(); i++) {
            countryList.add(mCurrencyRateObjectList.get(i).getCountry());
        }

        mSpinCountry.setAdapter(new CurrencySpinnerAdapter(this, countryList));
        mSpinCountry.setOnItemSelectedListener(new ItemSelectedListener());

        //關閉讀取畫面
        hideLoading();

        //待抓取檔案完成後
        //如果是第一次開啟APP, 詢問是否要新手指引
        if (currencyPresenter.checkFirstTime()) {
            AlertDialog dialogGuide = new AlertDialog.Builder(CurrencyActivity.this)
                    .setTitle("新手指引")
                    .setMessage("是否要顯示簡易教學?")
                    .setPositiveButton("好的", new GuideDialogClickListener())
                    .setNegativeButton("不用", null)
                    .setCancelable(true)
                    .show();

            dialogGuide.getWindow().setWindowAnimations(R.style.AnimDialog);
        }
    }

    @Override
    public void showGuide() {
        List<SimpleTarget> targets = currencyPresenter.needGuideTarget(
                mRadGroupRate, mSpinCountry, mTxtCashBuy, mTxtSpotBuy, mBtnConvert, mRadBtnCash, mRadBtnSpot,
                mEdtNTCurrency, mBtnConvert, mEdtForeignCurrency, mBtnClear, mBtnSetting);


        Spotlight.with(CurrencyActivity.this)
                .setDuration(100)
                .setTargets(targets.get(0), targets.get(1), targets.get(2), targets.get(3),
                        targets.get(4), targets.get(5), targets.get(6), targets.get(7), targets.get(8),
                        targets.get(9), targets.get(10), targets.get(11), targets.get(12))
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_convert:
                doConvert();
                break;
            case R.id.btn_clear:
                doClear();
                break;
            case R.id.btn_setting:
                showDialogSetting();
                break;
            case R.id.btn_loading_cancel:
                hideLoading();
                break;
        }
    }

    private void showDialogSetting() {
        AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(CurrencyActivity.this, R.animator.anim_btn_setting);
        anim.setTarget(mBtnSetting);
        anim.start();

        //顯示清單
        Dialog dialog = new DialogSetting(CurrencyActivity.this);
        dialog.show();
        dialog.getWindow().setLayout(550, ViewGroup.LayoutParams.WRAP_CONTENT); //此函示必須在show()後面才能呼叫
    }

    private void doClear() {
        //實現清除按鈕的動畫
        mBtnClear.bringToFront();
        AnimatorSet animBtn = (AnimatorSet) AnimatorInflater.loadAnimator(CurrencyActivity.this, R.animator.anim_btn_clear);
        animBtn.setTarget(mBtnClear);
        animBtn.start();
        //實現清除按鈕說的話的動畫
        mImgClearTalk.setVisibility(View.VISIBLE);
        mImgClearTalk.bringToFront();
        AnimatorSet animImg = (AnimatorSet) AnimatorInflater.loadAnimator(CurrencyActivity.this, R.animator.anim_img_clear_talk);
        animImg.setTarget(mImgClearTalk);
        animImg.start();
        //裝設傾聽器, 在動畫播完後, 隱藏mImgClearTalk
        animImg.addListener(new ImageClearAnimatorListener());

        //清除輸入框的數值
        mEdtNTCurrency.setText("");
        mEdtForeignCurrency.setText("");
    }

    private void doConvert() {
        //取得輸入的錢幣數值
        String sNtCurrency = mEdtNTCurrency.getText().toString();
        String sForeignCurrency = mEdtForeignCurrency.getText().toString();

        if (!sNtCurrency.equals("") && !sForeignCurrency.equals("")) {
            Toast.makeText(this, "一次只能輸入一組數值", Toast.LENGTH_SHORT).show();
        } else if (!sNtCurrency.equals("") || !sForeignCurrency.equals("")) {
            //錢幣數值必不為空
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
    }

    @Override
    public void onConvertResult(String result) {
        //讓按鈕實現動畫
        Animation anim = AnimationUtils.loadAnimation(CurrencyActivity.this, R.anim.anim_btn_convert);
        mBtnConvert.startAnimation(anim);

        //將計算結果顯示在空欄位
        if (!mEdtNTCurrency.getText().toString().equals("")) {
            mEdtForeignCurrency.setText(result);
        } else {
            mEdtNTCurrency.setText(result);
        }
    }

    @Override
    public void showLoading(String mode) {
        //mode = "loading" or "demo"
        mLayoutLoading.setVisibility(View.VISIBLE);
        loadingAnim = AnimatorInflater.loadAnimator(CurrencyActivity.this, R.animator.anim_loading);
        loadingAnim.setTarget(mImgLoading);
        loadingAnim.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //"mode = demo"時, 要額外顯示右上角的取消按鈕, 並關掉讀取字樣
        if (mode.equals("demo")) {
            mBtnLoadingCancel.setVisibility(View.VISIBLE);
            mTxtLoading.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void hideLoading() {
        mBtnLoadingCancel.setVisibility(View.GONE);
        mTxtLoading.setVisibility(View.VISIBLE);
        loadingAnim.pause();
        mLayoutLoading.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //點選Spinner裡的國家,會出現相對應的匯率
            mTxtCashBuy.setText(mCurrencyRateObjectList.get(position).getCashBuyRate());
            mTxtCashSell.setText(mCurrencyRateObjectList.get(position).getCashSellRate());
            mTxtSpotBuy.setText(mCurrencyRateObjectList.get(position).getSpotBuyRate());
            mTxtSpotSell.setText(mCurrencyRateObjectList.get(position).getSpotSellRate());

            //顯示相對應的國家名
            mTxtForeignName.setText(mCurrencyRateObjectList.get(position).getCountry());
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

    private class GuideDialogClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            showGuide();
        }
    }

    private class ImageClearAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mImgClearTalk.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
