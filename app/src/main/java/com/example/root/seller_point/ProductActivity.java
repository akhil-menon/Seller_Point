package com.example.root.seller_point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    CheckBox chkfreeship;
    TextView txtshipcharge;
    Spinner spincategory,spinsubcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        chkfreeship = findViewById(R.id.chkfreeship);
        txtshipcharge = findViewById(R.id.shippingcharge);
        String str[] = {"a","b","c"};
        spincategory = findViewById(R.id.spincategory);
        spincategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,str));
        spinsubcategory = findViewById(R.id.spinsubcategory);
    }

    public void Chkfreeship_click(View v){
        if(chkfreeship.isChecked()){
            txtshipcharge.setVisibility(View.VISIBLE);
            txtshipcharge.setHint("Shipping Charge");
        }
        else{
            txtshipcharge.setVisibility(View.INVISIBLE);
            txtshipcharge.setHint("");
        }
    }
}
