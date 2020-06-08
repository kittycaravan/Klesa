package com.peisia.klesa.util;
public class UtilKlesaMap {
    static public long xyzToXyzCode(int x, int y, int z){
        return (long)(1 * 1000
                + x * 100
                + y * 10
                + z * 1);
    }
}
