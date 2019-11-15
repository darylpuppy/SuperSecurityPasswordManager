package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateAccount extends AppCompatActivity {
    EditText etCreateUsername;
    EditText etCreatePassword;
    EditText etPhone;
    EditText etEmail;
    EditText etZipCode;
    Spinner sProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etCreateUsername = findViewById(R.id.etCreateUsername);
        etCreatePassword = findViewById(R.id.etCreatePassword);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        sProvider = findViewById(R.id.sProvider);
        etZipCode = findViewById(R.id.etZipCode);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new String[] {"AT&T", "T-Mobile", "Sprint", "Verizon", "Virgin Mobile"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sProvider.setAdapter(adapter);
    }

    public void createAccount(View V){
        try {
            FileOutputStream fileWriter = openFileOutput(etCreateUsername.getText().toString(), MODE_PRIVATE);
            String[] domains = new String[] {"txt.att.net", "tmomail.net", "messaging.sprintpcs.com", "vtext.com", "vmobl.com"};
            String phoneNumber = etPhone.getText().toString().replace("-", "").replace("(", "").replace(")", "").replace("+", "").replace(" ", "") + "@" + domains[sProvider.getSelectedItemPosition()];
            String emailAddress = etEmail.getText().toString();
            String zipCode = etZipCode.getText().toString();
            if(zipCode.length() != 5) {
                Toast.makeText(this, "Zip Code invalid format", Toast.LENGTH_LONG).show();
                etZipCode.setText("");
            }else{
                try{
                    Integer.parseInt(zipCode);
                }catch(NumberFormatException e){
                    Toast.makeText(this,"Zip Code invalid format",Toast.LENGTH_LONG).show();
                    etZipCode.setText("");
                }
            }
            fileWriter.write((etCreatePassword.getText().toString() + "\n").getBytes());
            fileWriter.write((phoneNumber + "\n").getBytes());
            fileWriter.write((emailAddress + "\n").getBytes());
            fileWriter.write((zipCode + "\n").getBytes());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
