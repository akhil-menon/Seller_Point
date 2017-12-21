package com.example.root.seller_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountDetail2Activity extends AppCompatActivity {

    TextView txtproducts,txtoffers,txtreturns,txtaccount,txtviewreturns,txtviewreturn1;
    ImageView img;
    int acid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail2);

        txtproducts = findViewById(R.id.products);
        txtoffers = findViewById(R.id.txtoffer);
        txtreturns= findViewById(R.id.txtprodreturn);
        txtaccount = findViewById(R.id.account_name);
        txtviewreturns = findViewById(R.id.txtviewreturn3);
        txtviewreturn1 = findViewById(R.id.txtviewreturn1);
        img = findViewById(R.id.header_cover_image);

        Intent intent = getIntent();
        acid = intent.getIntExtra("ID",0);

        if(acid == 1)
            img.setImageResource(R.drawable.flipkart);
        else if(acid == 2)
            img.setImageResource(R.drawable.amazon);
        else if(acid == 3)
            img.setImageResource(R.drawable.snapdeal);
        else if(acid == 4)
            img.setImageResource(R.drawable.ebay);
        else if(acid == 5)
            img.setImageResource(R.drawable.shopclues);

        new DisplayTask().execute();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountDetail2Activity.this,MainActivity.class));
    }

    public void txtprodclick(View view){
        Intent intent = new Intent(this,DisplayProductActivity.class);
        intent.putExtra("ID",acid);
        startActivity(intent);
    }

    public void txtreturnclick(View view){
        Intent intent = new Intent(this,SalesActivity.class);
        intent.putExtra("ID",acid);
        startActivity(intent);
    }

    public class DisplayTask extends AsyncTask<String,Void,String>
    {

        SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"AccountDetail/"+pref.getInt("UserID",0)+"/"+acid+"";

            try {
                URL url = new URL(link);
                HttpURLConnection hc = (HttpURLConnection) url.openConnection();
                hc.setRequestMethod("GET");
                hc.setDoInput(true);
                hc.setDoOutput(false);
                hc.connect();

                int responsecode = hc.getResponseCode();
                if(responsecode == HttpURLConnection.HTTP_OK)
                {
                    InputStream is = hc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line = "";

                    while((line = br.readLine()) != null)
                    {
                        response = response + line;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                if(jsonArray.length() == 0){
                    txtproducts.append(" : No Products Uploaded Yet");
                }

                for(int i = 0;i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (i == 0) {
                        txtaccount.setText(jsonObject1.getString("Name"));
                        txtproducts.append(" : " + jsonObject1.getString("ProductCount"));
                        txtviewreturn1.setVisibility(View.VISIBLE);
                    } else if(i == 1) {
                        txtproducts.append("\n \nProduct Sales : " + jsonObject1.getString("ProductCount"));
                    }else if(i == 2){
                        txtreturns.setText("Returned Product : " + jsonObject1.getString("ProductCount"));
                        txtviewreturns.setVisibility(View.VISIBLE);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
