package com.example.nu_mad_sp2023_final_project_12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId, new LoginFragment(), "maintologin").commit();
       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayoutId,new HomeFragment(),"hello").commit();
    }
}