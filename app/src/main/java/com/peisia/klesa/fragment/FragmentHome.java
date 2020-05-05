package com.peisia.klesa.fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    @BindView(R.id.fm_home_rv_info_display)
    RecyclerView mRv;
    private LinearLayoutManager mLlm;
    AdapterRecyclerInfoDisplay mAdapterRecyclerInfoDisplay;
    ArrayList<ListItemInfoDisplay> mItems = new ArrayList<>();

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

        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("헬로 야옹이 월드에 오신것을 환영합니다.")));
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("당신의 이름은 호양이 입니다.")));
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("당신의 직업은 백수 입니다.")));
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("당신의 레벨은 1 입니다.")));
        mItems.add(new ListItemInfoDisplay(new ListDataInfoDisplay("당신의 성별은 남성 입니다.")));

        mAdapterRecyclerInfoDisplay = new AdapterRecyclerInfoDisplay(mItems);
        mLlm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLlm);
        mRv.setAdapter(mAdapterRecyclerInfoDisplay);
    }
}
