package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        etEmail = findViewById(R.id.etEmail);
    }

    public void sendMessage(View v){
        String email = etEmail.getText().toString();
        if(validFormat(email)){
            openDialog();
        }else{
            Toast.makeText(this,"Email Invalid",Toast.LENGTH_LONG).show();
        }
    }

    private boolean validFormat(String e){
        if(e.contains("@") && e.contains("com")){
                return true;
            }else {
                return false;
        }
    }

    public void openDialog(){
        EmailDialog emailDialog = new EmailDialog();
        emailDialog.show(getSupportFragmentManager(),"email dialog");
    }
}
