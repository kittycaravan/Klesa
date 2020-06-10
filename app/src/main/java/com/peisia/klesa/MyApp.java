package com.peisia.klesa;
import android.app.Application;
public class MyApp extends Application {
    static public final String INTENT_KEY_SERVICE_CMD = "INTENT_KEY_SERVICE_CMD";
    static public final String INTENT_KEY_SERVICE_TIME_NOTICE_TO_ACTIVITY = "INTENT_KEY_SERVICE_TIME_NOTICE_TO_ACTIVITY";
    static public final String INTENT_VALUE_SERVICE_START_WORLD_TIME = "INTENT_VALUE_SERVICE_START_WORLD_TIME";
    static public final String INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY = "INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY";
    static public final long WORLD_TIME_TERM_MS = 1000L;
    static public final long WORLD_TIME_TERM_TICK_SEC = 30L;
    static public final long WORLD_TIME_TERM_TICK_PREPARE_SEC = 5L;

    private long mWorldTime = 0L;    // 초기값 0 ms
    public long getmWorldTime() {
        return mWorldTime;
    }
    public void setmWorldTime(long mWorldTime) {
        this.mWorldTime = mWorldTime;
    }
}
