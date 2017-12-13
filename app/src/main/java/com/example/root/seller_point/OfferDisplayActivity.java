package com.example.root.seller_point;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class OfferDisplayActivity extends AppCompatActivity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_display);

        lv = findViewById(R.id.listoffer);
    }

    public void fabAddOffer_click(View v){
        Intent i = new Intent(this,OfferActivity.class);
        startActivity(i);
    }
}
