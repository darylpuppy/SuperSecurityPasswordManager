package com.example.securityproject;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationActivity extends AppCompatActivity{

    private FusedLocationProviderClient client;
    //contains latitude and longitude to convert to address
    protected Location lastLocation;
    // handles the results of the address lookup
    private AddressResultReceiver resultReceiver;
    TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
       // tvLocation = findViewById(R.id.tvLocation);
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

    public void allowLocation(View v){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);

    }
    public void denyLocation(View v){
        //force logout
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
            //displayAddressOutput();
            Log.i("LocationActivity",addressOutput);
            //Show toast msg if address was found
            if(resultCode == Constants.SUCCESS_RESULT){
                Toast.makeText(LocationActivity.this,R.string.address_found, Toast.LENGTH_LONG).show();
            }
        }
       //

    }

}
