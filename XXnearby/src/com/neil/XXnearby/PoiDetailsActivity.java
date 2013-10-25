package com.neil.XXnearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PoiDetailsActivity extends Activity {
    private static final int WALK = 1;
    private static final int BUS = 2;
    private static final int DRIVER = 3;
    private MapView mapView;
    private View popview;
    private TextView distancetext,nametext,detailtext,phonetext;
    private ImageButton backbutton,phonebutton;
    private LayoutInflater layoutInflater;
    private GeoPoint mylocation,startlocation,endlocation;
    private MyItemizedOverlay<OverlayItem> itemizedOverlay;
    private RadioButton radioButton1,radioButton2,radioButton3;
    private MKSearch mkSearch = new MKSearch();
    private RouteOverlay routeOverlay;
    private TransitOverlay transitOverlay;
    private MKRoute route;
    private String dis;
    private List<ArrayList<GeoPoint>> geoPoints;
    private BMapManager app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        DemoApplication demoApplication = (DemoApplication) getApplication();
        app = demoApplication.mBMapManager;
        if (demoApplication.mBMapManager==null){
            demoApplication.mBMapManager = new BMapManager(this);
            demoApplication.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poimapdetails);
        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.bmapView);
        mylocation = MyMessage.mylocation;
        startlocation = MyMessage.mylocation;
        endlocation = MyMessage.endpoi.getLocation();
        radioButton1 = (RadioButton) findViewById(R.id.footbutton);
        radioButton2 = (RadioButton) findViewById(R.id.busbutton);
        radioButton3 = (RadioButton) findViewById(R.id.carbutton);
        backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        distancetext = (TextView) findViewById(R.id.distancemessage);
        nametext = (TextView) findViewById(R.id.locationname);
        detailtext = (TextView) findViewById(R.id.locationaddress);
        phonebutton = (ImageButton) findViewById(R.id.phonebutton);
        phonetext = (TextView) findViewById(R.id.phonenumber);
        init();
        radioButton1.setChecked(true);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MKPlanNode stNode = new MKPlanNode();
                stNode.pt =MyMessage.mylocation;
                MKPlanNode enNode = new MKPlanNode();
                enNode.pt = MyMessage.endpoi.getLocation();
                mkSearch.walkingSearch(MyMessage.mycity, stNode, MyMessage.mycity, enNode);

            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MKPlanNode stNode = new MKPlanNode();
                stNode.pt =MyMessage.mylocation;
                MKPlanNode enNode = new MKPlanNode();
                enNode.pt = MyMessage.endpoi.getLocation();
                mkSearch.transitSearch(MyMessage.mycity, stNode, enNode);

            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MKPlanNode stNode = new MKPlanNode();
                stNode.pt =MyMessage.mylocation;
                MKPlanNode enNode = new MKPlanNode();
                enNode.pt = MyMessage.endpoi.getLocation();
                mkSearch.drivingSearch(MyMessage.mycity,stNode,MyMessage.mycity,enNode);

            }
        });

    }

    private void init() {
        initmap();
        inittitle();
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
    private void inittitle() {
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initmap() {
        mkSearch.init(app, new MKSearchListener(){

            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                                                int error) {
                route= null;
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(PoiDetailsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                    mapView.getOverlays().clear();
                    return;
                }

                geoPoints= res.getPlan(0).getRoute(0).getArrayPoints();

                routeOverlay = new RouteOverlay(PoiDetailsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
//                //重置路线节点索引，节点浏览时使用
//                nodeIndex = -1;
                if(route==null){
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                }else{
                    distancetext.setTextSize(25);
                    distancetext.setText("距离大约为:"+route.getDistance()+"m,"+getTime(route.getDistance(),DRIVER));
                }
            }

            public void onGetTransitRouteResult(MKTransitRouteResult res,
                                                int error) {
                route= null;
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(PoiDetailsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                    mapView.getOverlays().clear();
                    return;
                }

                geoPoints= res.getPlan(0).getRoute(0).getArrayPoints();
                transitOverlay = new TransitOverlay (PoiDetailsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                transitOverlay.setData(res.getPlan(0));
                //清除其他图层
                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(transitOverlay);
                //执行刷新使生效
                mapView.refresh();

                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //重置路线节点索引，节点浏览时使用
                route =  res.getPlan(0).getRoute(0);
                MKLine mkLine = new MKLine();
                mapView.getController().enableClick(true);
                if(route==null){
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                }else{


                    distancetext.setTextSize(25);
                    distancetext.setText("距离大约为:"+res.getPlan(0).getDistance()+"m"+getTime(res.getPlan(0).getDistance(),BUS));
                }
            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                                                int error) {
                route= null;
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(PoiDetailsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                    mapView.getOverlays().clear();
                    return;
                }

                geoPoints= res.getPlan(0).getRoute(0).getArrayPoints();
                routeOverlay = new RouteOverlay(PoiDetailsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route =  res.getPlan(0).getRoute(0);
                if(route==null){
                    distancetext.setTextSize(20);
                    distancetext.setText("暂无距离信息");
                }else{
                    distancetext.setTextSize(25);
                    distancetext.setText("距离大约为:"+route.getDistance()+"m"+getTime(route.getDistance(),WALK));
                }

            }
            public void onGetAddrResult(MKAddrInfo res, int error) {
            }
            public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

            @Override
            public void onGetPoiDetailSearchResult(int type, int iError) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type,
                                            int error) {
                // TODO Auto-generated method stub

            }
        });
        nametext.setText(MyMessage.endpoi.getName());
        detailtext.setText(MyMessage.endpoi.getDetail());
        mapView.getController().setCenter(MyMessage.mylocation);
        mapView.getController().setZoom(18);
        itemizedOverlay = new MyItemizedOverlay<OverlayItem>(getResources().getDrawable(R.drawable.u71_normal),mapView);
        MyItemizedOverlay<OverlayItem> mylocationOverlay = new MyItemizedOverlay<OverlayItem>(getResources().getDrawable(R.drawable.u71_normal),mapView);
        OverlayItem overlayItem = new OverlayItem(new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6)),"这是我的位置","这是我位置的描述");
        mylocationOverlay.addItem(overlayItem);
        OverlayItem overlayItem1 = new OverlayItem(new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6)),"这是起点","这是起点的描述");
        overlayItem1.setMarker(getResources().getDrawable(R.drawable.ic_loc_from));
        itemizedOverlay.addItem(overlayItem1);
        OverlayItem overlayItem2 = new OverlayItem(new GeoPoint((int) (34.25934463685013 * 1E6+4000), (int) (108.94721031188965 * 1E6+4000)),getIntent().getStringExtra("name"),getIntent().getStringExtra("detail"));
        overlayItem2.setMarker(getResources().getDrawable(R.drawable.ic_loc_to));
        itemizedOverlay.addItem(overlayItem2);
        if(route==null){
            distancetext.setTextSize(20);
            distancetext.setText("暂无距离信息");
        }else{
            distancetext.setTextSize(25);
            distancetext.setText("距离大约为:"+route.getDistance()+"m");
        }
        if(MyMessage.endpoi.getPhone().equals("")){
            phonetext.setText("暂无电话");
        }else{
            phonetext.setText(MyMessage.endpoi.getPhone());

            phonebutton.setOnClickListener(new View.OnClickListener() {
                private String phone_number;
                private AlertDialog loginDialog;
                @Override
                public void onClick(View view) {
                    phone_number = MyMessage.endpoi.getPhone().trim();

                    String[] phonenumber = phone_number.split(";");
                    if(phonenumber.length>=2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PoiDetailsActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(PoiDetailsActivity.this);
                        LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.phonenumber, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.FILL_PARENT,
                                LinearLayout.LayoutParams.FILL_PARENT);
                        view1.setLayoutParams(params);
                        for(int x=0;x<phonenumber.length;x++){
                            TextView textView = new TextView(PoiDetailsActivity.this);
                            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            textView.setText(phonenumber[x]);
                            textView.setTextSize(24);
                            textView.setPadding(2,5,2,5);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView textView1 = (TextView) view;
                                    phone_number = textView1.getText().toString();
                                    loginDialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                                    PoiDetailsActivity.this.startActivity(intent);//内部类
                                }
                            });
                            ImageView imageView = new ImageView(PoiDetailsActivity.this);
                            imageView.setImageResource(R.drawable.u244_line);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            view1.addView(textView);
                            if(x==phonenumber.length-1){
                                continue;
                            }else{
                                view1.addView(imageView);
                            }
                        }

                        builder.setView(view1);

                        loginDialog = builder.create();
                        loginDialog.show();
                    }else{


                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                        PoiDetailsActivity.this.startActivity(intent);//内部类
                    }
                }
            });
        }
        MKPlanNode stNode = new MKPlanNode();
        stNode.pt = MyMessage.mylocation;
        MKPlanNode enNode = new MKPlanNode();
        enNode.pt = MyMessage.endpoi.getLocation();
        mkSearch.walkingSearch(MyMessage.mycity, stNode, MyMessage.mycity, enNode);


    }

    class MyItemizedOverlay<T> extends ItemizedOverlay {

        public MyItemizedOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("wwww", "ontap" + i);
            com.baidu.mapapi.map.OverlayItem item = itemizedOverlay.getItem(i);
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
    private String getTime(int len,int type){
        int h=0;
        int m=0;
        int s=0;
        if(type==WALK){
            double time  = (float)len/1.8;
            if(time>60){
               m=(int)(time/60);
            } else if (time>3600){
               h=(int)(time/3600);
            }
            s=(int)(time-(int)60*m+(int)3600*h);
        }
        if(type==BUS){
            double time  = (float)len/5.5;
            if(time>60){
                m=(int)(time/60);
            } else if (time>3600){
                h=(int)(time/3600);
            }
            s=(int)(time-(int)60*m+(int)3600*h);
        }
        if(type==DRIVER){
            double time  = (float)len/8;
            if(time>60){
                m=(int)(time/60);
            } else if (time>3600){
                h=(int)(time/3600);
            }
            s=(int)(time-(int)60*m+(int)3600*h);
        }
        String str="路程大约需花费";
        if(h!=0){
           str+=h+"小时";
        }else if(m!=0){
            str+=m+"分钟";
        }else if(s!=0){
            str+=s+"秒";
        }
        return str;
    }

}
