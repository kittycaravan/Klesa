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
    private Timer timer;
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
        long currentWorldTime = mMyApp.getmWorldTime();
        currentWorldTime += MyApp.WORLD_TIME_TERM_MS;   // 시간을 증가
        mMyApp.setmWorldTime(currentWorldTime); // 세계의 시간을 MyApp.WORLD_TIME_TERM_MS 만큼의 텀으로 갱신. 즉 이 단위로 계산을 처리한다는 뜻.
        Log.v("ASM", "==== ==== 세계의 시간이 흐른다! 신세계의 신 카미너스님! :" + mMyApp.getmWorldTime() + " ms 초");

        //todo 액티로 데이터 보내기
        if((currentWorldTime/1000) % MyApp.WORLD_TIME_TERM_TICK == 0 && currentWorldTime != 0){
            Log.v("ASM","==== ==== 틱선녀 부활!");
            sendNewComesTickGodness();
        }

/*        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        intent.putExtra(MyApp.INTENT_KEY_SERVICE_TIME_NOTICE_TO_ACTIVITY, MyApp.INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY);
        startActivity(intent);*/
    }
    /** 틱선녀가 왔다! */
    private void sendNewComesTickGodness() {
        try{
            Bundle bundle = new Bundle();
            bundle.putInt("tick",1);
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