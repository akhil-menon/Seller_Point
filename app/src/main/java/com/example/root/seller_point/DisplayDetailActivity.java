package com.example.root.seller_point;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayDetailActivity extends AppCompatActivity {

    TextView txtprod,txtcategory,txtprice,txtdesc,txtoffer,txtaccount;
    ViewFlipper imageFlipper;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    int prodid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        txtprod = findViewById(R.id.prod_title);
        txtcategory = findViewById(R.id.prod_category);
        txtprice = findViewById(R.id.prod_price);
        txtdesc = findViewById(R.id.prod_desc);
        txtoffer = findViewById(R.id.prod_offer);
        txtaccount = findViewById(R.id.prod_account);

        imageFlipper = findViewById( R.id.image_flipper );

        Intent intent = getIntent();
        prodid = intent.getIntExtra("ID",0);

        new imgDisplayTask().execute();

        new DisplayTask().execute();

        new OfferDisplayTask().execute();

        imageFlipper.setFlipInterval( 3000 );
        imageFlipper.setInAnimation(this,R.anim.grow_to_middle);
        imageFlipper.setOutAnimation(this,R.anim.shrink_to_middle);
        imageFlipper.startFlipping();
    }

    public class imgDisplayTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"api/tblProductImages/ProductID~"+prodid;

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
                    ImageView image = new ImageView ( getApplicationContext() );

                    byte[] imageBytes = Base64.decode(jsonObject1.getString("Image"), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    image.setImageBitmap(decodedImage);
                    imageFlipper.addView( image );
                }

            } catch (JSONException e) {
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
            String link = getResources().getString(R.string.URL)+"/api/tblProduct%20p,tblCategory%20c,tblAccountType%20a/p.*,a.Name%20as%20Account,c.Name%20as%20CategoryName/p.Category~c.ID,p.AccountID~a.ID,p.ID~"+prodid;

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
                HashMap<String,String> map = new HashMap<String, String>();

                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

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
                    map.put("Account",jsonObject1.getString("Account"));
                }

                txtprod.setText(map.get("Name"));
                txtcategory.setText(map.get("CategoryName"));
//                txtstock.setText(map.get("Stock"));
                txtdesc.setText(map.get("Desc"));
//                txtdiscount.setText(map.get("Flat_Discount"));
//                txtcashoff.setText(map.get("Cash_Discount"));
                txtprice.setText("Rs "+map.get("Price"));
                txtaccount.setText(map.get("Account"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }

    public class OfferDisplayTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String link = getResources().getString(R.string.URL)+"/api/tblOffer/ProdID~"+prodid;

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

                    if(i == 0){
                        txtoffer.setText(jsonObject1.getString("Name")+"\n");
                    }
                    else{
                        txtoffer.append("\n \n"+jsonObject1.getString("Name")+"\n");
                    }
                    txtoffer.append(jsonObject1.getString("Description")+"\n");
                    txtoffer.append(jsonObject1.getString("Discount"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
