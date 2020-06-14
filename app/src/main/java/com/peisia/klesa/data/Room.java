package com.peisia.klesa.data;
public class Room {
    String name;
    String desc;
    long xyzCode;
    long xyz;
    public Room(String name, String desc, long xyzCode, long xyz) {
        this.name = name;
        this.desc = desc;
        this.xyzCode = xyzCode;
        this.xyz = xyz;
    }
    public Room(String name, String desc, long xyzCode) {
        this.name = name;
        this.desc = desc;
        this.xyzCode = xyzCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public long getXyzCode() {
        return xyzCode;
    }
    public void setXyzCode(long xyzCode) {
        this.xyzCode = xyzCode;
    }
    public long getXyz() {
        return xyz;
    }
    public void setXyz(long xyz) {
        this.xyz = xyz;
    }
}
