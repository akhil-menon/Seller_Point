package com.example.root.seller_point;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by root on 8/12/17.
 */

public class gridfrontadapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public gridfrontadapter(Context mContext, String[] gridViewString, int[] gridViewImageId) {
        this.mContext = mContext;
        this.gridViewString = gridViewString;
        this.gridViewImageId = gridViewImageId;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.gridlayoutfront,viewGroup,false);
        }

        TextView txtName = view.findViewById(R.id.android_gridview_text);
        ImageView img = view.findViewById(R.id.android_gridview_image);

        txtName.setText(gridViewString[i]);
        img.setImageResource(gridViewImageId[i]);

        return view;
    }
}
