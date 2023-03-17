package com.example.tablehomework.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.Loader;

import com.example.tablehomework.MainActivity;
import com.example.tablehomework.R;
import com.example.tablehomework.supports.LinkFromLesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class add_link_dialog extends DialogFragment {
    String subject;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_add_link, null);
        Button b = v.findViewById(R.id.add_link_to_sj);
        EditText link = v.findViewById(R.id.add_link);
        EditText image = v.findViewById(R.id.add_image_link);
        TextView title = v.findViewById(R.id.name_of_added_link);
        subject = getArguments().getString("SUBJECTNAME","");
        assert getArguments() != null;
        title.setText("Ссылка для " + subject);
        b.setOnClickListener(view -> {
            if(!link.getText().equals("")){
                b.setClickable(false);
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
                String path = "testtable/" + preferences.getString("enter","default") + "/link/" + subject + "/";
                LinkFromLesson l = new LinkFromLesson();
                l.setLink(link.getText().toString());
                if(image.getText().toString().equals("")){
                    l.setImage("null");
                }else{
                    l.setImage(image.getText().toString());
                }
                FirebaseDatabase.getInstance().getReference(path).setValue(l);
                Toast.makeText(getActivity(),"Ссылка создана",Toast.LENGTH_LONG).show();
                dismiss();
            }else{
                Toast.makeText(getActivity(),"Error: empty link", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        return v;
    }
}

