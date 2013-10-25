package com.neil.XXnearby;

import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/21/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PoiDetail implements Serializable {
    private int id;
    private String name;
    private String detail;
    private GeoPoint location;
    private String phone;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
