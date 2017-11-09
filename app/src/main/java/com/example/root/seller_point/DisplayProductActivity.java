package com.example.root.seller_point;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
//            Toast.makeText(getApplicationContext(),"Process1",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = "http://192.168.0.104/sellerapi/public/index.php/api/tblProduct";

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
//                Toast.makeText(DisplayProductActivity.this,"CatchBg",Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("Name",jsonObject1.getString("Name"));
                    map.put("Category",jsonObject1.getString("Category"));
                    map.put("Desc",jsonObject1.getString("Description"));

                    list.add(map);
                }
//                Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_LONG).show();
                objMyAdapter = new MyAdapter(DisplayProductActivity.this,list);
                lv.setAdapter(objMyAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(getApplicationContext(),"Catch",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);

        }
    }

}
