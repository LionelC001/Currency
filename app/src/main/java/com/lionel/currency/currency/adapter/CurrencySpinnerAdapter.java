package com.lionel.currency.currency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.lionel.currency.R;

import java.util.List;

public class CurrencySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final List<String> mCountryList;
    private final Context mContext;

    public CurrencySpinnerAdapter(Context c, List<String> list) {
        this.mContext = c;
        this.mCountryList = list;
    }

    @Override
    public int getCount() {
        return mCountryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCountryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TextView txtView = (TextView) inflater.inflate(R.layout.item_spinner_country, null);
        txtView.setPadding(10, 10, 10, 10);
        txtView.setText(mCountryList.get(position));
        return txtView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TextView txtView = (TextView) inflater.inflate(R.layout.item_spinner_country, null);
        txtView.setPadding(10, 10, 10, 30);
        txtView.setText(mCountryList.get(position));
        return txtView;
    }
}
