package com.example.root.seller_point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 8/12/17.
 */

public class acgridadapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;

    public acgridadapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.gridaccountlayout,viewGroup,false);
        }

        HashMap<String,String> map = list.get(i);
        TextView txtName = view.findViewById(R.id.txt);
        txtName.setText(map.get("Name"));
        ImageView img = view.findViewById(R.id.img);
        if(map.get("Name").equals("Flipkart"))
            img.setImageResource(R.mipmap.flipkart);
        else if(map.get("Name").equals("Amazon"))
            img.setImageResource(R.mipmap.amazon);
        else if(map.get("Name").equals("Ebay"))
            img.setImageResource(R.mipmap.ebay);
        else if(map.get("Name").equals("Snapdeal"))
            img.setImageResource(R.mipmap.snapdeal);
        else if(map.get("Name").equals("Shopclues"))
            img.setImageResource(R.mipmap.shopclues);
        return view;
    }
}
