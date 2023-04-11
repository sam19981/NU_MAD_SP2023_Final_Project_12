package com.example.nu_mad_sp2023_final_project_12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
      getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId, new LoginFragment(), "maintologin").commit();
       //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayoutId,new HomeFragment(),"hello").commit();
    }
}