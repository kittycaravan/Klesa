package com.peisia.klesa.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peisia.klesa.ActivityMain;
import com.peisia.klesa.MyApp;
import com.peisia.klesa.R;
import com.peisia.klesa.data.Player;
import com.peisia.klesa.service.ServiceWorldTime;
import com.peisia.klesa.ui.adapter.AdapterRecyclerInfoDisplay;
import com.peisia.klesa.ui.listdata.ListDataInfoDisplay;
import com.peisia.klesa.ui.listitem.ListItemInfoDisplay;
import com.peisia.klesa.util.klesa.UtilKlesaMap;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by 호양이 on 2019-08-22.
 */
public class FragmentHome extends Fragment {
    private Context mContext;
    private MyApp mMyApp;
    @BindView(R.id.fm_home_rv_info_display) RecyclerView mRv;
    @BindView(R.id.fm_home_et) EditText mEt;
    @BindView(R.id.fm_home_status_cl_tv) TextView mStatus;
    private LinearLayoutManager mLlm;
    AdapterRecyclerInfoDisplay mAdapterRecyclerInfoDisplay;
    ArrayList<ListItemInfoDisplay> mItems = new ArrayList<>();

    InputMethodManager imm;

    private String mLastInputText;
    private int mX; // mCoordinateX, 좌표 X
    private int mY; // mCoordinateY, 좌표 y
    private int mZ; // mCoordinateZ, 좌표 z

