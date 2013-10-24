package com.neil.XXnearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.*;

public class PoilistActivity extends Activity {

    private static BMapManager bMapManager;
    private ListView listView;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String,Object>> tempdata = new ArrayList<Map<String, Object>>();
    private List<String>messagenamedata = new ArrayList<String>();
    private List<String>messagedetaildata = new ArrayList<String>();
    private List<String>messagedistancedata = new ArrayList<String>();
    private View distanceselect,relativemap;
    private ImageButton tabimagebutton,locatedbutton,prebutton,nextbutton;
    private  SimpleAdapter simpleAdapter;
    private MapView mapView;
    private View popview;
    private LayoutInflater layoutInflater;
    private TextView textView1;
    private MyItemizedOverlay<OverlayItem> itemizedOverlay;
    private String title;
    private int currentCount = 1;
    private int mapcurrentcount=1;
    private String range="3000";


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DemoApplication demoApplication = (DemoApplication) getApplication();
        if (demoApplication.mBMapManager==null){
            demoApplication.mBMapManager = new BMapManager(this);
            demoApplication.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }

        setContentView(R.layout.poidetail);
        listView = (ListView) findViewById(R.id.firstitem);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(1);
        tabimagebutton= (ImageButton) findViewById(R.id.mapbutton);
        distanceselect = findViewById(R.id.distanceselected);
        mapView= (MapView) findViewById(R.id.bmapView);
        relativemap = findViewById(R.id.mapparent);
        prebutton = (ImageButton) findViewById(R.id.probutton);
        nextbutton = (ImageButton) findViewById(R.id.prebutton);
        layoutInflater = LayoutInflater.from(this);
        locatedbutton= (ImageButton) findViewById(R.id.locatedbutton);
        //mylocation = new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6));
        title = getIntent().getStringExtra("title");
        init();
    }

    private void init() {
        inittitle();
        initdistancebar();
        initlistView();
        initmapdata();

        initmapView();

    }

    private void initmapdata(){
        mapView.getController().setCenter(MyMessage.mylocation);


        itemizedOverlay = new MyItemizedOverlay<OverlayItem>(getResources().getDrawable(R.drawable.u71_normal),mapView);
        ItemizedOverlay<OverlayItem> mylocationOverlay = new ItemizedOverlay<OverlayItem>(getResources().getDrawable(R.drawable.u71_normal),mapView);
        OverlayItem overlayItem = new OverlayItem(MyMessage.mylocation,"这是我的位置","这是我位置的描述");
        mylocationOverlay.addItem(overlayItem);
        JsonUtils.getData(title,mapcurrentcount,range,MyMessage.mylocation);
        for(int x=0;x<MyMessage.poiDetails.size();x++){
            PoiDetail poiDetail = MyMessage.poiDetails.get(x);
            OverlayItem overlayItem1 = new OverlayItem(poiDetail.getLocation(),poiDetail.getName(),poiDetail.getDetail());
            overlayItem1.setMarker(getResources().getDrawable(R.drawable.ic_loc_normal));
            itemizedOverlay.addItem(overlayItem1);
        }

        mapView.getOverlays().add(itemizedOverlay);
        mapView.getOverlays().add(mylocationOverlay);
        popview = layoutInflater.inflate(R.layout.popitem, null);
        popview.setVisibility(View.GONE);
        mapView.addView(popview);
        popview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                startActivity(intent);

            }
        });
        setCenter();
        mapView.refresh();
    }

    private void initmapView() {
        tabimagebutton.setOnClickListener(new View.OnClickListener() {
            private boolean flag= true;
            @Override
            public void onClick(View view) {
                ImageButton imageButton = (ImageButton) view;
                if(flag){

                    imageButton.setImageResource(R.drawable.ic_action_list);
                    flag=false;
                    listView.setVisibility(View.GONE);
                    relativemap.setVisibility(View.VISIBLE);
                    mapView.getController().animateTo(MyMessage.mylocation);

                }
                else{
                    imageButton.setImageResource(R.drawable.ic_action_map);
                    flag=true;
                    relativemap.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                }
            }
        });
        locatedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getController().animateTo(MyMessage.mylocation);
            }
        });
        prebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapcurrentcount==1){
                    Toast.makeText(PoilistActivity.this,"已是第一页",Toast.LENGTH_LONG).show();

                    return;
                }
                itemizedOverlay.removeAll();
                AsyncTask asyncTask = new AsyncTask() {
                    private ProgressDialog progressDialog;
                    @Override
                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(PoilistActivity.this);
                        progressDialog.setMessage("加载中,请稍候...");
                        progressDialog.show();
                        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                    }

                    @Override
                    protected Object doInBackground(Object... objects) {
                        mapcurrentcount--;
                        Log.e("qqqq",mapcurrentcount+"....");
                        if(mapcurrentcount==0){
                            mapcurrentcount=1;

                        }else{
                            JsonUtils.getData(title,mapcurrentcount,range,MyMessage.mylocation);
                            Log.e("qqqq",MyMessage.poiDetails.size()+"<<<<<");
                            for(int x=0;x<MyMessage.poiDetails.size();x++){
                                PoiDetail poiDetail = MyMessage.poiDetails.get(x);
                                Log.e("qqqq",poiDetail.getLocation().getLatitudeE6()+">>>>>"+poiDetail.getLocation().getLongitudeE6());
                                OverlayItem overlayItem1 = new OverlayItem(poiDetail.getLocation(),poiDetail.getName(),poiDetail.getDetail());
                                overlayItem1.setMarker(getResources().getDrawable(R.drawable.ic_loc_normal));
                                itemizedOverlay.addItem(overlayItem1);
                            }

                        }

                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        progressDialog.dismiss();
                        setCenter();
                        mapView.refresh();

                        super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                    }
                };
                asyncTask.execute();
            }
        });
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean aa = itemizedOverlay.removeAll();
                Log.e("asdasd",aa+"<><>");
                mapView.refresh();
                AsyncTask asyncTask = new AsyncTask() {
                    private ProgressDialog progressDialog;
                    @Override
                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(PoilistActivity.this);
                        progressDialog.setMessage("加载中,请稍候...");
                        progressDialog.show();
                        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                    }

                    @Override
                    protected Object doInBackground(Object... objects) {
                        mapcurrentcount++;
                        JsonUtils.getData(title,mapcurrentcount,range,MyMessage.mylocation);
                        if(MyMessage.isempty){
                            mapcurrentcount--;
                        }else{
                            for(int x=0;x<MyMessage.poiDetails.size();x++){
                                PoiDetail poiDetail = MyMessage.poiDetails.get(x);
                                OverlayItem overlayItem1 = new OverlayItem(poiDetail.getLocation(),poiDetail.getName(),poiDetail.getDetail());
                                overlayItem1.setMarker(getResources().getDrawable(R.drawable.ic_loc_normal));
                                itemizedOverlay.addItem(overlayItem1);
                            }

                        }

                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        progressDialog.dismiss();
                        if(MyMessage.isempty){
                            Toast.makeText(PoilistActivity.this,"暂无更多数据",Toast.LENGTH_LONG).show();
                        }else{
                        setCenter();
                        mapView.refresh();
                        }
                        super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                    }
                };
                asyncTask.execute();
            }
        });
    }

    private void initdistancebar() {
        distanceselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PoilistActivity.this);
                LayoutInflater inflater = LayoutInflater.from(PoilistActivity.this);
                View view1 = inflater.inflate(R.layout.distance, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                view1.setLayoutParams(params);


                builder.setView(view1);
                if(textView1!=null){
                    textView1.setText("加载更多");
                }
                final AlertDialog loginDialog = builder.create();
                loginDialog.show();
                TextView button1 = (TextView) view1.findViewById(R.id.button1);

                TextView button2 = (TextView) view1.findViewById(R.id.button2);

                TextView button3 = (TextView) view1.findViewById(R.id.button3);

                TextView button4 = (TextView) view1.findViewById(R.id.button4);

                TextView button5 = (TextView) view1.findViewById(R.id.button5);

                button1.setOnClickListener(new View.OnClickListener() {
                    private ProgressDialog progressDialog;
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) distanceselect.findViewById(R.id.distancemessage);
                        textView.setText("范围:1000m内");
                        range = "1000";
                        AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(PoilistActivity.this);
                                progressDialog.setMessage("加载中,请稍候...");
                                progressDialog.show();
                                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                            }

                            @Override
                            protected Object doInBackground(Object... objects) {


                                data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                progressDialog.dismiss();
                                Log.e("aaaaa",MyMessage.isempty+"");
                                if(MyMessage.isempty){
                                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                                }else{
                                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                                listView.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetInvalidated();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                        Intent intent = new Intent();
                                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                        intent.putExtra("name",hashMap.get("name").toString());
                                        intent.putExtra("detail",hashMap.get("detail").toString());
                                        startActivity(intent);

                                    }
                                });
                                }
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        };
                        asyncTask.execute();
                        loginDialog.dismiss();

                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) distanceselect.findViewById(R.id.distancemessage);
                        textView.setText("范围:2000m内");
                        range = "2000";
                        AsyncTask asyncTask = new AsyncTask() {
                            private ProgressDialog progressDialog;
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(PoilistActivity.this);
                                progressDialog.setMessage("加载中,请稍候...");
                                progressDialog.show();
                                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                            }

                            @Override
                            protected Object doInBackground(Object... objects) {


                                data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {

                                progressDialog.dismiss();
                                if(MyMessage.isempty){
                                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                                }else{
                                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                                listView.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetInvalidated();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                        Intent intent = new Intent();
                                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                        intent.putExtra("name",hashMap.get("name").toString());
                                        intent.putExtra("detail",hashMap.get("detail").toString());
                                        startActivity(intent);

                                    }
                                });
                                }
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        };
                        asyncTask.execute();
                        loginDialog.dismiss();
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) distanceselect.findViewById(R.id.distancemessage);
                        textView.setText("范围:3000m内");
                        range = "3000";

                        AsyncTask asyncTask = new AsyncTask() {
                            private ProgressDialog progressDialog;
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(PoilistActivity.this);
                                progressDialog.setMessage("加载中,请稍候...");
                                progressDialog.show();
                                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                            }

                            @Override
                            protected Object doInBackground(Object... objects) {


                                data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                progressDialog.dismiss();
                                if(MyMessage.isempty){
                                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                                }else{
                                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                                listView.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetInvalidated();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                        Intent intent = new Intent();
                                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                        intent.putExtra("name",hashMap.get("name").toString());
                                        intent.putExtra("detail",hashMap.get("detail").toString());
                                        startActivity(intent);

                                    }
                                });
                                }
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        };
                        asyncTask.execute();
                        loginDialog.dismiss();
                    }
                });
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) distanceselect.findViewById(R.id.distancemessage);
                        textView.setText("范围:4000m内");
                        range = "4000";
                        AsyncTask asyncTask = new AsyncTask() {
                            private ProgressDialog progressDialog;
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(PoilistActivity.this);
                                progressDialog.setMessage("加载中,请稍候...");
                                progressDialog.show();
                                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                            }

                            @Override
                            protected Object doInBackground(Object... objects) {


                                data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                progressDialog.dismiss();
                                if(MyMessage.isempty){
                                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                                }else{
                                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                                listView.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetInvalidated();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                        Intent intent = new Intent();
                                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                        intent.putExtra("name",hashMap.get("name").toString());
                                        intent.putExtra("detail",hashMap.get("detail").toString());
                                        startActivity(intent);

                                    }
                                });
                                }
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        };
                        asyncTask.execute();
                        loginDialog.dismiss();
                    }
                });
                button5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) distanceselect.findViewById(R.id.distancemessage);
                        textView.setText("范围:5000m内");
                        range = "5000";
                        AsyncTask asyncTask = new AsyncTask() {
                            private ProgressDialog progressDialog;
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(PoilistActivity.this);
                                progressDialog.setMessage("加载中,请稍候...");
                                progressDialog.show();
                                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                            }

                            @Override
                            protected Object doInBackground(Object... objects) {


                                data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                progressDialog.dismiss();
                                if(MyMessage.isempty){
                                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                                }else{
                                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                                listView.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetInvalidated();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                        Intent intent = new Intent();
                                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                        intent.putExtra("name",hashMap.get("name").toString());
                                        intent.putExtra("detail",hashMap.get("detail").toString());
                                        startActivity(intent);

                                    }
                                });
                                }
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        };
                        asyncTask.execute();
                        loginDialog.dismiss();
                    }
                });
            }
        });
    }

    private void inittitle() {
        TextView textView = (TextView) findViewById(R.id.titletextview);
        textView.setText(getIntent().getCharSequenceExtra("title"));
        ImageButton backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        ImageButton updatebutton = (ImageButton) findViewById(R.id.updatebutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask asyncTask = new AsyncTask() {
                    private ProgressDialog progressDialog;
                    @Override
                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(PoilistActivity.this);
                        progressDialog.setMessage("加载中,请稍候...");
                        progressDialog.show();
                        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                    }

                    @Override
                    protected Object doInBackground(Object... objects) {


                        data = JsonUtils.getData(title,1,range,MyMessage.mylocation);

                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        progressDialog.dismiss();
                        simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});


                        listView.setAdapter(simpleAdapter);
                        simpleAdapter.notifyDataSetInvalidated();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                HashMap<String,Object> hashMap = (HashMap<String, Object>) data.get(i);
                                Intent intent = new Intent();
                                intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                                intent.putExtra("name",hashMap.get("name").toString());
                                intent.putExtra("detail",hashMap.get("detail").toString());
                                startActivity(intent);

                            }
                        });
                        super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                    }
                };
                asyncTask.execute();
            }
        });
    }


    private void initlistView(){

        final ProgressDialog progressDialog = new ProgressDialog(PoilistActivity.this);

        progressDialog.setMessage("加载中,请稍候...");

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            protected Object doInBackground(Object... objects) {


                data = JsonUtils.getData(title,1,range,new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6)));

                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void onPostExecute(Object o) {
                progressDialog.dismiss();
                if(MyMessage.isempty){
                    Toast.makeText(PoilistActivity.this,"暂无数据",Toast.LENGTH_LONG).show();
                }else{
                simpleAdapter = new SimpleAdapter(PoilistActivity.this,data,R.layout.poilistitem,new String[]{"name","detail","distance"},new int[]{R.id.name,R.id.detail,R.id.distance});
                textView1 = new TextView(PoilistActivity.this);
                textView1.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView1.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                textView1.setPadding(0, 3, 0, 3);
                textView1.setText("加载更多");


                listView.addFooterView(textView1);

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


                                tempdata = JsonUtils.getData(title,currentCount,range,MyMessage.mylocation);
                                if(tempdata!=null){
                                    int totle = tempdata.size();
                                    Log.d("wwwww",tempdata.size()+">>>>>");

                                    for(int x=0;x<totle;x++){

                                        data.add(tempdata.get(x));
                                    }

                                }
                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
                                if(tempdata==null){
                                    //Toast.makeText(PoilistActivity.this,"暂无更多数据可以加载",Toast.LENGTH_LONG).show();
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
                        intent.setClass(PoilistActivity.this,PoiDetailsActivity.class);
                        intent.putExtra("name",hashMap.get("name").toString());
                        intent.putExtra("detail",hashMap.get("detail").toString());
                        startActivity(intent);

                    }
                });
                }
                super.onPostExecute(o);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        asyncTask.execute();



    }

    private void initmessagedata(){
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");
        messagenamedata.add("黄记煌三汁闷锅");
        messagedetaildata.add("西安南大街53号南附楼内3层");
        messagedistancedata.add("500m");
        messagenamedata.add("香港私家菜天子海鲜火锅");
        messagedetaildata.add("西安南大街");
        messagedistancedata.add("800m");

    }
    class MyItemizedOverlay<T> extends ItemizedOverlay{

        public MyItemizedOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("wwww","ontap"+i);
            com.baidu.mapapi.map.OverlayItem item = itemizedOverlay.getItem(i);
            MyMessage.endpoi = MyMessage.poiDetails.get(i);
            GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();
            MapView.LayoutParams layoutParam  = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);
            TextView textView1 = (TextView) popview.findViewById(R.id.nametext);
            TextView textView2 = (TextView) popview.findViewById(R.id.detailtext);
            textView1.setText(title);
            textView2.setText(content);


            popview.setLayoutParams(layoutParam);
            popview.setVisibility(View.VISIBLE);
            mapView.getController().animateTo(point);


            return super.onTap(i);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("wwww","ontap");
            popview.setVisibility(View.GONE);
            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
    @Override
    protected void onPause() {
        /**          amMapV
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        /**
         *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
         */
        mapView.destroy();
        super.onDestroy();
    }
    private void setCenter(){
        int lanmax=0;
        int lanmin=0;
        int lonmax=0;
        int lonmin=0;
        List<Integer>lanlist = new ArrayList<Integer>();
        List<Integer>lonlist = new ArrayList<Integer>();
        for(int x=0;x<MyMessage.poiDetails.size();x++){
            lanlist.add(MyMessage.poiDetails.get(x).getLocation().getLatitudeE6());
            lonlist.add(MyMessage.poiDetails.get(x).getLocation().getLongitudeE6());
        }
        mapView.getController().zoomToSpan(Collections.max(lanlist)-Collections.min(lanlist),Collections.max(lonlist)-Collections.min(lonlist));
    }
}
