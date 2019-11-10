package com.example.securityproject;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Address lookup service for Location Activity.
 * Runs in the background and fetches the address corresponding to a given geographic location
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver receiver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * Converts a geographic location to an address.
     * Uses a Geocoder to fetch the address for the location
     * and sends the results to the ResultReceiver.
     * Returns the string containing the address on a successful reverse;
     * otherwise the string contains the error message.
     * @param intent This service
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent == null) {
            return;
        }
        String errorMessage = "";

        // Get the location passed to this service through
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);
        //Get the receiver passed to this service through extra
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        List<Address> addresses = null;

        try {
            //Returns one address corresponding to this geographical location
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            //Sends results back to LocationActivity
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

    /**
     * Returns address to back to ResultReceiver in the activity that started the service
     * @param resultCode Numeric code reports success or failure of geocoding request
     * @param message If reverse geocding successful, returns address;
     *                otherwise, returns reason for failure
     */
    private void deliverResultToReceiver(int resultCode, String message){
        //Construct msg bundle
        Bundle bundle = new Bundle();
        //Concatenate constant and value in message
        bundle.putString(Constants.RESULT_DATA_KEY,message);
        receiver.send(resultCode,bundle);
    }
}
