package com.peisia.klesa.fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.peisia.klesa.ui.adapter.AdapterRecyclerInfoDisplay;
import com.peisia.klesa.ui.listdata.ListDataInfoDisplay;
import com.peisia.klesa.ui.listitem.ListItemInfoDisplay;

import java.util.ArrayList;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.v("hoyangi","==== 여긴가");
        initKeyboard();

//        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("야옹이")));
//        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("호양이")));
//        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("우리야옹이")));

        mAdapterRecyclerInfoDisplay = new AdapterRecyclerInfoDisplay(mItems);
        mLlm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLlm);
        mRv.setAdapter(mAdapterRecyclerInfoDisplay);
    }

    private void initKeyboard(){
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEt, 0);

/*        mEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.v("ASM","==== ==== 일단 키 부터 찍어보자 :"+keyCode);
                return false;
            }
        });*/

        mEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 주의. 전송(엔터) 누른 경우 이 onEditorAction 이 두번일어난다.
                // 한번은 event 가 null 인 채로 오고
                // 두번째는 event 가 있는 상태로 온다.
                // 따라서 아래처럼 null 이 아닐 때의 처리로 분기해야한다.
                if(event != null){
                    switch (v.getId()){
                        case R.id.fm_home_et:
                            String inputText = v.getText().toString();
                            Log.v("ASM","==== ==== 입력 값:"+inputText);
                            Log.v("ASM","==== ==== 이벤트도 확인하자:"+event.getAction());
                            Log.v("ASM","==== ==== actionId도 확인하자:"+actionId);
                            displayInputText(inputText);
                            clearInputText();
                            reOpenKeyboard();
                            break;
                    }
                }
                return false;
            }
        });
    }
    private void displayInputText(String inputText) {
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay(inputText)));
        mAdapterRecyclerInfoDisplay.notifyDataSetChanged();
    }
    private void clearInputText() {
        mEt.setText("");
    }
    private void reOpenKeyboard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("ASM","==== ==== 여기 오지?");
                mEt.requestFocus();
                imm.showSoftInput(mEt, 0);
            }
        },100); //1000, 500, 200
    }
}
