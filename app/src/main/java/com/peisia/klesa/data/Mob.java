package com.peisia.klesa.data;
public class Mob {
    private String name;
    private int str;
    private int dex;
    private int iq;
    private int hpMax;
    private int mpMax;
    private int vpMax;
    private int hpCurrent;
    private int mpCurrent;
    private int vpCurrent;
    private int codeXyz;
    public Mob(String name, int str, int dex, int iq, int hpMax, int mpMax, int vpMax, int hpCurrent, int mpCurrent, int vpCurrent, int codeXyz) {
        this.name = name;
        this.str = str;
        this.dex = dex;
        this.iq = iq;
        this.hpMax = hpMax;
        this.mpMax = mpMax;
        this.vpMax = vpMax;
        this.hpCurrent = hpCurrent;
        this.mpCurrent = mpCurrent;
        this.vpCurrent = vpCurrent;
        this.codeXyz = codeXyz;
    }
    public Mob(int str, int dex, int iq, int hpMax, int mpMax, int vpMax) {
        this.str = str;
        this.dex = dex;
        this.iq = iq;
        this.hpMax = hpMax;
        this.mpMax = mpMax;
        this.vpMax = vpMax;
    }
    public Mob(int str, int dex, int iq, int hpMax, int mpMax, int vpMax, int hpCurrent, int mpCurrent, int vpCurrent) {
        this.str = str;
        this.dex = dex;
        this.iq = iq;
        this.hpMax = hpMax;
        this.mpMax = mpMax;
        this.vpMax = vpMax;
        this.hpCurrent = hpCurrent;
        this.mpCurrent = mpCurrent;
        this.vpCurrent = vpCurrent;
    }
    @Override
    public String toString() {
        return "Player{" +
                "str=" + str +
                ", dex=" + dex +
                ", iq=" + iq +
                ", hpMax=" + hpMax +
                ", mpMax=" + mpMax +
                ", vpMax=" + vpMax +
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
    public int getHpMax() {
        return hpMax;
    }
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    public int getMpMax() {
        return mpMax;
    }
    public void setMpMax(int mpMax) {
        this.mpMax = mpMax;
    }
    public int getVpMax() {
        return vpMax;
    }
    public void setVpMax(int vpMax) {
        this.vpMax = vpMax;
    }
    public int getCodeXyz() {
        return codeXyz;
    }
    public void setCodeXyz(int codeXyz) {
        this.codeXyz = codeXyz;
    }
    public int getHpCurrent() {
        return hpCurrent;
    }
    public void setHpCurrent(int hpCurrent) {
        this.hpCurrent = hpCurrent;
    }
    public int getMpCurrent() {
        return mpCurrent;
    }
    public void setMpCurrent(int mpCurrent) {
        this.mpCurrent = mpCurrent;
    }
    public int getVpCurrent() {
        return vpCurrent;
    }
    public void setVpCurrent(int vpCurrent) {
        this.vpCurrent = vpCurrent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
