package com.example.root.seller_point;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {

    Button btn;
    ProgressDialog pd;
    EditText txtname,txtuname,txtaddr,txtemail,txtpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtname = findViewById(R.id.txtname);
        txtuname = findViewById(R.id.txtuname);
        txtaddr = findViewById(R.id.txtaddr);
        txtemail = findViewById(R.id.txtemail);
        txtpwd = findViewById(R.id.txtpwd);

        btn = findViewById(R.id.btnreg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insasynccls().execute(txtname.getText().toString(),txtuname.getText().toString(),
                        txtaddr.getText().toString(),txtemail.getText().toString(),txtpwd.getText().toString());
            }
        });
    }

    public class insasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(RegistrationActivity.this);
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
            link = getResources().getString(R.string.URL)+"insert/user";

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
                        .appendQueryParameter("Mobile",params[1])
                        .appendQueryParameter("Address",params[2])
                        .appendQueryParameter("Email",params[3])
                        .appendQueryParameter("Password",params[4])
                        .appendQueryParameter("IsActive","1");

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
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

}
