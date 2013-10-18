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
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PoiDetailsActivity extends Activity {
    private final static String KEY = "3ad0f994e60f95a2383eb8f0d7498110";
    private MapView mapView;
    private View popview;
    private ImageButton backbutton;
    private LayoutInflater layoutInflater;
    private GeoPoint mylocation,startlocation,endlocation;
    private MyItemizedOverlay<OverlayItem> itemizedOverlay;
    private RadioButton radioButton1,radioButton2,radioButton3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        DemoApplication demoApplication = (DemoApplication) getApplication();
        if (demoApplication.mBMapManager==null){
            demoApplication.mBMapManager = new BMapManager(this);
            demoApplication.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poimapdetails);
        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.bmapView);
        mylocation = new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6));
        startlocation = mylocation;
        endlocation = new GeoPoint((int) (34.25934463685013 * 1E6+4000), (int) (108.94721031188965 * 1E6+4000));
        radioButton1 = (RadioButton) findViewById(R.id.footbutton);
        radioButton2 = (RadioButton) findViewById(R.id.busbutton);
        radioButton3 = (RadioButton) findViewById(R.id.carbutton);
        backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        init();



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
        mapView.getController().setCenter(mylocation);
        mapView.getController().setZoom(14);
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
        RouteOverlay routeOverlay = new RouteOverlay(this,mapView);
        MKRoute mkRoute = new MKRoute();

        GeoPoint[] geoPoint = new GeoPoint[2];
        geoPoint[0] =startlocation;
        geoPoint[1] = endlocation;

        mkRoute.customizeRoute(startlocation,endlocation,geoPoint);

        routeOverlay.setData(mkRoute);
        mapView.getOverlays().add(mylocationOverlay);
        mapView.getOverlays().add(routeOverlay);


        popview = layoutInflater.inflate(R.layout.popitem, null);
        popview.setVisibility(View.GONE);
        mapView.addView(popview);
        mapView.refresh();

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
