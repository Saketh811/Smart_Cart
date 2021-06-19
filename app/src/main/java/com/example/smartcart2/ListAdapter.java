package com.example.smartcart2;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter {
    private Activity mContext;
    List<ucart> list;
    int sum = 0;
    public  ListAdapter(Activity mContext, List<ucart> uList){
        super(mContext,R.layout.list_item,uList);
        this.mContext=mContext;
        this.list=uList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater=mContext.getLayoutInflater();
        View listItemView =inflater.inflate(R.layout.list_item,null,true);
        TextView tvtitle =listItemView.findViewById(R.id.tvtitle);
        TextView tvprice =listItemView.findViewById(R.id.tvprice);
        TextView tvid =listItemView.findViewById(R.id.tvid);
         ucart cart =list.get(position);
        tvtitle.setText(cart.getTitle());
        tvprice.setText(Integer.toString(cart.getPrice()));
        tvid.setText(cart.getId());
        return listItemView;
    }
}



