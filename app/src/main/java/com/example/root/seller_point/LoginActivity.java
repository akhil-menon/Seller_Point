package com.example.root.seller_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView txtreg,txtuname,txtpwd;
    Button btn;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);

        if(pref.getInt("UserID",0) != 0 )
        {
            startActivity(new Intent(this,MainActivity.class));
        }

        map = new HashMap<>();
        txtuname = findViewById(R.id.txtuname);
        txtpwd = findViewById(R.id.txtpwd);

        txtreg = findViewById(R.id.txtreg);
        txtreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        btn = findViewById(R.id.btnlogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtuname.getText().toString().equals("") && !txtpwd.getText().toString().equals("")){
                    new LoginTask().execute(txtuname.getText().toString(),txtpwd.getText().toString());
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Enter Credential to login",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public class LoginTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"api/tblUser/UserName~"+params[0]+",Password~"+params[1];

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

                if(jsonArray.length() > 0 )
                {
                    for(int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        map.put("ID",jsonObject1.getString("UserID"));
                        map.put("Username",jsonObject1.getString("UserName"));
                    }

                    SharedPreferences loginpref = getSharedPreferences("User",MODE_PRIVATE);
                    SharedPreferences.Editor edit;

                    if(!map.get("ID").equals(""))
                    {
                        edit = loginpref.edit();
                        edit.putInt("UserID",Integer.parseInt(map.get("ID")));
                        edit.putString("UserName",map.get("Username"));
                        edit.commit();
                    }

                    if(loginpref.getInt("UserID",0) != 0)
                    {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Something Went Wrong Try Again",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Either Username or Password Incorrect",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
