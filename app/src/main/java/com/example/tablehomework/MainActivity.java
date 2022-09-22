package com.example.tablehomework;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tablehomework.fragments.dz_fragment;
import com.example.tablehomework.fragments.edit_fragment;
import com.example.tablehomework.fragments.timetable_fragment;
import com.example.tablehomework.supports.VPAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablehomework.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new timetable_fragment(),"Расписание");
        vpAdapter.addFragment(new edit_fragment(),"Добавить/удалить ДЗ");
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(vpAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.telegram:
            Uri uri_telegram = Uri.parse("https://t.me/+HNJkWE1MqUZkOWYy");
            Intent intent_t = new Intent(Intent.ACTION_VIEW, uri_telegram);
            startActivity(intent_t);
            return(true);
        case R.id.vk:
            Uri uri_vk = Uri.parse("https://vk.me/join/RdgXMkYXHGelTvwj4x6yCCw2PHvSFu9AGFg=");
            Intent intent_v = new Intent(Intent.ACTION_VIEW, uri_vk);
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