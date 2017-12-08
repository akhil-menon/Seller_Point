package com.example.root.seller_point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 9/11/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String,String>> list;

    public SpinnerAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.spinnerlayout,viewGroup,false);
        }

        TextView id = view.findViewById(R.id.cat_id);
        TextView name = view.findViewById(R.id.cat_name);

        HashMap<String,String> map = list.get(i);
        id.setText(map.get("ID"));
        name.setText(map.get("Name"));

        return view;
    }
}
