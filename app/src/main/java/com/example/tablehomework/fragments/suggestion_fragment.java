package com.example.tablehomework.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.Loader;

import com.example.tablehomework.MainActivity;
import com.example.tablehomework.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class suggestion_fragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog, null);
        getDialog().setTitle("Сообщение разработчику");
        Button send = v.findViewById(R.id.send);
        EditText et = v.findViewById(R.id.et);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et.getText().equals("")){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("suggestions");
                    ref.push().setValue(et.getText().toString());
                    send.setClickable(false);
                    Toast.makeText(getActivity(),"Сообщение отправлено",Toast.LENGTH_LONG).show();
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Сообщение не отправлено", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
        });
        return v;
    }
}

