package com.example.root.seller_point;

import android.app.ActionBar;
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

public class DisplayDetailActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    SliderLayout slider;
    ImageView img;
    ArrayList<HashMap<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

//        slider = findViewById(R.id.slider);
        img = findViewById(R.id.img);

        new DisplayTask().execute();
    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
            String link = getResources().getString(R.string.URL)+"api/tblProductImages/ProductID~2";

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
                HashMap<String,Bitmap> map = new HashMap<String, Bitmap>();

                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

//                    map.put("ProductID",jsonObject1.getString("ProductID"));
                    byte[] imageBytes = Base64.decode(jsonObject1.getString("Image"), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                    image.setImageBitmap(decodedImage);
                    map.put("Image", decodedImage);
//                    list.add(map);
                }

                img.setImageBitmap(map.get("Image"));

//                for(String name : map.keySet()){
//                    TextSliderView textSliderView = new TextSliderView(DisplayDetailActivity.this);
//                    // initialize a SliderLayout
//                    textSliderView
//                            .description("")
//                            .image(String.valueOf((Bitmap)map.get(name)))
//                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                            .setOnSliderClickListener(DisplayDetailActivity.this);
//
//                    //add your extra information
//                    textSliderView.bundle(new Bundle());
//                    textSliderView.getBundle()
//                            .putString("extra",name);
//
//                    slider.addSlider(textSliderView);
//                }
//
//                slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                slider.setCustomAnimation(new DescriptionAnimation());
//                slider.setDuration(2000);
//                slider.addOnPageChangeListener(DisplayDetailActivity.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);

        }
    }
}
