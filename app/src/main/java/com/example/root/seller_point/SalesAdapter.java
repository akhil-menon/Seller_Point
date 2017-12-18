package com.example.root.seller_point;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 17/12/17.
 */

public class SalesAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog pd;

    public SalesAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.saleslayout,viewGroup,false);
        }

        final int pos = i;

        TextView txtprodname = view.findViewById(R.id.ProdName);
        TextView txtcosname = view.findViewById(R.id.CosName);
        TextView txtcosemail = view.findViewById(R.id.CosEmail);
        TextView txtcosphone = view.findViewById(R.id.CosPhone);
        TextView txtaccname = view.findViewById(R.id.AccName);
        TextView txtstatus = view.findViewById(R.id.Status);

        HashMap<String,String> map = list.get(i);
        txtcosname.setText(map.get("Name"));
        txtcosemail.setText(map.get("Email"));
        txtcosphone.setText(map.get("Phone"));
        txtprodname.setText(map.get("ProductName"));
        txtaccname.setText(map.get("AccountName"));

        if(Integer.parseInt(map.get("isReturn").toString()) == 0){
            txtstatus.setTextColor(Color.parseColor("#00ff00"));
            txtstatus.setText("Delivered");
        }
        else{
            txtstatus.setTextColor(Color.parseColor("#ff0000"));
            txtstatus.setText("Returned");
        }

        return view;
    }

}
