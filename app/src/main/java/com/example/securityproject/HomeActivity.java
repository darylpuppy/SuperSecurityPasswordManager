package com.example.securityproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();

    String domain;
    String username;
    String pass;

    Entry entry;
    EntryList entryList = new EntryList();

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button addEntry = findViewById(R.id.bAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                View view = getLayoutInflater().inflate(R.layout.layout_entry_dialog, null);
                final EditText websiteName = view.findViewById(R.id.websiteName);
                final EditText usernameName = view.findViewById(R.id.usernameName);
                final EditText password = view.findViewById(R.id.password);

                builder.setMessage("Please enter your domain and login information: ");

                builder.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        domain = websiteName.getText().toString();
                        username = usernameName.getText().toString();
                        pass = password.getText().toString();

                        entry = new Entry(domain, username, pass);
                        entryList.add(entry);

                        arrayList.add(entryList.toString(entryList.length()-1));

                        arrayAdapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        listView = findViewById(R.id.EntryList);
                        listView.setAdapter(arrayAdapter);
                    }
                });

                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void logout(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
