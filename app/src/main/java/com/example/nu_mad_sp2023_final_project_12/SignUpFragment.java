package com.example.nu_mad_sp2023_final_project_12;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.example.nu_mad_sp2023_final_project_12.interfaces.DisplayTakenPhoto;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements DisplayTakenPhoto {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ImageView profilePic;
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText euserName;
    TextView tvLoginHere;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    StorageReference storageReference ;

    private FirebaseStorage storage;


    // TODO: Rename and change types of parameters
    private Uri URI;

    private static final int PERMISSIONS_CODE = 0x100;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(Uri param1) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            URI = getArguments().getParcelable(ARG_PARAM1);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);

        profilePic = view.findViewById(R.id.profilePicId);
        etRegEmail = view.findViewById(R.id.etRegEmail);
        etRegPassword = view.findViewById(R.id.etRegPass);
        tvLoginHere = view.findViewById(R.id.tvLoginHere);
        btnRegister = view.findViewById(R.id.btnRegister);
        euserName = view.findViewById(R.id.eUserName);

        if(URI!= null)
        {
            Glide.with(getContext()).load(URI).into(profilePic);
        }

        Boolean cameraAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Boolean readAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writeAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        profilePic.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if(cameraAllowed && readAllowed && writeAllowed){
                                                  Toast.makeText(getContext(), "All permissions granted!", Toast.LENGTH_SHORT).show();
                                                  /*getActivity().getSupportFragmentManager().beginTransaction()
                                                          .replace(R.id.rootLayoutId, new CameraFragment(SignUpFragment.this), "cameraFragment").addToBackStack(null)
                                                          .commit();*/

                                              }else{
                                                  requestPermissions(new String[]{
                                                          Manifest.permission.CAMERA,
                                                          Manifest.permission.READ_EXTERNAL_STORAGE,
                                                          Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                  }, PERMISSIONS_CODE);
                                              }
                                          }
                                      }
        );

        btnRegister.setOnClickListener(view1 ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view1 ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId,new LoginFragment()).addToBackStack(null).commit();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()!=0)
                {
                    /*Intent intent = new Intent(view.getContext(),InClass08.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                }
            }
        });

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>2){
            /*getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootLayoutId, new CameraFragment(SignUpFragment.this), "cameraFragment").addToBackStack(null)
                    .commit();*/
        }else{
            Toast.makeText(getContext(), "You must allow Camera and Storage permissions!", Toast.LENGTH_LONG).show();
        }
    }
    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String userName = euserName.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }
        else if(TextUtils.isEmpty(userName))
        {
            euserName.setError("Username is empty");
            euserName.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        UserData newUser = new UserData(userName,email,null);
                        DocumentReference docRef = db.collection("users").document(newUser.getEmail());
                        docRef.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId,new HomeFragment(),"signUptoHome").addToBackStack(null).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Registration Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("regError", "onFailure: " + e.getMessage());
                            }
                        });


                                /*task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                UserData newUser = new UserData(userName,email,uri.toString());
                                                DocumentReference docRef = db.collection("users").document(newUser.getEmail());
                                                docRef.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId,ChatFragment.newInstance(newUser),"signUpToChat").addToBackStack(null).commit();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Registration Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.d("regError", "onFailure: " + e.getMessage());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });*/
                    }else{
                        Toast.makeText(getContext(), "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    public void displayphoto(Uri URI) {
        this.URI = URI;
        Log.d("checking in prevFrag",URI.toString());
        Glide.with(getContext()).load(URI).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return false;
            }
        }).into(profilePic);
    }

    @Override
    public void displayphotoFromgallery(Uri URI) {
        this.URI = URI;
        Log.d("checking in prevFrag",URI.toString());
        Glide.with(getContext()).load(URI).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId,SignUpFragment.newInstance(URI),"galleryToSignup").commit();
                return false;
            }
        }).into(profilePic);

    }
}