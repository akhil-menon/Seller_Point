package com.example.root.seller_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SalesActivity extends AppCompatActivity {


    ListView lv;
    SalesAdapter objsalesadapter;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    int acid = 0;
    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);

        Intent intent = getIntent();
        acid = intent.getIntExtra("ID",0);

        if(acid == 0){
            link=getResources().getString(R.string.URL)+"api/tblProduct%20p,tblSales%20s,tblAccountType%20at/s.*,p.Name%20ProductName,at.Name%20AccountName/p.ID~s.ProdID,p.AccountID~at.ID";
        }
        else{
            link=getResources().getString(R.string.URL)+"api/tblProduct%20p,tblSales%20s,tblAccountType%20at/s.*,p.Name%20ProductName,at.Name%20AccountName/p.ID~s.ProdID,p.AccountID~at.ID,s.isReturn~1,at.ID~"+acid;
        }

        lv = findViewById(R.id.listsales);
        new DisplayTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class DisplayTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
//            String link = getResources().getString(R.string.URL)+"api/tblProduct%20p,tblSales%20s,tblAccountType%20at/s.*,p.Name%20ProductName,at.Name%20AccountName/p.ID~s.ProdID,p.AccountID~at.ID";

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

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("ID",jsonObject1.getString("ID"));
                    map.put("Name",jsonObject1.getString("Name"));
                    map.put("Email",jsonObject1.getString("Email"));
                    map.put("Phone",jsonObject1.getString("Phone"));
                    map.put("isReturn",jsonObject1.getString("isReturn"));
                    map.put("ProductName",jsonObject1.getString("ProductName"));
                    map.put("AccountName",jsonObject1.getString("AccountName"));

                    list.add(map);
                }
                objsalesadapter = new SalesAdapter(SalesActivity.this,list);
                lv.setAdapter(objsalesadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
