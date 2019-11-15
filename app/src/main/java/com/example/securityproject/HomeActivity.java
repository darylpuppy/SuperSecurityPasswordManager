package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            Intent i = getIntent();
            EntryList entryList = (EntryList) i.getSerializableExtra("MyClass");

            arrayList.add(entryList.toString(entryList.length()-1));

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
            listView = findViewById(R.id.EntryList);
            listView.setAdapter(arrayAdapter);
        }
        catch (Exception e){

        }
    }

    public void createEntry(View V) {
        openDialog();
    }

    public void openDialog(){
        EntryDialog entryDialog = new EntryDialog();
        entryDialog.show(getSupportFragmentManager(),"entry dialog");
    }

    public void logout(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
