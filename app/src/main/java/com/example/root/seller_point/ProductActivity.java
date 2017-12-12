package com.example.root.seller_point;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    CheckBox chkfreeship;
    TextView txtshipcharge,txtprodname,txtstock,txtdesc,txtdiscount,txtcashoff,txtprice,spinaccount;
    Spinner spincategory,spinsubcategory,spinmaxbuyqty;
    SpinnerAdapter objSpinAdapter;
    acspinadapter objacspinadapter;
    Button btnaddprod;
    ProgressDialog pd;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> spinlist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> subspinlist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> acspin = new ArrayList<HashMap<String, String>>();
    String[] acspinlist;
    boolean[] acspinlistchk;
    int pos = 0;

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        chkfreeship = findViewById(R.id.chkfreeship);
        btnaddprod = findViewById(R.id.btnaddprod);
        txtshipcharge = findViewById(R.id.shippingcharge);
        txtprodname = findViewById(R.id.txtprodname);
        txtstock = findViewById(R.id.stock);
        txtdesc = findViewById(R.id.desc);
        txtdiscount = findViewById(R.id.flatdiscount);
        txtcashoff = findViewById(R.id.cashdiscount);
        txtprice = findViewById(R.id.price);

        spincategory = findViewById(R.id.spincategory);
        spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map = spinlist.get(Integer.parseInt(spincategory.getSelectedItemId()+""));
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

        spinaccount = findViewById(R.id.spinaccount);
//        spinaccount.setLayoutMode(Spinner.MODE_DIALOG);
        new acspinasynccls().execute();

        spinaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setMultiChoiceItems(acspinlist, acspinlistchk, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        acspinlistchk[i] = b;
                    }
                });
                // Specify the dialog is not cancelable
                builder.setCancelable(false);

                // Set a title for alert dialog
                builder.setTitle("Accounts on Which you want to insert");

                // Set the positive/yes button click listener
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click positive button
//                spinaccount.setText("Your preferred colors..... \n");
                        spinaccount.setText("");
                        for (int i = 0; i<acspinlistchk.length; i++){
                            boolean checked = acspinlistchk[i];
                            if (checked) {
                                spinaccount.setText(spinaccount.getText() + acspinlist[i] + "\n");
                            }
                        }
                    }
                });

                // Set the neutral/cancel button click listener
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click the neutral button
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface

                dialog.show();
            }
        });

        new asynccls().execute();

        btnaddprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnaddprod_Click(view);
            }
        });

        Intent intent = getIntent();
        pos = intent.getIntExtra("id",-1);
        if( pos != -1){
            new DisplayTask().execute();
        }
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
        HashMap<String,String> catg = spinlist.get(Integer.parseInt(spincategory.getSelectedItemId()+""));
        HashMap<String,String> subcatg = new HashMap<String, String>();
        if(Integer.parseInt(spincategory.getSelectedItemId()+"") !=1)
            subcatg = spinlist.get(Integer.parseInt(spinsubcategory.getSelectedItemId()+""));
        else
        {
            subcatg.put("ID","0");
        }

        String[] s = spinaccount.getText().toString().split("\n");
        int [] id= new int[s.length];

        for(int i=0;i<s.length;i++) {
            for (HashMap<String, String> tmp : acspin) {
                if (tmp.get("Name").equals(s[i]))
                {
                    id[i]=Integer.parseInt(tmp.get("ID"));
                }
            }
        }

        for(int i=0;i<id.length;i++) {
            new insasynccls().execute(txtprodname.getText().toString(), catg.get("ID"), subcatg.get("ID"),
                    txtstock.getText().toString(), spinmaxbuyqty.getSelectedItem().toString(), txtdesc.getText().toString(),
                    txtdiscount.getText().toString(), txtcashoff.getText().toString(), txtprice.getText().toString(), txtshipcharge.getText().toString(),id[i]+"");
        }
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
            String link = getResources().getString(R.string.URL)+"/api/tblCategory/CategoryID~0";
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
            String link = getResources().getString(R.string.URL)+"api/tblCategory/CategoryID~"+map.get("ID");
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

    public class acspinasynccls extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"/api/tblAccountType";
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
            try {

                JSONArray jsonArray = new JSONArray(result);

                acspinlist=new String[jsonArray.length()];
                acspinlistchk=new boolean[jsonArray.length()];

                //HashMap<String,String> acmap = new HashMap<String, String>();
                //acmap.put("Name","Select Account");
                //acspinlist.add(acmap);
                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("ID",jsonObject1.getString("ID"));
                    map.put("Name",jsonObject1.getString("Name"));

                    acspinlist[i] = jsonObject1.getString("Name");
                    acspinlistchk[i]=false;
                    acspin.add(map);
                }



