package com.example.dictionaryapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dictionaryapp.fragments.LearnedWordsFragment;
import com.example.dictionaryapp.fragments.MainFragment;
import com.example.dictionaryapp.fragments.TranslateFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        switch (id) {
            case R.id.page_1:
                fragment = new TranslateFragment();
                break;
            case R.id.page_2:
                fragment = new MainFragment();
                break;
            case R.id.page_3:
                fragment = new LearnedWordsFragment();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.frameLayout, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }
        return true;
    }
}