package com.example.nu_mad_sp2023_final_project_12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private static UserData currentBio;

    public static UserData getCurrentBio() {
        return currentBio;
    }

    public static void setCurrentBio(UserData currentUser) {
        currentBio = currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        System.out.println(currentUser);
        goToNextScreen();
    }

    public void goToNextScreen()
    {
        if(currentUser!= null)
        {
            // go to the chatScreen
            Log.d("email",currentUser.getEmail());
            db.collection("users").document(currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful())
                    {   DocumentSnapshot documentSnapshot = task.getResult();
                        UserData  User = documentSnapshot.toObject(UserData.class);
                        MainActivity.setCurrentBio(User);
                        Log.d("data",currentBio.toString());
                        replaceFragment(new HomeFragment(),"logintohome");
                    }
                }
            });
        }
        else {
            replaceFragment(new LoginFragment(),"ActivityToLogin");
        }
    }

    public void replaceFragment(Fragment fragment,String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rootLayoutId, fragment,tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}