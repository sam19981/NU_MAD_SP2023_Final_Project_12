package com.example.nu_mad_sp2023_final_project_12;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.Adapter.ViewPagerAdapter;
import com.example.nu_mad_sp2023_final_project_12.interfaces.DisplayTakenPhoto;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DisplayTakenPhoto {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton pBtn;
    private TextView name;
    private CircleImageView profilepic;
    private ImageView changeProfilePic;
    private UserData currentUserBio;
    private MainActivity parentActivity;
    private ImageView logOut;
    private FirebaseAuth mAuth;
    private StorageReference storageReference ;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    private static final int PERMISSIONS_CODE = 0x100;




    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        db = FirebaseFirestore.getInstance();
        currentUserBio = MainActivity.getCurrentBio();
        parentActivity = (MainActivity)getActivity();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        name = view.findViewById(R.id.id_fullName_TextView);
        profilepic = view.findViewById(R.id.id_Profile_Image);
        changeProfilePic = view.findViewById(R.id.changeProfilePicId);
        logOut = view.findViewById(R.id.logout);
        name.setText(currentUserBio.getName());
        Glide.with(parentActivity).load(currentUserBio.getProfilepicture()).into(profilepic);

        Boolean cameraAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Boolean readAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writeAllowed = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checking","profile");
                if(cameraAllowed && readAllowed && writeAllowed){
                    Toast.makeText(getContext(), "All permissions granted!", Toast.LENGTH_SHORT).show();
                    parentActivity.replaceFragment(new CameraFragment(HomeFragment.this),"profilepicChange");


                }else{
                    requestPermissions(new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_CODE);
                }
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                MainActivity.setCurrentBio(null);
                parentActivity.replaceFragment(new LoginFragment(),"HomeToLogin");
            }
        });

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new JobsFragment());
        fragments.add(new JobHistory());
        fragments.add(new ChatFragment());

        viewPager = (ViewPager2) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(parentActivity.getSupportFragmentManager(),getLifecycle(), fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the tab title based on the position of the current page
            switch (position) {
                case 0:
                    tab.setText("Jobs");
                    break;
                case 1:
                    tab.setText("History");
                    break;
                case 2:
                    tab.setText("Chats");
                    break;
            }
        }).attach();
        pBtn = view.findViewById(R.id.postbtn);
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.replaceFragment(new PostJob(),"hometopost");
            }
        });

        return view;
    }

    @Override
    public void displayphoto(Uri URI) {
        storageReference = storage.getReference().child("images/"+URI.getLastPathSegment());
        UploadTask uploadImage  = storageReference.putFile(URI);
        uploadImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("users").document(currentUserBio.getEmail()).update("profilepicture",uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                UserData updateUserBio = MainActivity.getCurrentBio();
                                updateUserBio.setProfilepicture(uri.toString());
                                MainActivity.setCurrentBio(updateUserBio);
                                parentActivity.replaceFragment(new HomeFragment(),"newhome");
                                Toast.makeText(parentActivity, "Profile changed Successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public void displayphotoFromgallery(Uri URI) {
        storageReference = storage.getReference().child("images/"+URI.getLastPathSegment());
        UploadTask uploadImage  = storageReference.putFile(URI);
        uploadImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("users").document(currentUserBio.getEmail()).update("profilepicture",uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                UserData updateUserBio = MainActivity.getCurrentBio();
                                updateUserBio.setProfilepicture(uri.toString());
                                MainActivity.setCurrentBio(updateUserBio);
                                parentActivity.replaceFragment(new HomeFragment(),"newhome");
                                Toast.makeText(parentActivity, "Profile changed Successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }
}