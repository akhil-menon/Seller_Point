package com.example.root.seller_point;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView txtname,txtemail,txtmobile,txtaddress,txtaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtname = findViewById(R.id.user_profile_name);
        txtemail = findViewById(R.id.user_profile_email);
        txtmobile = findViewById(R.id.user_profile_mobile);
        txtaddress = findViewById(R.id.address);
        txtaccount = findViewById(R.id.account);

        new userasynccls().execute();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }

    public class userasynccls extends AsyncTask<String,Void,String>
    {

        SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"api/tblUser%20u,tblUserAccounts%20ua,tblAccountType%20a/u.*,a.Name%20as%20AccountName/ua.AccountType~a.ID,ua.UserID~u.UserID,u.UserID~"+pref.getInt("UserID",0);

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

                    if(i == 0){
                        txtname.setText(jsonObject1.getString("Name"));
                        txtemail.setText(jsonObject1.getString("Email"));
                        txtmobile.setText(jsonObject1.getString("Mobile"));
                        txtaddress.setText(jsonObject1.getString("Address"));
                        txtaccount.setText(jsonObject1.getString("AccountName")+"\n");
                    }else{
                        txtaccount.append(jsonObject1.getString("AccountName")+"\n");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }

}