//                objacspinadapter = new acspinadapter(ProductActivity.this,acspinlist);
//                spinaccount.setAdapter(objacspinadapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    public class insasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ProductActivity.this);
            pd.setTitle("Loading");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = "";
//            if( pos == -1)
                 link = getResources().getString(R.string.URL)+"insert/product";
//            else
//                link = getResources().getString(R.string.URL)+"update/product";

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("Name",params[0])
                        .appendQueryParameter("Category",params[1])
                        .appendQueryParameter("Sub_Category",params[2])
                        .appendQueryParameter("Stock",params[3])
                        .appendQueryParameter("MaxAllowedBuyQty",params[4])
                        .appendQueryParameter("Description",params[5])
                        .appendQueryParameter("Flat_Discount",params[6])
                        .appendQueryParameter("Cash_Discount",params[7])
                        .appendQueryParameter("Price",params[8])
                        .appendQueryParameter("Shipping_Charge",params[9])
                        .appendQueryParameter("AccountID",params[10]);

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
//            pd.dismiss();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            try {
                startActivity(new Intent(ProductActivity.this,DisplayProductActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
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
                    map.put("Name",jsonObject1.getString("Name"));
                    map.put("Category",jsonObject1.getString("Category"));
                    map.put("CategoryName",jsonObject1.getString("CategoryName"));
                    map.put("Sub_Category",jsonObject1.getString("Sub_Category"));
                    map.put("Stock",jsonObject1.getString("Stock"));
                    map.put("MaxAllowedBuyQty",jsonObject1.getString("MaxAllowedBuyQty"));
                    map.put("Desc",jsonObject1.getString("Description"));
                    map.put("Price",jsonObject1.getString("Price"));
                    map.put("Flat_Discount",jsonObject1.getString("Flat_Discount"));
                    map.put("Cash_Discount",jsonObject1.getString("Cash_Discount"));
                    map.put("Warranty_Period",jsonObject1.getString("Warranty_Period"));
                    map.put("FreeShippingAvail",jsonObject1.getString("FreeShippingAvail"));
                    map.put("Shipping_Charge",jsonObject1.getString("Shipping_Charge"));
                    map.put("AccountID",jsonObject1.getString("AccountID"));

                    list.add(map);
                }
                HashMap<String,String> map = list.get(pos);
                Toast.makeText(ProductActivity.this,map+"",Toast.LENGTH_LONG).show();

                chkfreeship = findViewById(R.id.chkfreeship);
                txtshipcharge = findViewById(R.id.shippingcharge);
                txtprodname = findViewById(R.id.txtprodname);
                txtstock = findViewById(R.id.stock);
                txtdesc = findViewById(R.id.desc);
                txtdiscount = findViewById(R.id.flatdiscount);
                txtcashoff = findViewById(R.id.cashdiscount);
                txtprice = findViewById(R.id.price);
                spincategory = findViewById(R.id.spincategory);
                spinsubcategory = findViewById(R.id.spinsubcategory);
                spinmaxbuyqty = findViewById(R.id.spinmaxbuyqty);

                txtprodname.setText(map.get("Name"));
                txtstock.setText(map.get("Stock"));
                txtdesc.setText(map.get("Desc"));
                txtdiscount.setText(map.get("Flat_Discount"));
                txtcashoff.setText(map.get("Cash_Discount"));
                txtprice.setText(map.get("Price"));
                if(Integer.parseInt(map.get("Shipping_Charge")) != 0 ){
                    chkfreeship.setChecked(true);
                    txtshipcharge.setVisibility(View.VISIBLE);
                    txtshipcharge.setHint("Shipping Charge");
                    txtshipcharge.setText(map.get("Shipping_Charge"));
                }
                else{
                    chkfreeship.setChecked(false);
                    txtshipcharge.setVisibility(View.INVISIBLE);
                    txtshipcharge.setHint("");
                }
                spinmaxbuyqty.setSelection(Integer.parseInt(map.get("MaxAllowedBuyQty")) - 1);

                spincategory.setSelection(getIndex(spincategory, map.get("CategoryName")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            HashMap<String,String> map = (HashMap<String, String>) spinner.getItemAtPosition(i);
            if (map.get("Name").equalsIgnoreCase(myString)){
                index = i++;
                break;
            }
        }
        return index;
    }
}
