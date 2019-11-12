package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CreateAccount extends AppCompatActivity {
    EditText etCreateUsername;
    EditText etCreatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etCreateUsername = (EditText) findViewById(R.id.etCreateUsername);
        etCreatePassword = (EditText) findViewById(R.id.etCreatePassword);
    }

    public void createAccount(View V){
        try {
            FileOutputStream fileWriter = openFileOutput(etCreateUsername.getText().toString(), MODE_PRIVATE);
            fileWriter.write(etCreatePassword.getText().toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
