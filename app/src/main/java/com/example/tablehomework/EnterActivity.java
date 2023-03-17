package com.example.tablehomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tablehomework.fragments.enter_dialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        if(preferences.getBoolean("first",true)){
            preferences.edit().putString("remove_splash","null").apply();
            findViewById(R.id.splash_image).setVisibility(View.VISIBLE);
            preferences.edit().putString("version", "1.51").apply();
            preferences.edit().putString("enter", "denied").apply();
            preferences.edit().putBoolean("first",false).apply();
        }
        if(preferences.getString("remove_splash","null").equals("none")){
            findViewById(R.id.splash_image).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lock = false;
        listener = ref.child("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!lock){
                if(Double.parseDouble(preferences.getString("version","1.51")) >= ((double) snapshot.child("patch").getValue())){
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
                    preferences.edit().putBoolean("first",true).apply();
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