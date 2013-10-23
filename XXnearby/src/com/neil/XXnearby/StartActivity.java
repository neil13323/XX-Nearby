package com.neil.XXnearby;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartActivity extends Activity {
    private ListView listView;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<String>messagedata = new ArrayList<String>();
    private ImageButton locatedbutton;
    private ImageButton optionsbutton;
    private ImageButton titlesearchbutton;
    private TextView locatedmessage;
    private LocationClient locationClient;

    private String addresstext;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        listView = (ListView) findViewById(R.id.firstitem);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(1);
        locatedbutton = (ImageButton) findViewById(R.id.locationbutton);
        locatedmessage= (TextView) findViewById(R.id.locationmessage);
        locationClient = new LocationClient(this);
        init();
    }

    private void init() {
        initlistView();
        initimagebutton();
        initOptions();
        initSearch();
        initmylocationmessage();
    }

    private void initSearch() {
        titlesearchbutton= (ImageButton) findViewById(R.id.titlesearchbutton);
        titlesearchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(StartActivity.this, SearchActivity.class);
                startActivity(intent);
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


    }

    private void initmylocationmessage() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.e("asdasd",bdLocation.getLatitude()+">>>>"+bdLocation.getLongitude());
                MyMessage.mylocation = new GeoPoint((int)(bdLocation.getLatitude()*1E6),(int)(bdLocation.getLongitude()*1E6));
                MyMessage.myaddress  = bdLocation.getAddrStr();
                MyMessage.mycity  = bdLocation.getCity();
                addresstext =bdLocation.getAddrStr();

                locatedmessage.setText(addresstext);
               // Toast.makeText(StartActivity.this,"定位完成!"+bdLocation.getAddrStr(),Toast.LENGTH_LONG).show();
                locationClient.stop();
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }

    private void initimagebutton() {
        locatedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String text= (String) locatedmessage.getText();
               locatedmessage.setText("定位中,请稍等...");
               AsyncTask asyncTask= new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        locationClient.start();

                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                   @Override
                   protected void onPostExecute(Object o) {
                       locationClient.stop();
                       locatedmessage.setText(addresstext);
                       super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                   }
               };
                asyncTask.execute();

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
                        intent.setClass(StartActivity.this, SecondActivity.class);
                        intent.putExtra("title",textView.getText());

                        startActivity(intent);
                    }
                });
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(StartActivity.this, PoilistActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });

                return view;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        listView.setAdapter(simpleAdapter);


    }


    private  void initOptions(){
        optionsbutton = (ImageButton) findViewById(R.id.optionsbutton);
        optionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, OptionsActivity.class);

                startActivity(intent);
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });



    }

    private void initmessagedata(){
        messagedata.add("餐饮服务");
        messagedata.add("购物服务");
        messagedata.add("生活服务");
        messagedata.add("体育休闲服务");
        messagedata.add("住宿服务");
        messagedata.add("医疗保健服务");
        messagedata.add("科教文化服务");
        messagedata.add("交通设施服务");
        messagedata.add("公共设施");
    }

}
