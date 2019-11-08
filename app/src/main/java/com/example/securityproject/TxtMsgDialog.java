package com.example.securityproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TxtMsgDialog extends AppCompatDialogFragment {
    private EditText etSecurityCode;
    private CharSequence message = "Please enter the security code sent to your phone via text message.";
    private String code = "1234"; //for testing, need to refer to actual code generated
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_txtmsg_dialog,null);
        etSecurityCode = view.findViewById(R.id.etSecurityCode);
        builder.setView(view)
                .setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String securityCode =  etSecurityCode.getText().toString();
                        if(securityCode.equals(code)){
                            //start email activity
                            Intent intent = new Intent(getActivity(),EmailActivity.class);
                            startActivity(intent);
                        }else{
                            //force logout and return user to login activity (main)

                        }
                    }
                });
        return builder.create();
    }
}
