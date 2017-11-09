package com.example.root.seller_point;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.opengl.EGLExt;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    CheckBox chkfreeship;
    TextView txtshipcharge;
    Spinner spincategory,spinsubcategory,spinmaxbuyqty;
    SpinnerAdapter objSpinAdapter;
    ArrayList<HashMap<String,String>> spinlist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> subspinlist = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        chkfreeship = findViewById(R.id.chkfreeship);
        txtshipcharge = findViewById(R.id.shippingcharge);
        spincategory = findViewById(R.id.spincategory);
        spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map = spinlist.get(Integer.parseInt(spincategory.getSelectedItemId()+""));
                Toast.makeText(ProductActivity.this,map.get("ID"),Toast.LENGTH_SHORT).show();
                new spinasynccls().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinsubcategory = findViewById(R.id.spinsubcategory);
        String[] str = {"1","2","3","4","5"};
        spinmaxbuyqty = findViewById(R.id.spinmaxbuyqty);
        spinmaxbuyqty.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str));
        new asynccls().execute();
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

    public void btnaddprod_Click(View v)
    {
        HashMap<String,String> map = spinlist.get(Integer.parseInt(spincategory.getSelectedItemId()+""));
        Toast.makeText(ProductActivity.this,map.get("ID"),Toast.LENGTH_SHORT).show();
    }

    public class asynccls extends AsyncTask<String,Void,String>
    {
//        @Override
//        protected void onPreExecute() {
//            pd = new ProgressDialog(MainActivity.this);
//            pd.setTitle("Loading");
//            pd.setMessage("Please Wait...");
//            pd.setCancelable(false);
//            pd.show();
//            super.onPreExecute();
//        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = "http://192.168.0.107/sellerapi/public/index.php/api/tblCategory/CategoryID~0";
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(false);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("RestaurantName",params[0])
//                        .appendQueryParameter("MobileNumber",params[1])
//                        .appendQueryParameter("LandlineNumber",params[2])
//                        .appendQueryParameter("Email",params[3])
//                        .appendQueryParameter("Address",params[4])
//                        .appendQueryParameter("RestaurantType",params[5])
//                        .appendQueryParameter("Description",params[6])
//                        .appendQueryParameter("RestaurantImage",result)
//                        .appendQueryParameter("OpeningTime",params[7])
//                        .appendQueryParameter("ClosingTime",params[8])
//                        .appendQueryParameter("Username",params[9])
//                        .appendQueryParameter("Password",params[10])
//                        .appendQueryParameter("StarsID",params[11])
//                        .appendQueryParameter("GSTNumber",params[12])
//                        .appendQueryParameter("IsGSTIncluded",params[13]);
//
//                String qry = builder.build().getEncodedQuery();

//                OutputStream os = httpURLConnection.getOutputStream();
//                OutputStreamWriter osw = new OutputStreamWriter(os);
//                BufferedWriter bw = new BufferedWriter(osw);
//                bw.write(qry);
//                bw.flush();
//                bw.close();
//                os.close();

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
//            pd.dismiss();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            pd.dismiss();
            Toast.makeText(ProductActivity.this,result,Toast.LENGTH_SHORT).show();
            try {

                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("ID",jsonObject1.getString("ID"));
                    map.put("Name",jsonObject1.getString("Name"));

                    spinlist.add(map);

                }

                objSpinAdapter = new SpinnerAdapter(ProductActivity.this,spinlist);
                spincategory.setAdapter(objSpinAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    public class spinasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> map = spinlist.get(Integer.parseInt(spincategory.getSelectedItemId()+""));
            String response = "";
            String link = "http://192.168.0.107/sellerapi/public/index.php/api/tblCategory/CategoryID~"+map.get("ID");
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(false);
                httpURLConnection.connect();

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

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ProductActivity.this,result,Toast.LENGTH_SHORT).show();
            try {

                JSONArray jsonArray = new JSONArray(result);
                subspinlist.clear();
                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("ID",jsonObject1.getString("ID"));
                    map.put("Name",jsonObject1.getString("Name"));

                    subspinlist.add(map);

                }

                objSpinAdapter = new SpinnerAdapter(ProductActivity.this,subspinlist);
                spinsubcategory.setAdapter(objSpinAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }
}
