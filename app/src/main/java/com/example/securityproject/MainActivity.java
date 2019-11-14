package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
    }

    public void signIn(View v){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(openFileInput(username)));
            String realPassword = fileReader.readLine();
            String phoneNumber = fileReader.readLine();
            String emailAddress = fileReader.readLine();

            if (realPassword.equals(password)){
                Intent intent = new Intent(this, MessageActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("emailAddress", emailAddress);
                startActivity(intent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(View v){
        Log.i("MainActivity", "creating account");
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }
}
