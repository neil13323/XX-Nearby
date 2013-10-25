package com.neil.XXnearby;

import android.util.Log;
import android.widget.Toast;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/18/13
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonUtils {
    private static String url;
    private static String appkey="1742908036";
    private static String access_token="2.00ccqyLEwzDxtB05cff2f681g5nkYB";
    private String range;
    private int page;

    public static List<Map<String,Object>> getData(String poiname,int page,String range,GeoPoint geoPoint){
        String jsonurl = "https://api.weibo.com/2/location/pois/search/by_geo.json";
        String q = poiname;
        Log.e("asdasd",MyMessage.mylocation.getLatitudeE6()+">>++++>"+MyMessage.mylocation.getLongitudeE6());
        url = jsonurl +"?q="+q+"&access_token="+access_token+"&page="+page+"&range="+range+"&coordinate="+(float)MyMessage.mylocation.getLongitudeE6()/1E6+","+(float)MyMessage.mylocation.getLatitudeE6()/1E6+"&sr=1";
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        MyMessage.poiDetails = new ArrayList<PoiDetail>();
        try {
            JSONObject jsonObject = new JSONObject(getjson());
            jsonObject.opt("total");
            JSONArray jsonArray =jsonObject.optJSONArray("poilist");
            if(jsonArray==null){
                MyMessage.isempty =true;
                return null;
            }else{
                MyMessage.isempty=false;
            }
            for(int x=0;x<jsonArray.length();x++){
               JSONObject jsonObject1= jsonArray.optJSONObject(x);
                HashMap<String,Object> hashMap = new HashMap<String, Object>();
                String name =  jsonObject1.optString("name");
                String detail = jsonObject1.optString("address");
                String distance = jsonObject1.optString("distance")+"m";

                hashMap.put("name",name);
                hashMap.put("detail",detail);
                hashMap.put("distance",distance);
                hashMap.put("tel",jsonObject1.optString("tel"));

                PoiDetail poiDetail = new PoiDetail();
                poiDetail.setName(name);
                poiDetail.setDetail(detail);
                poiDetail.setDistance(distance);
                poiDetail.setPhone(jsonObject1.optString("tel"));
                float lon  =Float.parseFloat(jsonObject1.optString("x"));
                float lan  =Float.parseFloat(jsonObject1.optString("y"));
                Log.e("aaaaa",lon+"????"+lan);
                GeoPoint geoPoint1 = new GeoPoint((int)(lan*1E6),(int)(lon*1E6));
                hashMap.put("location",geoPoint1);
                poiDetail.setLocation(geoPoint1);
                MyMessage.poiDetails.add(poiDetail);
                data.add(hashMap);
                Log.d("wwwww",data.size()+"");
            }


        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return data;
    }

    private static String getjson() throws IOException {
        HttpGet httpGet = new HttpGet(url);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
        String str = EntityUtils.toString(httpResponse.getEntity());
        Log.d("wwww",str);
        return str;
    }
}
