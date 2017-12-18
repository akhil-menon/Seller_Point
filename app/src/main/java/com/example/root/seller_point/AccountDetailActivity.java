package com.example.root.seller_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.HashMap;

public class AccountDetailActivity extends AppCompatActivity {

    TextView txtproducts,txtoffers,txtreturns,txtaccount;
    ImageView img;
    int acid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        txtproducts = findViewById(R.id.txtproduct);
        txtoffers = findViewById(R.id.txtoffer);
        txtreturns= findViewById(R.id.txtreturn);
        txtaccount = findViewById(R.id.account_name);

        Intent intent = getIntent();
        acid = intent.getIntExtra("ID",0);

        new DisplayTask().execute();
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
            String link = getResources().getString(R.string.URL)+"api/tblUserAccounts ua,tblAccounts a/a.Name/ua.AccountType~a.Name,ua.UserID~"+pref.getInt("UserID",0)+",a.AccountType~"+acid+"";

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

                    txtaccount.setText(jsonObject1.getString("a.Name"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
