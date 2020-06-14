package com.peisia.klesa;
import android.app.Application;
public class MyApp extends Application {
    static public final String INTENT_KEY_SERVICE_CMD = "INTENT_KEY_SERVICE_CMD";
    static public final String INTENT_KEY_SERVICE_TIME_NOTICE_TO_ACTIVITY = "INTENT_KEY_SERVICE_TIME_NOTICE_TO_ACTIVITY";
    static public final String INTENT_VALUE_SERVICE_START_WORLD_TIME = "INTENT_VALUE_SERVICE_START_WORLD_TIME";
    static public final String INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY = "INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY";
    static public final long WORLD_TIME_TERM_MS = 1000L;
    static public final long WORLD_TIME_TERM_TICK_SEC = 30L;
    static public final long WORLD_TIME_TERM_TICK_PREPARE_SEC = 10L;
    ////    플레이어
    static public final int PLAYER_TICK_RECOVER_HP_POINT = 20;
    static public final int PLAYER_TICK_RECOVER_MP_POINT = 5;
    static public final int PLAYER_TICK_RECOVER_VP_POINT = 10;
    ////    플레이어 - 이동 입력 편의기능을 위한 기능 상수값
    static public final long PLAYER_MOVE_INPUT_WAIT_TIME = 2000;  // 이동 입력 후 몇 초 안으로 추가 입력이 없을 시 이동 자동 처리.

    private long mWorldTime = 0L;    // 초기값 0 ms
    public long getmWorldTime() {
        return mWorldTime;
    }
    public void setmWorldTime(long mWorldTime) {
        this.mWorldTime = mWorldTime;
    }
}
