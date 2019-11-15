package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

public class MessageActivity extends AppCompatActivity{
    int PIN;
    String phoneNumber;
    String emailAddress;
    String user;
    Button bText;
    Button bEmail;
    EditText etPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        PIN = (int)Math.floor(Math.random() * 100000);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        emailAddress = getIntent().getStringExtra("emailAddress");
        user = getIntent().getStringExtra("user");
        bText = findViewById(R.id.bText);
        bEmail = findViewById(R.id.bEmail);
        etPIN = findViewById(R.id.etPIN);

        bText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendPIN(phoneNumber);
            }
        });
        bEmail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendPIN(emailAddress);
            }
        });
    }

    public void sendPIN(String address){
            BackgroundMail bm = new BackgroundMail(this);
            bm.setGmailUserName("cs4389utd@gmail.com");
            bm.setGmailPassword("SuperSecurity1");
            bm.setMailTo(address);
            bm.setFormSubject("Confirmation");
            bm.setFormBody("Your security code is: " + PIN);
            bm.send();
    }

    public void submitPIN(View v){
        String inputPIN = etPIN.getText().toString();
        if (inputPIN.equals("" + PIN)){
            Intent intent = new Intent(this, LocationActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
}
