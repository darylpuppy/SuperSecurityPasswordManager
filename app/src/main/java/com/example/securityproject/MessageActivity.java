package com.example.securityproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity{
    int PIN;
    String phoneNumber;
    String emailAddress;
    String backupAddress;
    String user;
    private ArrayList<String> zipList;
    Button bText;
    Button bEmail;
    EditText etPIN;
    protected Location lastLocation;
    private FusedLocationProviderClient client;
    private AddressResultReceiver resultReceiver;

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
                backupAddress = emailAddress;
                sendPIN(phoneNumber);
            }
        });
        bEmail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                backupAddress = phoneNumber;
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
            zipList = getZipCodes();if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            } else {
                checkLocation();
            }
        }
    }

    public void checkLocation(){
        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("hello was a success");
                        lastLocation = location;
                        //Get last known location
                        if(lastLocation == null){
                            System.out.println("last location is null");
                            return;
                        }
                        if(!Geocoder.isPresent()){
                            Toast.makeText(MessageActivity.this, R.string.no_geocoder_available,Toast.LENGTH_LONG).show();
                            return;
                        }
                        System.out.println("about to start service");
                        //Start service
                        startIntentService();
                    }
                });
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocation();
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
    }
    protected void startIntentService(){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        resultReceiver = new MessageActivity.AddressResultReceiver(new Handler());
        //provide resultReceiver obj to handle results of address lookup
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        //provide location obj to service for conversion to an address
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);
        System.out.println("hello starting service");
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        String addressOutput;
        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            System.out.println("hello received location");
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
                checkAddressOutput();
            }
        }

        protected void checkAddressOutput(){
            //check users saved zip codes
            if(zipList.contains(addressOutput)){
                Intent intent = new Intent(MessageActivity.this, HomeActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(MessageActivity.this, LocationActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("address", backupAddress);
                intent.putExtra("zipCode", addressOutput);
                startActivity(intent);
            }

        }

    }
}
