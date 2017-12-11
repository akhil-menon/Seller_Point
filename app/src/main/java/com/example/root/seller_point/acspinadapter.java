package com.example.root.seller_point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 11/12/17.
 */

public class acspinadapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;

    public acspinadapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.acspinnerlayout,viewGroup,false);
        }

        CheckBox chkbox = view.findViewById(R.id.chkacname);
        TextView txtacname = view.findViewById(R.id.txtacname);

        final int pos = i;
        final HashMap<String,String> map = list.get(pos);
        if(!(map.get("Name").equals("Select Account"))) {
            chkbox.setText(map.get("Name"));
            chkbox.setVisibility(View.VISIBLE);
            txtacname.setVisibility(View.INVISIBLE);
        }
        else {
            txtacname.setText(map.get("Name"));
            chkbox.setVisibility(View.INVISIBLE);
            txtacname.setVisibility(View.VISIBLE);
        }

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        return view;
    }
}
