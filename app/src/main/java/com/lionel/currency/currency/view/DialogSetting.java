package com.lionel.currency.currency.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lionel.currency.R;
import com.lionel.currency.currency.CurrencyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSetting extends Dialog implements AdapterView.OnItemClickListener {
    private final Context mContext;
    private ListView mListSetting;

    public DialogSetting(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_currency_dialog_setting);

        initListView();
        setLayoutParams();
    }

    private void initListView() {
        mListSetting = findViewById(R.id.list_setting);

        String[] listItemText = new String[]{"重新整理", "新手指引"};
        int[] listItemImg = new int[]{R.drawable.ic_refresh, R.drawable.ic_guide};
        List<Map<String, Object>> listItem = new ArrayList<>();
        for (int i = 0; i < listItemImg.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("itemText", listItemText[i]);
            item.put("itemImage", listItemImg[i]);
            listItem.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(mContext,
                listItem,
                R.layout.item_listview_setting,
                new String[]{"itemText", "itemImage"},
                new int[]{R.id.txt_item_list_setting, R.id.img_item_list_setting});

        mListSetting.setAdapter(adapter);
        mListSetting.setOnItemClickListener(DialogSetting.this);
    }

    private void setLayoutParams() {
        //設定Dialog的位置
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.START;
        params.x = 110;
        params.y = 110;
        getWindow().setAttributes(params);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_currency_dialog_setting);
        //設定Dialog進出場的動畫效果
        getWindow().setWindowAnimations(R.style.AnimDialogSetting);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                ((CurrencyActivity) mContext).requestData();
                break;
            case 1:
                ((CurrencyActivity) mContext).showGuide();
                break;
        }
        dismiss();
    }
}
