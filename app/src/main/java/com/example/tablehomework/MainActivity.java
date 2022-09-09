package com.example.tablehomework;

import android.os.Bundle;

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
        vpAdapter.addFragment(new dz_fragment(),"ДЗ");
        vpAdapter.addFragment(new timetable_fragment(),"Расписание");
        vpAdapter.addFragment(new edit_fragment(),"Добавить/удалить ДЗ");
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(vpAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

}