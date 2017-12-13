package com.example.root.seller_point;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView txtreg,txtuname,txtpwd;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                if(txtuname.getText().toString().equals("Admin") && txtpwd.getText().toString().equals("Admin")){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        });

    }
}
