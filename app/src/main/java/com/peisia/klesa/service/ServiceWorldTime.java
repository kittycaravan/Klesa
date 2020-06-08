package com.peisia.klesa.service;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.peisia.klesa.MyApp;

import java.util.Timer;
import java.util.TimerTask;
public class ServiceWorldTime extends Service {
    private MyApp mMyApp;
    private Timer timer;
    public ServiceWorldTime() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        mMyApp = (MyApp)getApplication();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return Service.START_STICKY;    // 비정상 종료 시 재시작 시켜줌
        } else {
            procCmd(intent);    // intent 가지고 정상 처리하기
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void procCmd(Intent intent){
        String cmd = intent.getStringExtra(MyApp.INTENT_KEY_CMD);
        switch (cmd){
            case MyApp.INTENT_VALUE_START_WORLD_TIME:
                procCmdRunWorldTime();
                break;
        }
    }

    private void procCmdRunWorldTime(){
        //todo 세계의 시간을 움직여라 =ㅅ= 흐흐..
        //아어 뭘로 구현할까.. 타이머?
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mMyApp.setmWorldTime(mMyApp.getmWorldTime() + MyApp.WORLD_TIME_TERM_MS); // 세계의 시간을 MyApp.WORLD_TIME_TERM_MS 만큼의 텀으로 갱신. 즉 이 단위로 계산을 처리한다는 뜻.
//                Log.v("ASM","==== ==== 세계의 시간이 흐른다! 신세계의 신 카미너스님! :"+mMyApp.getmWorldTime() + " ms 초");
//            }
//        }, MyApp.WORLD_TIME_TERM_MS);

        runTimer();
    }

    void runTimer(){
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                updateWorldTime();
            }
        };
        timer = new Timer();
        timer.schedule(tt, 0, MyApp.WORLD_TIME_TERM_MS);
    }
    private void updateWorldTime() {
        mMyApp.setmWorldTime(mMyApp.getmWorldTime() + MyApp.WORLD_TIME_TERM_MS); // 세계의 시간을 MyApp.WORLD_TIME_TERM_MS 만큼의 텀으로 갱신. 즉 이 단위로 계산을 처리한다는 뜻.
        Log.v("ASM", "==== ==== 세계의 시간이 흐른다! 신세계의 신 카미너스님! :" + mMyApp.getmWorldTime() + " ms 초");
    }
}
