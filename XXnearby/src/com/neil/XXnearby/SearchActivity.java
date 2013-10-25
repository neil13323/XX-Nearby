package com.neil.XXnearby;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends Activity {
    private EditText editText;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String,Object>> tempdata = new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;
    private int currentCount =1;
    private boolean flag =false;
    private ListView listView;
    private TextView textView1;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);
        listView = (ListView) findViewById(R.id.firstitem);
        init();
    }

    private void init() {
        inittitle();


    }

    private void inittitle() {
        editText = (EditText) findViewById(R.id.searchMessage);

        ImageButton backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton searchbutton= (ImageButton) findViewById(R.id.titlesearchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asdasd",editText.getText().toString());
                initlistView();
            }
        });
    }
    private void initlistView(){

        final ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);

        progressDialog.setMessage("加载中,请稍候...");

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            protected Object doInBackground(Object... objects) {


                data = JsonUtils.getData(editText.getText().toString(),1,"100000",MyMessage.mylocation);

                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void onPostExecute(Object o) {
                progressDialog.dismiss();
                if (MyMessage.isempty){
                    Toast.makeText(SearchActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                } else{
                    simpleAdapter = new SimpleAdapter(SearchActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});
                    if(!flag){
                        textView1 = new TextView(SearchActivity.this);
                        textView1.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        textView1.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                        textView1.setPadding(0, 3, 0, 3);
                        textView1.setText("加载更多");
                        textView1.setTextSize(20);


                        listView.addFooterView(textView1);
                        flag = true;
                    }
                    listView.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetInvalidated();
                    textView1.setOnClickListener(new View.OnClickListener() {
                        private TextView textView;
                        @Override
                        public void onClick(View view) {
                            textView = (TextView) view;
                            textView.setText("加载中...");
                            currentCount++;
                            AsyncTask<Object,Object,Object> asyncTask = new AsyncTask<Object, Object, Object>() {
                                @Override
                                protected Object doInBackground(Object... objects) {


                                    tempdata = JsonUtils.getData(editText.getText().toString(),currentCount,"3000",MyMessage.mylocation);
                                    if(!MyMessage.isempty){
                                        int totle = tempdata.size();
                                        Log.d("wwwww", tempdata.size() + ">>>>>");
                                        for(int x=0;x<totle;x++){

                                            data.add(tempdata.get(x));
                                        }
                                    }

                                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                                    if(MyMessage.isempty){
                                        textView.setText("暂无更多数据可以加载");
                                    }else{
                                        simpleAdapter.notifyDataSetChanged();
                                        textView.setText("加载更多");
                                    }
                                }


                            };

                            asyncTask.execute();

                        }
                    });
                    //   listView.add
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                            String name = (String) hashMap.get("name");
                            String distance = (String)hashMap.get("distance");
                            String detail = (String) hashMap.get("detail");
                            String tel = (String) hashMap.get("tel");
                            GeoPoint geoPoint = (GeoPoint) hashMap.get("location");


                            MyMessage.endpoi = new PoiDetail();
                            MyMessage.endpoi.setName(name);
                            MyMessage.endpoi.setPhone(tel);
                            MyMessage.endpoi.setDetail(detail);
                            MyMessage.endpoi.setDistance(distance);
                            MyMessage.endpoi.setLocation(geoPoint);
                            Intent intent = new Intent();
                            intent.setClass(SearchActivity.this,PoiDetailsActivity.class);
                            intent.putExtra("name",hashMap.get("name").toString());
                            intent.putExtra("detail",hashMap.get("detail").toString());
                            startActivity(intent);

                        }
                    });
                    super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                }
            }
        };
        asyncTask.execute();



    }




}
