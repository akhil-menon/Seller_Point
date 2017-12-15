package com.example.root.seller_point;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayProductActivity extends AppCompatActivity{

    FloatingActionButton fabAddProd;
    ListView lv;
    MyAdapter objMyAdapter;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    String [] ImageStringUrl;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

//        Toast.makeText(this,data.toString(),Toast.LENGTH_LONG).show();

        if(data.getClipData()!=null)
        {
            int cnt = data.getClipData().getItemCount();
            ImageStringUrl = new String[cnt];

            for(int i=0;i<cnt;i++)
            {
                Uri image = data.getClipData().getItemAt(i).getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),image);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);
                    byte[] imagebyte = out.toByteArray();
                    ImageStringUrl[i] = Base64.encodeToString(imagebyte,Base64.DEFAULT);
                    bitmap = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences pref = getSharedPreferences("Prod",MODE_PRIVATE);
            String id = pref.getInt("ProdID",0)+"";
            for (int i=0;i<ImageStringUrl.length;i++)
            {
                new picinsasynccls().execute(id,ImageStringUrl[i]);
            }
        }

    }

    public class picinsasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = "";
            link = getResources().getString(R.string.URL)+"insert/prodimg";

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ProdID",params[0])
                        .appendQueryParameter("Image",params[1]);

                String qry = builder.build().getEncodedQuery();

                OutputStream os = httpURLConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(qry);
                bw.flush();
                bw.close();
                os.close();

                int responsecode = httpURLConnection.getResponseCode();
                if(responsecode ==  HttpURLConnection.HTTP_OK)
                {
                    InputStream is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line = "";

                    while((line = br.readLine())!=null)
                    {
                        response = response + line;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
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
                    map.put("ID",jsonObject1.getString("ID"));
                    map.put("Name",jsonObject1.getString("Name"));
                    map.put("Category",jsonObject1.getString("CategoryName"));
                    map.put("Desc",jsonObject1.getString("Description"));
                    map.put("Price",jsonObject1.getString("Price"));
                    map.put("Discount",jsonObject1.getString("Flat_Discount"));

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