package com.peisia.klesa.service;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.peisia.klesa.MyApp;

import java.util.Timer;
import java.util.TimerTask;
public class ServiceWorldTime extends Service {
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_SEND_TO_ACTIVITY_WORLD_TIME_TICK = 2;
    private Messenger mClient = null;   // Activity 에서 가져온 Messenger

    private MyApp mMyApp;
    private Timer mTimer;
    public ServiceWorldTime() { }
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
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
        mTimer.cancel();
        super.onDestroy();
    }
    private void procCmd(Intent intent) {
        String cmd = intent.getStringExtra(MyApp.INTENT_KEY_SERVICE_CMD);
        switch (cmd){
            case MyApp.INTENT_VALUE_SERVICE_START_WORLD_TIME:
                procCmdRunWorldTime();
                break;
        }
    }

    private void procCmdRunWorldTime(){
        if(mTimer == null) {    // 타이머 중복 실행 방지
            runTimer();
        }
    }

    void runTimer(){
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                updateWorldTime();
            }
        };
        mTimer = new Timer();
        mTimer.schedule(tt, 0, MyApp.WORLD_TIME_TERM_MS);
    }
    private void updateWorldTime() {
        long currentWorldTime = mMyApp.getmWorldTime();
        currentWorldTime += MyApp.WORLD_TIME_TERM_MS;   // 시간을 증가
        mMyApp.setmWorldTime(currentWorldTime); // 세계의 시간을 MyApp.WORLD_TIME_TERM_MS 만큼의 텀으로 갱신. 즉 이 단위로 계산을 처리한다는 뜻.
        Log.v("ASM", "==== ==== 세계의 시간이 흐른다! 신세계의 신 카미너스님! :" + mMyApp.getmWorldTime() + " ms 초");

        sendUpdateWorldTime();  Log.v("ASM","==== ==== 서비스쪽에서 세계 시간 업데이트 신호 보냄");
//        if((currentWorldTime/1000) % MyApp.WORLD_TIME_TERM_TICK_SEC == 0 && currentWorldTime != 0){
//            sendUpdateWorldTime();  Log.v("ASM","==== ==== 틱선녀 부활!");
//        }
    }
    /** 틱선녀가 왔다! */
    private void sendUpdateWorldTime() {
        try{
            Bundle bundle = new Bundle();
//            bundle.putInt("tick",1);  // 데이터 전송 참고 코드. 남겨놓으시오.
            Message msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY_WORLD_TIME_TICK);
            msg.setData(bundle);
            mClient.send(msg);  //msg 보내기
        } catch (Exception e) {}
    }

    /** activity로부터 binding 된 Messenger */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.w("test","ControlService - message what : "+msg.what +" , msg.obj "+ msg.obj);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClient = msg.replyTo;  // activity로부터 가져온
                    break;
            }
            return false;
        }
    }));
}