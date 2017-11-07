package com.example.root.seller_point;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DisplayProductActivity extends AppCompatActivity {

    FloatingActionButton fabAddProd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

        fabAddProd = findViewById(R.id.fabAddProduct);
    }

    public void fabAddProd_click(View v){
        Intent i = new Intent(this,ProductActivity.class);
        startActivity(i);
    }
}
