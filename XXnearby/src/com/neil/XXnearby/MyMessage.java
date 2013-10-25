package com.neil.XXnearby;

import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/21/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyMessage {
    public static GeoPoint mylocation;
    public static List<PoiDetail> poiDetails = new ArrayList<PoiDetail>();
    public static PoiDetail endpoi;
    public static String myaddress;
    public static String mycity;
    public static boolean isempty = false;

}
