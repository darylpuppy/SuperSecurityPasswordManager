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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity{

    private FusedLocationProviderClient client;
    //contains latitude and longitude to convert to address
    protected Location lastLocation;
    // handles the results of the address lookup
    private AddressResultReceiver resultReceiver;
    private TextView tvLocation;

    String user;
    Button buttonContinue;
    private ArrayList<String> zipList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        user = getIntent().getStringExtra("user");
        tvLocation = findViewById(R.id.tvLocation);
        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setEnabled(false);
        //get zip codes from user file to verify
        zipList = getZipCodes();
        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lastLocation = location;
                        //Get last known location
                        if(lastLocation == null){
                            return;
                        }
                        if(!Geocoder.isPresent()){
                            Toast.makeText(LocationActivity.this, R.string.no_geocoder_available,Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Start service
                        startIntentService();
                    }
                });
    }

    public void goToHome(View v){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);

    }
    /**
     * Start service with explicit intent
     */
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
     */
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
    }

}
