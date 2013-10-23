package com.neil.XXnearby;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PoiDetailsActivity extends Activity {

    private MapView mapView;
    private View popview;
    private TextView distancetext,nametext,detailtext;
    private ImageButton backbutton;
    private LayoutInflater layoutInflater;
    private GeoPoint mylocation,startlocation,endlocation;
    private MyItemizedOverlay<OverlayItem> itemizedOverlay;
    private RadioButton radioButton1,radioButton2,radioButton3;
    private MKSearch mkSearch = new MKSearch();
    private RouteOverlay routeOverlay;
    private TransitOverlay transitOverlay;
    private MKRoute route;
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
        mkSearch.init(demoApplication.mBMapManager,null);
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
                mkSearch.transitSearch(MyMessage.mycity,stNode,enNode);
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
                    return;
                }


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

            }

            public void onGetTransitRouteResult(MKTransitRouteResult res,
                                                int error) {
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
                    return;
                }


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
            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                                                int error) {
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
                    return;
                }


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

        MKPlanNode stNode = new MKPlanNode();
        stNode.pt = MyMessage.mylocation;
        MKPlanNode enNode = new MKPlanNode();
        enNode.pt = MyMessage.endpoi.getLocation();
        mkSearch.walkingSearch(MyMessage.mycity, stNode, MyMessage.mycity, enNode);


//
//        MKRoute mkRoute = new MKRoute();
//
//        GeoPoint[] geoPoint = new GeoPoint[2];
//        geoPoint[0] =startlocation;
//        geoPoint[1] = endlocation;
//
//        mkRoute.customizeRoute(startlocation,endlocation,geoPoint);
//
//        routeOverlay.setData(mkRoute);
//        mapView.getOverlays().add(mylocationOverlay);
//        mapView.getOverlays().add(routeOverlay);
//
//
//        popview = layoutInflater.inflate(R.layout.popitem, null);
//        popview.setVisibility(View.GONE);
//        mapView.addView(popview);
//        mapView.refresh();

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
}
