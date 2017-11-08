package com.example.root.seller_point;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class DisplayProductActivity extends AppCompatActivity {

    FloatingActionButton fabAddProd;
    TextView tv;
    ImageView img;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

        fabAddProd = findViewById(R.id.fabAddProduct);
        Button b =findViewById(R.id.button);

        tv = (TextView)findViewById(R.id.textView2);
        img = (ImageView)findViewById(R.id.imageView2);

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setType("image/*");
                //pickIntent.setAction();

                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                chooserIntent.putExtra
                        (
                                Intent.EXTRA_INITIAL_INTENTS,
                                new Intent[]{takePhotoIntent}
                        );

                startActivityForResult(chooserIntent, 123);
            }
        });
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(data == null)
        {}
        else if(data.getData()==null && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap)data.getExtras().get("data");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            tv.setText(photo.toString());//Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
            img.setImageBitmap(photo);
        }
        else if(data.getData()!=null && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap map = null;
            try {
                map = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

            tv.setText(map.toString());//Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
            img.setImageBitmap(map);
        }
    }

    public void fabAddProd_click(View v){
        Intent i = new Intent(this,ProductActivity.class);
        startActivity(i);
    }
}
