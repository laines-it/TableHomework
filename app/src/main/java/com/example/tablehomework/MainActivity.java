package com.example.tablehomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tablehomework.databinding.ActivityMainBinding;
import com.example.tablehomework.fragments.death_calendar_fragment;
import com.example.tablehomework.fragments.edit_fragment;
import com.example.tablehomework.fragments.suggestion_fragment;
import com.example.tablehomework.fragments.timetable_fragment;
import com.example.tablehomework.supports.VPAdapter;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.tablehomework.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        case R.id.google_drive:
            Uri uri_google_drive = Uri.parse("https://drive.google.com/drive/folders/19o31Le0JrPWQwH1sp_X56B-BID5KeVN8");
            Intent intent_v = new Intent(Intent.ACTION_VIEW, uri_google_drive);
            startActivity(intent_v);
            return(true);
        case R.id.yd:
            Uri uri_yd = Uri.parse("https://disk.yandex.ru/d/L0C0IivmAD3N5w");
            Intent intent_yd = new Intent(Intent.ACTION_VIEW, uri_yd);
            startActivity(intent_yd);
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

}