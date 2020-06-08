package com.peisia.klesa.fragment;
import android.content.Context;
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

import com.peisia.klesa.R;
import com.peisia.klesa.data.Player;
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
    @BindView(R.id.fm_home_rv_info_display) RecyclerView mRv;
    @BindView(R.id.fm_home_et) EditText mEt;
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
        ButterKnife.bind(this, view);
        inits();    // 각종 초기화들
        mAdapterRecyclerInfoDisplay = new AdapterRecyclerInfoDisplay(mItems);
        mLlm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLlm);
        mRv.setAdapter(mAdapterRecyclerInfoDisplay);
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
        //todo 실 좌표 이동
        displayText("동쪽으로 이동했습니다.");   // 표시
//        procMatchingRoom(++mX, mY, mZ);
        procMatchingRoom(1,0,0);
    }
    private void procPlayerMoveWest(){
        //todo 실 좌표 이동
        displayText("서쪽으로 이동했습니다.");   // 표시
//        procMatchingRoom(--mX, mY, mZ);
        procMatchingRoom(-1, 0, 0);
    }
    private void procPlayerMoveNorth(){
        //todo 실 좌표 이동
        displayText("북쪽으로 이동했습니다.");   // 표시
//        procMatchingRoom(mX, ++mY, mZ);
        procMatchingRoom(0, 1, 0);
    }
    private void procPlayerMoveSouth(){
        //todo 실 좌표 이동
        displayText("남쪽으로 이동했습니다.");   // 표시
//        procMatchingRoom(mX, --mY, mZ);
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
    //todo 방좌표 코드와 매칭하여 방찾기
    private void procMatchingRoom(int x, int y, int z) {
        long inputCodeXyz = 1 * 1000  // 당분간 고정 값 1
                + (mX + x) * 100
                + (mY + y) * 10
                + (mZ + z) * 1;
        //todo 탐색
        Log.v("ASM","==== ==== 널이냐 공백이냐? 널이네:" + mMap.get(inputCodeXyz));
        if(mMap.get(inputCodeXyz) == null){
            displayText("이동 할 수 없네용");
        } else {    // todo 좌표 반영
            mX = mX + x;
            mY = mY + y;
            mZ = mZ + z;
            displayRoom(UtilKlesaMap.xyzToXyzCode(mX, mY, mZ));  // 이동한 새 방 설명 표시 처리
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
        mPlayer = new Player(10, 8, 3, 100, 50, 30);
        mPlayer.setCodeXyz(1111);
    }
}
