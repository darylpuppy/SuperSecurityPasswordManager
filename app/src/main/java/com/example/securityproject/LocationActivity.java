package com.example.securityproject;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity{
    int PIN;

    private FusedLocationProviderClient client;
    //contains latitude and longitude to convert to address
    protected Location lastLocation;
    // handles the results of the address lookup
    //private AddressResultReceiver resultReceiver;

    String user;
    String address;
    String zipCode;
    Button buttonContinue;
    EditText etBackupPIN;
    private ArrayList<String> zipList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        PIN = (int)Math.floor(Math.random() * 100000);
        etBackupPIN = findViewById(R.id.etBackupPIN);
        user = getIntent().getStringExtra("user");
        address = getIntent().getStringExtra("address");
        zipCode = getIntent().getStringExtra("zipCode");
        buttonContinue = findViewById(R.id.buttonContinue);

        sendPIN(address);
    }

    public void sendPIN(String address){
        BackgroundMail bm = new BackgroundMail(this);
        bm.setGmailUserName("cs4389utd@gmail.com");
        bm.setGmailPassword("SuperSecurity1");
        bm.setMailTo(address);
        bm.setFormSubject("Second Confirmation");
        bm.setFormBody("Your security code is: " + PIN);
        bm.send();
    }

    public void startDialog(View v){
        String enteredPIN = etBackupPIN.getText().toString();
        if (enteredPIN == "" + PIN) {
            Bundle bundle = new Bundle();
            bundle.putString("newZipCode", zipCode);
            bundle.putString("user", user);
            //notify user  if location not recognized
            LocationDialog locationDialog = new LocationDialog();
            locationDialog.setArguments(bundle);
            locationDialog.show(getSupportFragmentManager(),"location dialog");
        }

    }
    /**
     * Start service with explicit intent
     *//*
    protected void startIntentService(){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        resultReceiver = new AddressResultReceiver(new Handler());
        //provide resultReceiver obj to handle results of address lookup
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        //provide location obj to service for conversion to an address
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);
    }

    /**
     * Handles response from FetchAddressIntentService
     *//*
    class AddressResultReceiver extends ResultReceiver{
        String addressOutput;
        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultData == null){
                return;
            }
            //Display the address string
            //or an error message sent from the intent service
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if(addressOutput == null){
                addressOutput = "";
            }
            //Display if address was found
            if(resultCode == Constants.SUCCESS_RESULT){
                displayAddressOutput();

            }
        }

        protected void displayAddressOutput(){
            //check users saved zip codes
            if(zipList.contains(addressOutput)){
                tvLocation.setText("Zip Code " + addressOutput + " verified.");
                //enable continue button
                buttonContinue.setEnabled(true);
            }else{
                Bundle bundle = new Bundle();
                bundle.putString("newZipCode", addressOutput);
                bundle.putString("user",user);
                //notify user  if location not recognized
                LocationDialog locationDialog = new LocationDialog();
                locationDialog.setArguments(bundle);
                locationDialog.show(getSupportFragmentManager(),"location dialog");
            }

        }

    }

    protected ArrayList<String> getZipCodes(){
        ArrayList<String> zips = new ArrayList<>();
        //open file
        try{
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(openFileInput(user)));
            String line;
            ArrayList<String> strList = new ArrayList<>();
            //retrieve zip codes
            while((line = fileReader.readLine())!=null){
                strList.add(line);
            }
            fileReader.close();
            for(int i = 4; i < strList.size(); i++){
                zips.add(strList.get(i));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            return zips;
        }
    }*/

}
