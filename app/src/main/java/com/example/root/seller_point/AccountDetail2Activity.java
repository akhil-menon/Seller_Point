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

    TextView txtproducts,txtoffers,txtreturns,txtaccount;
    ImageView img;
    int acid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail2);

        txtproducts = findViewById(R.id.products);
        txtoffers = findViewById(R.id.txtoffer);
        txtreturns= findViewById(R.id.txtreturn);
        txtaccount = findViewById(R.id.account_name);
        img = findViewById(R.id.user_profile_photo);

        Intent intent = getIntent();
        acid = intent.getIntExtra("ID",0);

        new DisplayTask().execute();
    }

    public void txtprodclick(View view){
        Intent intent = new Intent(this,DisplayProductActivity.class);
        intent.putExtra("ID",acid);
        startActivity(intent);
    }

    public void txtofferclick(View view){
        Intent intent = new Intent(this,DisplayProductActivity.class);
        intent.putExtra("ID",acid);
        startActivity(intent);
    }

    public void txtreturnclick(View view){
        Intent intent = new Intent(this,DisplayProductActivity.class);
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

                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    txtaccount.setText(jsonObject1.getString("Name"));
                    txtproducts.append(" : "+jsonObject1.getString("ProductCount"));
                }

//                if(txtaccount.getText().equals("Flipkart"))
//                    img.setImageResource(R.mipmap.flipkart);
//                else if(txtaccount.getText().equals("Amazon"))
//                    img.setImageResource(R.mipmap.amazon);
//                else if(txtaccount.getText().equals("Ebay"))
//                    img.setImageResource(R.mipmap.ebay);
//                else if(txtaccount.getText().equals("Snapdeal"))
//                    img.setImageResource(R.mipmap.snapdeal);
//                else if(txtaccount.getText().equals("Shopclues"))
//                    img.setImageResource(R.mipmap.shopclues);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
