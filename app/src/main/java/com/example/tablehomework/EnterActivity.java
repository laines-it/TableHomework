package com.example.tablehomework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.widget.TextView;

import com.example.tablehomework.fragments.enter_dialog;
import com.example.tablehomework.fragments.suggestion_fragment;
import com.example.tablehomework.supports.Access;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class EnterActivity extends AppCompatActivity {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener listener;
    private SharedPreferences preferences;
    boolean lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        preferences = getApplicationContext().getSharedPreferences("com.example.tablehomework",Context.MODE_PRIVATE);
        preferences.edit().putString("version", "1.3").apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lock = false;
        listener = ref.child("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!lock){
                if(Double.parseDouble(preferences.getString("version","1.3")) >= ((double) snapshot.child("patch").getValue())){
                    String key = preferences.getString("enter","denied");
                    Log.e("ACCESS TO", String.valueOf(key));
                    if (key.equals("denied")){
                        DialogFragment df = new enter_dialog();
                        df.show(getSupportFragmentManager(),"what");
                    }else {
                        Intent intent = new Intent(EnterActivity.this, MainActivity.class);
                        intent.putExtra("access", key);
                        startActivity(intent);
                        ref.removeEventListener(listener);
                        Log.e("LISTENER","REMOVED");
                        lock = true;
                    }
                }else{
                    Intent intent = new Intent(EnterActivity.this, Update.class);
                    startActivity(intent);
                    ref.removeEventListener(listener);
                    Log.e("LISTENER","REMOVED");
                    lock = true;
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(listener);
        Log.e("LISTENER","REMOVED");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ref.removeEventListener(listener);
        Log.e("LISTENER","REMOVED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(listener);
        Log.e("LISTENER","REMOVED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ref.removeEventListener(listener);
        Log.e("LISTENER","REMOVED");
    }

//    public void clearApplicationData() {
//        File cache = getCacheDir();
//        File appDir = new File(cache.getParent());
//        if (appDir.exists()) {
//            String[] children = appDir.list();
//            for (String s : children) {
//                if (!s.equals("lib")) {
//                    deleteDir(new File(appDir, s));
//                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
//                }
//            }
//        }
//    }
//
//    public boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        return dir.delete();
//    }
}