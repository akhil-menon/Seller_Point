package com.example.root.seller_point;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
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

public class OfferActivity extends AppCompatActivity {

    EditText txtoffername,txtofferdesc,txtoffer;
    TextView txtaccount;
    Button btnaddoffer;
    ProgressDialog pd;
    ArrayList<HashMap<String,String>> acspin = new ArrayList<HashMap<String, String>>();
    String[] acspinlist;
    boolean[] acspinlistchk;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        txtoffername = findViewById(R.id.txtoffername);
        txtofferdesc = findViewById(R.id.txtofferdesc);
        txtoffer = findViewById(R.id.txtoffer);

        txtaccount= findViewById(R.id.txtofferaccount);
        new acspinasynccls().execute();
        txtaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OfferActivity.this);
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
                        txtaccount.setText("");
                        for (int i = 0; i<acspinlistchk.length; i++){
                            boolean checked = acspinlistchk[i];
                            if (checked) {
                                txtaccount.setText(txtaccount.getText() + acspinlist[i] + "\n");
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

        Intent intent = getIntent();
        pos = intent.getIntExtra("id",0);

        btnaddoffer = findViewById(R.id.btnaddoffer);
        btnaddoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s = txtaccount.getText().toString().split("\n");
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
                    if(pos == 0){
                        new insasynccls().execute(txtoffername.getText().toString(),txtofferdesc.getText().toString(),txtoffer.getText().toString(),txtaccount.getText().toString());
                    }
                    else{
                        new insasynccls().execute(txtoffername.getText().toString(),txtofferdesc.getText().toString(),txtoffer.getText().toString(),txtaccount.getText().toString());
                    }
                }
            }
        });
    }

    public class insasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(OfferActivity.this);
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
            link = getResources().getString(R.string.URL)+"insert/offer";

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
                        .appendQueryParameter("Desc",params[1])
                        .appendQueryParameter("Discount",params[2])
                        .appendQueryParameter("AccountID",params[3]);

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
                startActivity(new Intent(OfferActivity.this,DisplayProductActivity.class));
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
}
