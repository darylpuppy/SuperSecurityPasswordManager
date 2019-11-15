package com.example.securityproject;

import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class EntryDialog extends AppCompatDialogFragment{
    EditText websiteName;
    EditText usernameName;
    EditText password;

    EntryList entryList = new EntryList();
    Entry entry;

    String domain;
    String username;
    String pass;

    private CharSequence message = "Please enter your domain and login information: ";

    int x = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_entry_dialog, null);
        websiteName = view.findViewById(R.id.websiteName);
        usernameName = view.findViewById(R.id.usernameName);
        password = view.findViewById(R.id.password);
        builder.setView(view)
                .setMessage(message)
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        domain = websiteName.getText().toString();
                        username = usernameName.getText().toString();
                        pass = password.getText().toString();

                        entry = new Entry(domain, username, pass);
                        entryList.add(entry);

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("MyClass", entryList);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
