package com.peisia.klesa;
import android.app.Application;
public class MyApp extends Application {
    static public final String INTENT_KEY_CMD = "INTENT_KEY_CMD";
    static public final String INTENT_VALUE_START_WORLD_TIME = "INTENT_VALUE_START_WORLD_TIME";
    static public final long WORLD_TIME_TERM_MS = 1000L;

    private long mWorldTime = 0L;    // 초기값 0 ms
    public long getmWorldTime() {
        return mWorldTime;
    }
    public void setmWorldTime(long mWorldTime) {
        this.mWorldTime = mWorldTime;
    }
}
