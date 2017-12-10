package com.example.root.seller_point;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
        TextView prodprice= view.findViewById(R.id.prod_price);
        TextView proddiscount= view.findViewById(R.id.prod_Discount);
        final ImageButton imgbtn = view.findViewById(R.id.imgbtn);
        final int pos = i;

        HashMap<String,String> map = list.get(i);

        prodtitle.setText(map.get("Name"));
        proddesc.setText(map.get("Desc"));
        prodcategory.setText(map.get("Category"));
        prodprice.setText(map.get("Price"));
        proddiscount.setText(map.get("Discount")+"% discount");

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, imgbtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Edit) {
                            Intent intent = new Intent(context,ProductActivity.class);
                            intent.putExtra("id",pos);
                            context.startActivity(intent);
                        }
                        else if (item.getItemId() == R.id.Delete){
                            Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Warning")
                                    .setMessage("Are you sure you want to delete this product?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else if (item.getItemId() == R.id.Offer) {
                            Toast.makeText(context,"Offer",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        return view;
    }
}
