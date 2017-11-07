package com.example.root.seller_point;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.widget.TextView;
//import android.widget.Toolbar;

public class Profile2Activity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout ctl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        initInstancesDrawer();
    }

    private void initInstancesDrawer(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        TextView name = findViewById(R.id.user_profile_name);
        ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        ctl.setTitle("Menon Akhil Parameshwaran");
    }
}
