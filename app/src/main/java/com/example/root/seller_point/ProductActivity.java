package com.example.root.seller_point;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        new RestaurantRegistraion().execute();
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

    public void doWork(String args)
    {
        Toast.makeText(ProductActivity.this,"This is post execute",Toast.LENGTH_SHORT).show();
    }

    public  class RestaurantRegistraion extends AsyncTask<String,Void,String>
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
            String link = "http://192.168.0.104/sellerapi/public/index.php/api/tblCategory";
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
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                int code = jsonObject.getInt("code");
//                String message = jsonObject.getString("message");
//                if(code == 100)
//                {
//                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//                }
//                else if(code == 101)
//                {
//                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//                }
//                else if(code == 102)
//                {
//                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            super.onPostExecute(result);
        }
    }
}
