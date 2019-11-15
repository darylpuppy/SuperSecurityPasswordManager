package com.example.securityproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.FileOutputStream;
import java.io.IOException;

public class LocationDialog extends AppCompatDialogFragment {
    public static final String message = "We do not recognize this location."
            + " Would you like us to save this location for future reference?";
    public String newZipCode;
    private String user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(getArguments() != null){
            newZipCode = getArguments().getString("newZipCode");
            user = getArguments().getString("user");
        }
        builder.setTitle("Zip Code " + newZipCode + " not recognized")
                .setMessage(message)
                .setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //logout and return user to login screen
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNeutralButton("Don't Save but Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Save and Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save new zip code to file
                        saveNewZip(newZipCode, user);

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
    private void saveNewZip(String zip, String user){
        try{
            FileOutputStream fileWriter = getActivity().openFileOutput(user, getActivity().MODE_PRIVATE | getActivity().MODE_APPEND);
            fileWriter.write((zip + "\n").getBytes());
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
