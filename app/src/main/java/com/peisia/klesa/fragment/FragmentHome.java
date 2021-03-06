package com.peisia.klesa.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.peisia.klesa.data.Mob;
import com.peisia.klesa.data.Player;
import com.peisia.klesa.data.Room;
import com.peisia.klesa.data.Spatiotemporal;
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
    private AdapterRecyclerInfoDisplay mAdapterRecyclerInfoDisplay;
    private ArrayList<ListItemInfoDisplay> mItems = new ArrayList<>();
    private InputMethodManager imm;
    private String mLastInputText;
    private int mX, mY, mZ; // mCoordinateX, 좌표 X
    private HashMap<Long, Room> mRooms;   // todo 아래 mRooms로 변경
    private long mCurrentXyz;
    private Player mPlayer;
    private ArrayList<Mob> mMobs;
    private long mInputTxtTimeBefore = 0L;
    private Spatiotemporal mSpatiotemporal;
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
        initLoadMobs();
        initKeyboard();
        initPlayerXYZ();
    }
    private void initLoadMobs() {
        mMobs = new ArrayList<>();
        mMobs.add(new Mob("쥐",5,5,5,5,5,5,5,5,5,1111));
        mMobs.add(new Mob("쥐",5,5,5,5,5,5,5,5,5,1111));
        mMobs.add(new Mob("쥐",5,5,5,5,5,5,5,5,5,1111));
        mMobs.add(new Mob("쥐",5,5,5,5,5,5,5,5,5,1111));
    }
    private void initKeyboard(){
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEt, 0);
        mEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 주의. 전송(엔터) 누른 경우 이 onEditorAction 이 두번일어난다. 한번은 event 가 null 인 채로 오고
                // 두번째는 event 가 있는 상태로 온다. 따라서 아래처럼 null 이 아닐 때의 처리로 분기해야한다.
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
        mEt.addTextChangedListener(new TextWatcher() {  // 입력 변화 리스너로 첫 입력 후 두번째 입력이 n 초 후에 이뤄지지 않으면 아래 처리를 함. 단 이동에 대해서만.
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("ASM","==== ==== 텍스트 변화를 알 수 있다고? :"+s);
                ////    자동입력 처리에서 처리후 입력텍스트를 ""로 초기화 하는데 이 행위 자체로 여기가 호출되고,
                ////    이 조건을 안넣으면 입력시간을 기록해버리게 되므로 조건 추가함.
                if(!TextUtils.isEmpty(s.toString())){
                    mInputTxtTimeBefore = mMyApp.getmWorldTime();   //1.첫 입력한 현재 시간을 기록
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
    private void procUserTextInput(String inputText) {
        switch(inputText){
            case "e": case "ㄷ":
                //todo 새로운 처리 방식 메서드로 처리
//                procPlayerMoveEast();
                Log.v("hoyangi","======== 여기 안옴????????????");
                procPlayerMove(MyApp.DIRECTION_EAST);
                break;
            case "w": case "ㅅ":
                //todo 새로운 처리 방식 메서드로 처리
//                procPlayerMoveWest();
                procPlayerMove(MyApp.DIRECTION_WEST);
                break;
            case "s": case "ㄴ":
                //todo 새로운 처리 방식 메서드로 처리
//                procPlayerMoveSouth();
                procPlayerMove(MyApp.DIRECTION_SOUTH);
                break;
            case "n": case "ㅂ":
                //todo 새로운 처리 방식 메서드로 처리
//                procPlayerMoveNorth();
                procPlayerMove(MyApp.DIRECTION_NORTH);
                break;
        }

    }
    private void procPlayerMove(int direction) {
        //todo 플레이어의 현재 위치 검사
        long currentPlayerXyz = mMyApp.getCurrentPlayerXyz();
        Room currentRoom = mRooms.get(currentPlayerXyz);

        //다음방 연결값이 있는지 검사
        if(currentRoom.getRoomConnections().get(direction) == null){
            //todo 이동 불가처리
            Log.v("hoyangi","==== 이동 불가염...");
            displayText(getString(R.string.dp_player_move_cant));
        } else {    //todo 이동 처리
            long nextRoomXyz = currentRoom.getRoomConnections().get(direction);
            Log.v("hoyangi","======== nextRoomXyz 값 검사:"+nextRoomXyz);

            //todo 이동력 소모 시키기
            ////    vp 가 없으면 이동 못하게 처리, 있으면 처리
            if(mPlayer.getVpCurrent() < 1){
                displayText(getString(R.string.dp_info_status_vp_zero));
            } else {
                procPlayerStatus(0, 0, -1); // vp 1 감소 처리
                //todo 상태창 갱신하기
                procTimeStatus();
                //todo 플레이어가 움직인 방향 표시
                displayPlayerMoveNew(direction);
                //todo 현재 방 정보 표시
                displayRoom(nextRoomXyz);
                mMyApp.setCurrentPlayerXyz(nextRoomXyz);    // 현재 방 위치를 바꿈
            }
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

        mMyApp.setCurrentPlayerXyz(1251110101001001001L);   // todo 일단 플레이어 시작 위치는 연습장 입구로 지정함. 임시.
    }
    private void displayRoom(long xyzCode){
        displayText("["+ mRooms.get(xyzCode).getName()+"]");   // Room 이름 표시
        displayText(mRooms.get(xyzCode).getDesc());               // Room 설명 표시
    }
    /** 방좌표 코드와 매칭하여 방찾기 */
    private void procMatchingRoom(int x, int y, int z) {
        long inputCodeXyz = 1 * 1000  // 당분간 고정 값 1
                + (mX + x) * 100
                + (mY + y) * 10
                + (mZ + z) * 1;
        ////    탐색
        Log.v("ASM","==== ==== 널이냐 공백이냐? 널이네:" + mRooms.get(inputCodeXyz));
        if(mRooms.get(inputCodeXyz) == null) {
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
    private void displayPlayerMoveNew(int direction) {
        if(direction == MyApp.DIRECTION_EAST){
            displayText(getString(R.string.dp_player_move_e));   // 표시
        } else if(direction == MyApp.DIRECTION_WEST) {
            displayText(getString(R.string.dp_player_move_w));   // 표시
        } else if(direction == MyApp.DIRECTION_SOUTH) {
            displayText(getString(R.string.dp_player_move_s));   // 표시
        } else if(direction == MyApp.DIRECTION_NORTH) {
            displayText(getString(R.string.dp_player_move_n));   // 표시
        }
    }
    private void initLoadMap(){
        mSpatiotemporal = new Spatiotemporal();
        mRooms = mSpatiotemporal.loadRooms();
    }
    private void initLoadPlayer(){
        mPlayer = new Player(10, 8, 3, 100, 50, 30, 1, 1, 10);
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
    /************************************************************
     * * 중요 * 월드 시간 업데이트에 대한 처리. 즉 1초마다 할일
     ********************************************************** */
    public void procWorldTimeUpdate(){
        procTimeTick();
        procTimeStatus();
        procDelayInput();   // 딜레이 입력 처리(ex. ㄷ 입력후 5초 지나도록 입력이 없으면 이동 처리)
        //todo 또 뭘 처리할까. 이것저것 다 해야될껄?
    }
    private void procDelayInput() {
        if(mInputTxtTimeBefore != 0L){
            Log.v("ASM","======== 입력 지연 로직 들어옴"+mInputTxtTimeBefore);
            long currentTime = mMyApp.getmWorldTime();
            if (currentTime - mInputTxtTimeBefore > MyApp.PLAYER_MOVE_INPUT_WAIT_TIME) {    //이전 기록 시간과 비교
                Log.v("ASM", "======== 입력하고 "+MyApp.PLAYER_MOVE_INPUT_WAIT_TIME / 1000+"초가 넘음");
                //// 이동 입력 확인과 처리
                String inputText = mEt.getText().toString();
                //todo 새로운 처리 방식 메서드로 처리
                switch (inputText) {
                    case "e": case "ㄷ":
//                        procPlayerMoveEast();
                        procPlayerMove(MyApp.DIRECTION_EAST);
                        break;
                    case "w": case "ㅅ":
//                        procPlayerMoveWest();
                        procPlayerMove(MyApp.DIRECTION_WEST);
                        break;
                    case "s": case "ㄴ":
                        //todo 새로운 처리 방식 메서드로 처리
//                        procPlayerMoveSouth();
                        procPlayerMove(MyApp.DIRECTION_SOUTH);
                        break;
                    case "n": case "ㅂ":
//                        procPlayerMoveNorth();
                        procPlayerMove(MyApp.DIRECTION_NORTH);
                        break;
                }
                ////    초기화
                mLastInputText = inputText; // *중요*.0. 입력 편의를 위해 마지막 입력을 기억해두기 << 기능때문에 마지막 입력으로 취급하기위해 변수에 값 줘야함.
                mInputTxtTimeBefore = 0L;   // 1.이전 입력 시간 변수 초기화
                mEt.setText("");            // 2.입력 창 리셋
            } else {
                Log.v("ASM", "======== 입력하고 "+MyApp.PLAYER_MOVE_INPUT_WAIT_TIME / 1000+"초 안넘음");
            }
        }
    }
    /** 틱선녀 처리 */
    private void procTimeTick(){
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