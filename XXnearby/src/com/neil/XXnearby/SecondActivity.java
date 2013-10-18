package com.neil.XXnearby;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondActivity extends Activity {
    private ListView listView;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<String>messagedata = new ArrayList<String>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second);
        listView = (ListView) findViewById(R.id.firstitem);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(1);
        init();
    }

    private void init() {
        inittitle();
        initlistView();

    }

    private void inittitle() {
        TextView textView = (TextView) findViewById(R.id.titletextview);
        textView.setText(getIntent().getCharSequenceExtra("title"));
        ImageButton backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initlistView(){
        initmessagedata();
        for(String s:messagedata){
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("listmessage",s);
            data.add(hashMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.listitem,new String[]{"listmessage"},new int[]{R.id.listmessage}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.nextbutton);
                final TextView textView = (TextView) view.findViewById(R.id.listmessage);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("wqw", view.toString());
                        Intent intent = new Intent();
                        intent.setClass(SecondActivity.this, ThirdActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(SecondActivity.this, PoilistActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });
                return view;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        listView.setAdapter(simpleAdapter);

    }

    private void initmessagedata(){
        messagedata.add("中餐厅");
        messagedata.add("外国餐厅");
        messagedata.add("快餐厅");
        messagedata.add("休闲餐饮场所");
        messagedata.add("咖啡馆");
        messagedata.add("茶艺馆");
        messagedata.add("冷饮店");
        messagedata.add("糕饼店");
        messagedata.add("甜品店");
    }

}
