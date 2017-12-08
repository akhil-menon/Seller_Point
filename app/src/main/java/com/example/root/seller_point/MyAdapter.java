package com.example.root.seller_point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 8/11/17.
 */

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;

    public MyAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.productdisplaylayout,viewGroup,false);
        }

        TextView prodtitle = view.findViewById(R.id.prod_title);
        TextView proddesc = view.findViewById(R.id.prod_desc);
        TextView prodcategory = view.findViewById(R.id.prod_category);

        HashMap<String,String> map = list.get(i);

        prodtitle.setText(map.get("Name"));
        proddesc.setText(map.get("Desc"));
        prodcategory.setText(map.get("Category"));

        return view;
    }
}
