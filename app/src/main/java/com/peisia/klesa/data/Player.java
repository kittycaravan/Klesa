package com.peisia.klesa.data;
public class Player {
    private int str;
    private int dex;
    private int iq;
    private int hp;
    private int mp;
    private int vp;
    private int codeXyz;
    public Player(int str, int dex, int iq, int hp, int mp, int vp) {
        this.str = str;
        this.dex = dex;
        this.iq = iq;
        this.hp = hp;
        this.mp = mp;
        this.vp = vp;
    }
    @Override
    public String toString() {
        return "Player{" +
                "str=" + str +
                ", dex=" + dex +
                ", iq=" + iq +
                ", hp=" + hp +
                ", mp=" + mp +
                ", vp=" + vp +
                '}';
    }
    public int getStr() {
        return str;
    }
    public void setStr(int str) {
        this.str = str;
    }
    public int getDex() {
        return dex;
    }
    public void setDex(int dex) {
        this.dex = dex;
    }
    public int getIq() {
        return iq;
    }
    public void setIq(int iq) {
        this.iq = iq;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getMp() {
        return mp;
    }
    public void setMp(int mp) {
        this.mp = mp;
    }
    public int getVp() {
        return vp;
    }
    public void setVp(int vp) {
        this.vp = vp;
    }
    public int getCodeXyz() {
        return codeXyz;
    }
    public void setCodeXyz(int codeXyz) {
        this.codeXyz = codeXyz;
    }
}
