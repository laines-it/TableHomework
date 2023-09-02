package com.example.tablehomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tablehomework.databinding.ActivityMainBinding;
import com.example.tablehomework.fragments.TimetableEditActivity;
import com.example.tablehomework.fragments.add_link_dialog;
import com.example.tablehomework.fragments.death_calendar_fragment;
import com.example.tablehomework.fragments.edit_fragment;
import com.example.tablehomework.fragments.suggestion_fragment;
import com.example.tablehomework.fragments.timetable_fragment;
import com.example.tablehomework.supports.MovableFloatingActionButton;
import com.example.tablehomework.supports.VPAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.tablehomework.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getApplicationContext().getSharedPreferences("com.tablehomework",MODE_PRIVATE);
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("social").child("freeid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(preferences.getInt("id",0) == 0) {
                    preferences.edit().putInt("id", snapshot.getValue(Integer.class)).apply();
                    FirebaseDatabase.getInstance().getReference("social/freeid/").setValue(snapshot.getValue(Integer.class) + 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new death_calendar_fragment(),"Календарь Смерти");
        vpAdapter.addFragment(new timetable_fragment(),"Расписание");
        vpAdapter.addFragment(new edit_fragment(),"Добавить/удалить ДЗ");
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(vpAdapter);
        viewPager.setCurrentItem(1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.help:
            DialogFragment df = new suggestion_fragment();
            df.show(getSupportFragmentManager(),"what");
            return(true);
        case R.id.yd:
            Intent intent_yd;
            ref.child("social").child("links").child("disk").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String uri_string = snapshot.getValue(String.class);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri_string)));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            return(true);
        case R.id.edit_tt:
            Intent intent2edit = new Intent(MainActivity.this, TimetableEditActivity.class);
            startActivity(intent2edit);
            return(true);
        case R.id.freerooms:
            ref.child("version").child("rooms_actual").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(Boolean.class)){
                        Intent intent2room = new Intent(MainActivity.this, Freerooms.class);
                        startActivity(intent2room);
                    }else{
                        Toast.makeText(getApplicationContext(),"Раздел находится в доработке",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}