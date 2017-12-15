package com.example.root.seller_point;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by root on 14/12/17.
 */

public class OfferAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog pd;

    public OfferAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.offerlayout,viewGroup,false);
        }

        final int pos = i;

        TextView txtoffername = view.findViewById(R.id.offer_title);
        TextView txtofferdesc = view.findViewById(R.id.offer_desc);
        TextView txtofferdisc = view.findViewById(R.id.offer_discount);
        TextView txtprodname = view.findViewById(R.id.product_name);
        TextView txtofferavail = view.findViewById(R.id.offer_available);
        TextView txtofferactive = view.findViewById(R.id.offer_active);
        final ImageButton imgbtn = view.findViewById(R.id.imgbtn);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, imgbtn);
                popup.getMenuInflater().inflate(R.menu.offerpopup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final HashMap<String,String> map = list.get(pos);
                        if (item.getItemId() == R.id.Edit) {
                            Intent intent = new Intent(context,OfferActivity.class);
                            intent.putExtra("offerid",Integer.parseInt(map.get("ID")+""));
                            context.startActivity(intent);
                        }
                        else if (item.getItemId() == R.id.Delete){
//                            Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Warning")
                                    .setMessage("Are you sure you want to delete this offer?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(context,map.get("ID")+"",Toast.LENGTH_LONG).show();
                                            new OfferAdapter.delasynccls().execute(map.get("ID").toString());
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        HashMap<String,String> map = list.get(i);
        txtoffername.setText(map.get("Name"));
        txtofferdesc.setText(map.get("Description"));
        txtofferdisc.setText(map.get("Discount"));
        txtprodname.setText(map.get("ProductName"));

        if(Integer.parseInt(map.get("isActive").toString()) == 1){
            txtofferactive.setText("Active");
            txtofferavail.setText("Available");
        }
        else{
            txtofferactive.setText("Not Active");
            txtofferavail.setText("Not Available");
        }

        txtofferactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String,String> map = list.get(pos);
                if(Integer.parseInt(map.get("isActive")) == 1)
                    new updactasynccls().execute(map.get("ID").toString(),"0");
                else
                    new updactasynccls().execute(map.get("ID").toString(),"1");
            }
        });

        return view;
    }

    public class updactasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Updating");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = "";
            link = context.getResources().getString(R.string.URL)+"update/offer/isactive";

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ID",params[0])
                        .appendQueryParameter("isActive",params[1]);

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
                context.startActivity(new Intent(context,OfferDisplayActivity.class));
//                OfferAdapter objofferadp = new OfferAdapter(context,list);
//                objofferadp.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class delasynccls extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
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
            link = context.getResources().getString(R.string.URL)+"delete/offer";

            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

//                result = getStringImage(bitmap);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ID",params[0]);

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
                context.startActivity(new Intent(context,OfferDisplayActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
