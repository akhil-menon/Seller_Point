package com.example.root.seller_point;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class DisplayProductActivity extends AppCompatActivity {

    FloatingActionButton fabAddProd;
    ListView lv;
    MyAdapter objMyAdapter;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

        fabAddProd = findViewById(R.id.fabAddProduct);
        lv = findViewById(R.id.listprod);
        new DisplayTask().execute();
    }

    public void fabAddProd_click(View v){
        Intent i = new Intent(this,ProductActivity.class);
        startActivity(i);
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
            String link = getResources().getString(R.string.URL)+"api/tblProduct%20p,tblCategory%20c/p.*,c.Name%20as%20CategoryName/p.Category~c.ID";

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
                    map.put("Name",jsonObject1.getString("Name"));
                    map.put("Category",jsonObject1.getString("CategoryName"));
                    map.put("Desc",jsonObject1.getString("Description"));

                    list.add(map);
                }
                objMyAdapter = new MyAdapter(DisplayProductActivity.this,list);
                lv.setAdapter(objMyAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }

}
