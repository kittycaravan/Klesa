package com.peisia.klesa.data;
import java.util.HashMap;
public class Room {
    String name;
    String desc;
    long xyzCode;
    long xyz;
    //todo 객체를 추가해야함. 연결 방에 대한
    //어떤식으로 했더라.. 방향코드와 방 고유값
    //동 EAST,
//    HashMap<Integer, Long> roomConnection;
    HashMap<Integer, Long> roomConnections = new HashMap<>();
    public Room(String name, String desc, long xyzCode, long xyz, HashMap<Integer, Long> roomConnections) {
        this.name = name;
        this.desc = desc;
        this.xyzCode = xyzCode;
        this.xyz = xyz;
        this.roomConnections = roomConnections;
    }
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
    public HashMap<Integer, Long> getRoomConnections() {
        return roomConnections;
    }
    public void setRoomConnections(HashMap<Integer, Long> roomConnections) {
        this.roomConnections = roomConnections;
    }
}