    private HashMap<Long, String> mMap;
    private long mCurrentXyz;
    private Player mPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mMyApp = (MyApp)((ActivityMain)mContext).getApplication();
        ButterKnife.bind(this, view);
        inits();    // 각종 초기화들
        mAdapterRecyclerInfoDisplay = new AdapterRecyclerInfoDisplay(mItems);
        mLlm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLlm);
        mRv.setAdapter(mAdapterRecyclerInfoDisplay);
        displayIntroduceWorld();    // 대문 표시
        startServiceWorldTime();    // 세계 시간을 시작
    }
    private void inits() {
        initLoadMap();     // 로드   : 방정보
        initLoadPlayer();   // 로드   : 플레이어
        initKeyboard();
        initPlayerXYZ();
    }
    private void initKeyboard(){
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEt, 0);
        mEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 주의. 전송(엔터) 누른 경우 이 onEditorAction 이 두번일어난다.
                // 한번은 event 가 null 인 채로 오고
                // 두번째는 event 가 있는 상태로 온다.
                // 따라서 아래처럼 null 이 아닐 때의 처리로 분기해야한다.
                if(event != null){
                    String inputText = v.getText().toString();
                    if(TextUtils.isEmpty(inputText)){
                        inputText = mLastInputText; // 공백 입력시 마지막 입력으로 대체한다.
                    } else {
                        mLastInputText = inputText; // 입력 편의를 위해 마지막 입력을 기억해두기
                    }
                    switch (v.getId()){
                        case R.id.fm_home_et:
                            Log.v("ASM","==== ==== 입력 값:"+inputText);
                            procUserTextInput(inputText);   // 입력값에 따른 처리 (ex. ㄷ 동 이동)
                            clearInputText();
                            reOpenKeyboard();
                            break;
                    }
                }
                return false;
            }
        });
    }
    private void procUserTextInput(String inputText) {
        switch(inputText){
            case "e":
            case "ㄷ":
                procPlayerMoveEast();
                break;
            case "w":
            case "ㅅ":
                procPlayerMoveWest();
                break;
            case "s":
            case "ㄴ":
                procPlayerMoveSouth();
                break;
            case "n":
            case "ㅂ":
                procPlayerMoveNorth();
                break;
        }

    }
    private void displayText(String s) {
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay(s)));
        mAdapterRecyclerInfoDisplay.notifyDataSetChanged();
        scrollEnd();
    }
    private void clearInputText() {
        mEt.setText("");
    }
    private void reOpenKeyboard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEt.requestFocus();
                imm.showSoftInput(mEt, 0);
            }
        },100); //1000, 500, 200
    }
    private void procPlayerMoveEast(){
        procMatchingRoom(1,0,0);
    }
    private void procPlayerMoveWest(){
        procMatchingRoom(-1, 0, 0);
    }
    private void procPlayerMoveNorth(){
        procMatchingRoom(0, 1, 0);
    }
    private void procPlayerMoveSouth(){
        procMatchingRoom(0, -1, 0);
    }
    private void scrollEnd(){
        mRv.scrollToPosition(mAdapterRecyclerInfoDisplay.getItemCount()-1); // 스크롤을 자동으로 맨 밑으로 가도록 처리
    }
    private void initPlayerXYZ(){
        mX = 1;
        mY = 1;
        mZ = 1;
    }
    private void displayRoom(long xyzCode){
        displayText(mMap.get(xyzCode));
    }
    /** 방좌표 코드와 매칭하여 방찾기 */
    private void procMatchingRoom(int x, int y, int z) {
        long inputCodeXyz = 1 * 1000  // 당분간 고정 값 1
                + (mX + x) * 100
                + (mY + y) * 10
                + (mZ + z) * 1;
        ////    탐색
        Log.v("ASM","==== ==== 널이냐 공백이냐? 널이네:" + mMap.get(inputCodeXyz));
        if(mMap.get(inputCodeXyz) == null) {
            displayText(getString(R.string.dp_player_move_cant));
        } else {    // 좌표 반영
            ////    vp 가 없으면 이동 못하게 처리, 있으면 처리
            if(mPlayer.getVpCurrent() < 1){
                displayText(getString(R.string.dp_info_status_vp_zero));
            } else {
                procPlayerStatus(0, 0, -1); // vp 1 감소 처리
                mX = mX + x;
                mY = mY + y;
                mZ = mZ + z;
                displayPlayerMove(x, y, z);
                displayRoom(UtilKlesaMap.xyzToXyzCode(mX, mY, mZ));  // 이동한 새 방 설명 표시 처리
            }
        }
    }
    private void displayPlayerMove(int x, int y, int z) {
        if(x == 1){
            displayText(getString(R.string.dp_player_move_e));   // 표시
        } else if(x == -1) {
            displayText(getString(R.string.dp_player_move_w));   // 표시
        } else if(y == 1) {
            displayText(getString(R.string.dp_player_move_n));   // 표시
        } else if(y == -1) {
            displayText(getString(R.string.dp_player_move_s));   // 표시
        } else if(z == 1) {

        } else if(z == -1) {

        }
    }
    private void initLoadMap(){
        mMap = new HashMap<>();
        mMap.put(1111L, "연습장 입구");
        mMap.put(1211L, "연습장 남서쪽");
        mMap.put(1221L, "연습장 북서쪽");
        mMap.put(1321L, "연습장 북동쪽");
        mMap.put(1311L, "연습장 남동쪽");
    }
    private void initLoadPlayer(){
//        mPlayer = new Player(10, 8, 3, 100, 50, 30);
        mPlayer = new Player(10, 8, 3, 100, 50, 30, 1, 1, 1);
        mPlayer.setCodeXyz(1111);
    }

    private void startServiceWorldTime(){
        Intent intent = new Intent(mContext, ServiceWorldTime.class);
        intent.putExtra(MyApp.INTENT_KEY_SERVICE_CMD, MyApp.INTENT_VALUE_SERVICE_START_WORLD_TIME);
        mContext.startService(intent);
        mContext.bindService(intent, ((ActivityMain)mContext).getConnection(), Context.BIND_AUTO_CREATE);
    }

    public void displayTickGodness(){
        displayText(getString(R.string.dp_info_tick_goddess));
    }
    public void displayTickGodnessPrepare(){
        displayText(getString(R.string.dp_info_tick_goddess_prepare));
    }
    /** 월드 시간 업데이트에 대한 처리. 즉 1초마다 할일 */
    public void procWorldTimeUpdate(){
        procTimeTick();
        //todo 또 뭘 처리할까. 이것저것 다 해야될껄?
        procTimeStatus();
    }
    private void procTimeTick(){
        ////    틱선녀 처리
        long currentWorldTime = mMyApp.getmWorldTime();
        if(currentWorldTime != 0){
            ////    틱선녀 처리 - 2.틱 지남 알림
            if((currentWorldTime/MyApp.WORLD_TIME_TERM_MS) % MyApp.WORLD_TIME_TERM_TICK_SEC == 0) {
                procPlayerRecoverByTick();
                displayTickGodness();
                ////    틱선녀 처리 - 1.틱 5초전 알림
            } else if(((currentWorldTime/MyApp.WORLD_TIME_TERM_MS) + MyApp.WORLD_TIME_TERM_TICK_PREPARE_SEC) % MyApp.WORLD_TIME_TERM_TICK_SEC == 0){
                displayTickGodnessPrepare();    //todo 백그라운드 내려가있을 때의 예외처리 해야함. 안그럼 죽어 자꾸. 아니면 백 상태에서 서비스가 안돌게 하던지.
            }
        }
    }
    private void displayIntroduceWorld(){
        displayText(getString(R.string.dp_info_world_enter));
    }
    /** 상태 처리.(상태창 갱신 등) */
    private void procTimeStatus(){
        int playerCurrentHp = mPlayer.getHpCurrent();
        int playerMaxHp = mPlayer.getHpMax();
        int playerCurrentMp = mPlayer.getMpCurrent();
        int playerMaxMp = mPlayer.getMpMax();
        int playerCurrentVp = mPlayer.getVpCurrent();
        int playerMaxVp = mPlayer.getVpMax();
        mStatus.setText(String.format(getString(R.string.dp_info_status_form), playerCurrentHp,playerMaxHp,playerCurrentMp,playerMaxMp,playerCurrentVp,playerMaxVp));
    }
    /** 플레이어 회복 처리 */
    private void procPlayerRecoverByTick(){
        int hpCurrent = mPlayer.getHpCurrent() + MyApp.PLAYER_TICK_RECOVER_HP_POINT;
        int mpCurrent = mPlayer.getMpCurrent() + MyApp.PLAYER_TICK_RECOVER_MP_POINT;
        int vpCurrent = mPlayer.getVpCurrent() + MyApp.PLAYER_TICK_RECOVER_VP_POINT;
        int hpMax = mPlayer.getHpMax();
        int mpMax = mPlayer.getMpMax();
        int vpMax = mPlayer.getVpMax();
        if(hpCurrent > hpMax){
            hpCurrent = hpMax;
        }
        if(mpCurrent > mpMax){
            mpCurrent = mpMax;
        }
        if(vpCurrent > vpMax){
            vpCurrent = vpMax;
        }
        mPlayer.setHpCurrent(hpCurrent);
        mPlayer.setMpCurrent(mpCurrent);
        mPlayer.setVpCurrent(vpCurrent);
    }
    /** 플레이어 스탯 가감 */
    private void procPlayerStatus(int addHp, int addMp, int addVp){
        int hpCurrent = mPlayer.getHpCurrent() + addHp;
        int mpCurrent = mPlayer.getMpCurrent() + addMp;
        int vpCurrent = mPlayer.getVpCurrent() + addVp;
        int hpMax = mPlayer.getHpMax();
        int mpMax = mPlayer.getMpMax();
        int vpMax = mPlayer.getVpMax();
        if(hpCurrent > hpMax){
            hpCurrent = hpMax;
        }
        if(mpCurrent > mpMax){
            mpCurrent = mpMax;
        }
        if(vpCurrent > vpMax){
            vpCurrent = vpMax;
        }
        mPlayer.setHpCurrent(hpCurrent);
        mPlayer.setMpCurrent(mpCurrent);
        mPlayer.setVpCurrent(vpCurrent);
    }
}