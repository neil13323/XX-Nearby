package com.neil.XXnearby.model;

/**
 * Created with IntelliJ IDEA.
 * User: administrator
 * Date: 10/22/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cat {
    private String name;
    private boolean havechild;
    public Cat(String name,boolean havechild){
        this.name = name;
        this.havechild = havechild;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHavechild() {
        return havechild;
    }

    public void setHavechild(boolean havechild) {
        this.havechild = havechild;
    }
}
