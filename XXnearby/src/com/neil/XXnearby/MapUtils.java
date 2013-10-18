package com.neil.XXnearby;

import android.content.Context;
import com.baidu.mapapi.BMapManager;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/17/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapUtils {
    private  static BMapManager bMapManager;
    private final static String KEY = "3ad0f994e60f95a2383eb8f0d7498110";
    public static synchronized BMapManager getbMapManager(Context context) {
        if(bMapManager==null){
            bMapManager = new BMapManager(context);
            bMapManager.init(KEY,null);
        }
        return bMapManager;
    }

    public static void setbMapManager(BMapManager bMapManager) {
        MapUtils.bMapManager = bMapManager;
    }
}
