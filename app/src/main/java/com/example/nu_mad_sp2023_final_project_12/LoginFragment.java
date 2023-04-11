package com.example.nu_mad_sp2023_final_project_12;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.inclass_sankara_narayanan_002787959.R;
import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText etLoginEmail;
    private TextInputEditText etLoginPassword;
    private TextView tvRegisterHere;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataBase;
    private FirebaseUser mUser;
    private MainActivity parentActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        parentActivity =(MainActivity)getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_login, container, false);

        etLoginEmail = view.findViewById(R.id.etLoginEmail);
        etLoginPassword = view.findViewById(R.id.etLoginPass);
        tvRegisterHere = view.findViewById(R.id.tvRegisterHere);
        btnLogin = view.findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(view1 -> {
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view1 ->{
            parentActivity.replaceFragment(new SignUpFragment(),"LoginTosignUp");
        });

        return view;

    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        dataBase.collection("users").document(mUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    UserData  currentUser = documentSnapshot.toObject(UserData.class);
                                    MainActivity.setCurrentBio(currentUser);
                                    parentActivity.replaceFragment(new HomeFragment(),"logintohome");
                                }
                            }
                        });

                    }else{
                        Toast.makeText(getContext(), "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}