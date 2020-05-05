package com.peisia.klesa.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peisia.klesa.R;
import com.peisia.klesa.ui.listitem.ListItemInfoDisplay;

import java.util.ArrayList;
/**
 * Created by 호양이 on 2020-05-05.
 */
public class AdapterRecyclerInfoDisplay extends RecyclerView.Adapter <AdapterRecyclerInfoDisplay.ItemViewHolder>{
    ArrayList<ListItemInfoDisplay> mItems;
    public AdapterRecyclerInfoDisplay(ArrayList<ListItemInfoDisplay> mItems) {
        this.mItems = mItems;
    }
    @NonNull
    @Override
    public AdapterRecyclerInfoDisplay.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_info_display, parent, false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerInfoDisplay.ItemViewHolder holder, int position) {
        holder.mTvMessage.setText(mItems.get(position).getListDataInfoDisplay().getMessage());
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvMessage;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.item_recycler_info_display_tv);
        }
    }
    public void setItems(ArrayList<ListItemInfoDisplay> mItems) {
        this.mItems = mItems;
    }
}
