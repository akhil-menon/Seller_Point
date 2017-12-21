package com.example.root.seller_point;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView appusername;
    SharedPreferences pref;
    Button btn;
    GridView gv;
    acgridadapter objMyAdapter;
    Dialog myDialog;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    int pos = 0,acid = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("User",MODE_PRIVATE);

        Toast.makeText(this,"Welcome "+pref.getString("UserName",""),Toast.LENGTH_LONG).show();
        gv = findViewById(R.id.grid_view_image_text);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                HashMap<String, String> map = list.get(pos);
                acid = Integer.parseInt(map.get("ID").toString());
//                Toast.makeText(MainActivity.this, map.get("ID"), Toast.LENGTH_SHORT).show();
                new chkacloginTask().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        appusername = (TextView)header.findViewById(R.id.appusername);
        appusername.setText("   "+pref.getString("UserName",""));

        new DisplayTask().execute();
    }

    private void callLoginDialog(int id)
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.acloginlayout);
        myDialog.setCancelable(true);

        Button login = (Button) myDialog.findViewById(R.id.btnlogin);

        final EditText email = (EditText) myDialog.findViewById(R.id.txtemail);
        final EditText password = (EditText) myDialog.findViewById(R.id.txtpwd);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insasynccls().execute(acid+"",email.getText().toString(),password.getText().toString(),pref.getInt("UserID",0)+"");
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i = new Intent(this,ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {

            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.commit();
            finish();
//            Intent i = new Intent(this,LoginActivity.class);
//            startActivity(i);
        } else if (id == R.id.product) {
            Intent i = new Intent(this,DisplayProductActivity.class);
            startActivity(i);
        } else if (id == R.id.offer) {
            Intent i = new Intent(this,OfferDisplayActivity.class);
            startActivity(i);
        }
        else if(id == R.id.sales)
        {
            startActivity(new Intent(this,SalesActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class insasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
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
            link = getResources().getString(R.string.URL)+"insert/useraccounts";

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("AccountType",params[0])
                        .appendQueryParameter("Email",params[1])
                        .appendQueryParameter("Password",params[2])
                        .appendQueryParameter("UserID",params[3]);

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
                startActivity(new Intent(MainActivity.this,DisplayProductActivity.class));
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
            String link = getResources().getString(R.string.URL)+"api/tblAccountType";

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

                    list.add(map);
                }
                objMyAdapter = new acgridadapter(MainActivity.this,list);
                gv.setAdapter(objMyAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }

    public class chkacloginTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"api/tblUserAccounts/UserID~"+pref.getInt("UserID",0);

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
                int flag = 0;
                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if(acid == Integer.parseInt(jsonObject1.getString("AccountType"))){
//                        Toast.makeText(MainActivity.this,acid+"",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,AccountDetail2Activity.class);
                        intent.putExtra("ID",acid);
                        startActivity(intent);
                        flag = 0;
                        break;
                    }
                    else{
                        flag = 1;
                    }

                }

                if(flag == 1 || jsonArray.length() == 0){
                    callLoginDialog(pref.getInt("UserID",0));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
