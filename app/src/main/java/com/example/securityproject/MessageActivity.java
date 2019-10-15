package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

public class MessageActivity extends AppCompatActivity {
    EditText etNumber;
    Spinner sProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        etNumber = findViewById(R.id.etNumber);
        sProvider = findViewById(R.id.sProvider);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new String[] {"AT&T", "T-Mobile", "Sprint", "Verizon", "Virgin Mobile"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sProvider.setAdapter(adapter);
    }

    public void sendMessage(View v){
        String[] domains = new String[] {"txt.att.net", "tmomail.net", "messaging.sprintpcs.com", "vtext.com", "vmobl.com"};
        String domain = domains[sProvider.getSelectedItemPosition()];
        String number = etNumber.getText().toString().replace("-", "").replace("(", "").replace(" ", "");

        boolean validNumber = true;
        if (number.length() == 10){
            for (int i = 0;i < number.length();i++){
                if (((int) number.charAt(i)) < 48 || ((int) number.charAt(i)) > 57){
                    validNumber = false;
                }
            }
        }
        if (validNumber) {
            BackgroundMail bm = new BackgroundMail(this);
            bm.setGmailUserName("cs4389utd@gmail.com");
            bm.setGmailPassword("SuperSecurity1");
            bm.setMailTo(number + "@" + domain);
            bm.setFormSubject("Confirmation");
            bm.setFormBody("Your security code is: 1234");
            bm.send();
        }
    }
}
