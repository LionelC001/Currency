package com.lionel.currency.currency.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lionel.currency.R;

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
    }

    private void initListView() {
        mListSetting = findViewById(R.id.list_setting);

        String[] listItemText = new String[]{"重新整理", "新手指引", "分享"};
        int[] listItemImg = new int[]{R.drawable.ic_refresh, R.drawable.ic_guide, R.drawable.ic_share};
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
    }
}
