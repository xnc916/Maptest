package com.yuluedu.maptest.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuluedu.maptest.custom.TreasureView;
import com.yuluedu.maptest.treasure.Treasure;
import com.yuluedu.maptest.treasure.detail.TreasureDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gameben on 2017-05-17.
 */

public class TreasureListAdapter extends RecyclerView.Adapter<TreasureListAdapter.MyrecyclerHodler>{

    private List<Treasure> data = new ArrayList<>();

    //对外添加数据方法

    public void additemDate(List<Treasure> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }


    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    //创建ViewHodler
    @Override
    public MyrecyclerHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        TreasureView treasureView = new TreasureView(parent.getContext());
        return new MyrecyclerHodler(treasureView);
    }

    //数据与视图绑定
    @Override
    public void onBindViewHolder(MyrecyclerHodler holder, int position) {
        final Treasure treasure = data.get(position);
        holder.mTreasureView.bindTreasure(treasure);
        //点击事件(也可用接口回掉)
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (mOnItemClickListener != null) {
                    //点击卡片的时候直接跳转到宝藏详情页
                    TreasureDetailActivity.open(v.getContext(), treasure);


                //}
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyrecyclerHodler extends RecyclerView.ViewHolder{
        private TreasureView mTreasureView;

        public MyrecyclerHodler(TreasureView itemView) {
            super(itemView);
            this.mTreasureView = itemView;
        }
    }
    public interface OnItemClickListener {

        void onItemClick();

        void onItemLongClick();
    }
}
