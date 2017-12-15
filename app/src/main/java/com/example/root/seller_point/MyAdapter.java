package com.example.root.seller_point;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
 * Created by root on 8/11/17.
 */

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog pd;

    public MyAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.productdisplaylayout,viewGroup,false);
        }

        TextView prodtitle = view.findViewById(R.id.prod_title);
        TextView proddesc = view.findViewById(R.id.prod_desc);
        TextView prodcategory = view.findViewById(R.id.prod_category);
        TextView prodprice= view.findViewById(R.id.prod_price);
        TextView proddiscount= view.findViewById(R.id.prod_Discount);
        TextView prodstock= view.findViewById(R.id.prod_stock);
        TextView prodactive= view.findViewById(R.id.prod_active);

        final ImageButton imgbtn = view.findViewById(R.id.imgbtn);
        final int pos = i;

        HashMap<String,String> map = list.get(i);

        prodtitle.setText(map.get("Name"));
        proddesc.setText(map.get("Desc"));
        prodcategory.setText(map.get("Category"));
        prodprice.setText(map.get("Price"));
        proddiscount.setText(map.get("Discount")+"% discount");

        if(Integer.parseInt(map.get("Stock").toString()) != 0) {
            prodstock.setText("In Stock("+map.get("Stock")+")");
//            prodstock.setTextColor(R.color.colorGreen);
        }
        else {
            prodstock.setText("Out Of Stock");
//            prodstock.setTextColor(R.color.colorRed);
        }

        if(Integer.parseInt(map.get("IsActive").toString()) == 1) {
            prodactive.setText("Active");
        }
        else{
            prodactive.setText("Not Active");
        }

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, imgbtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final HashMap<String,String> map = list.get(pos);
                        if (item.getItemId() == R.id.Edit) {
                            Intent intent = new Intent(context,ProductActivity.class);
                            intent.putExtra("id",Integer.parseInt(map.get("ID")+""));
                            context.startActivity(intent);
                        }
                        else if (item.getItemId() == R.id.Delete){
                            Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Warning")
                                    .setMessage("Are you sure you want to delete this product?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,map.get("ID")+"",Toast.LENGTH_LONG).show();
                                            new delasynccls().execute(map.get("ID").toString());
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
                        else if (item.getItemId() == R.id.Offer) {
                            Intent intent = new Intent(context,OfferActivity.class);
                            intent.putExtra("id",Integer.parseInt(map.get("ID")+""));
                            context.startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        CardView cvdisp = view.findViewById(R.id.cardview);
        cvdisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DisplayDetailActivity.class);
                HashMap<String,String> map = list.get(pos);
                intent.putExtra("ID",map.get("ID"));
                context.startActivity(intent);
            }
        });

        prodstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String,String> map = list.get(pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Update Stock");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PHONETIC);
                builder.setView(input);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,input.getText().toString(),Toast.LENGTH_SHORT).show();
                        new updasynccls().execute(map.get("ID").toString(),input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        prodactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String,String> map = list.get(pos);
                if(Integer.parseInt(map.get("IsActive")) == 1)
                    new MyAdapter.updactasynccls().execute(map.get("ID").toString(),"0");
                else
                    new MyAdapter.updactasynccls().execute(map.get("ID").toString(),"1");
            }
        });

        return view;
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
            link = context.getResources().getString(R.string.URL)+"delete/product";

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
                context.startActivity(new Intent(context,DisplayProductActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    public class updasynccls extends AsyncTask<String,Void,String>
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
            link = context.getResources().getString(R.string.URL)+"update/product/stock";

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
                        .appendQueryParameter("Stock",params[1]);

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
                context.startActivity(new Intent(context,DisplayProductActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
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
            link = context.getResources().getString(R.string.URL)+"update/product/isactive";

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
                        .appendQueryParameter("IsActive",params[1]);

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
                context.startActivity(new Intent(context,DisplayProductActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
