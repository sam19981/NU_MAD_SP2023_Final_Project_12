package com.example.nu_mad_sp2023_final_project_12;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.nu_mad_sp2023_final_project_12.interfaces.DisplayTakenPhoto;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostJob#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostJob extends Fragment implements DisplayTakenPhoto {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText job_name;
    private EditText job_pay;
    private EditText job_time;
    private EditText job_desc;

    private ImageView job_img;

    private Uri job_url;

    private MainActivity mainActivity;
    private Button jobPost;

    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    private TextView locationtxt;

    private FirebaseFirestore db;

    private ImageView backBtn;
    private StorageReference storageReference;

    private FirebaseStorage storage;

    double latitude = 0 ;
    double longitude= 0;

    String placeName;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private Button locBtn;

    FusedLocationProviderClient fusedLocationProviderClient;
    private FusedLocationProviderClient fusedLocationClient;





    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // This method is called when the user's location changes
            // You can use the location object to get the latitude and longitude
            Location location = locationResult.getLastLocation();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // Do something with the latitude and longitude
        }
    };


    public PostJob() {
        // Required empty public constructor
    }

    public PostJob(Uri URI) {
        // Required empty public constructor
        this.job_url = URI;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostJob.
     */
    // TODO: Rename and change types and number of parameters
    public static PostJob newInstance(String param1, String param2) {
        PostJob fragment = new PostJob();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }



    private void getLocation() {
        // Check if the app has permission to access the device's location
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the current location
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Use the current location
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        // Do something with the latitude and longitude
                        Log.d("loc", "onSuccess: " + latitude + " " + longitude);


                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null && !addresses.isEmpty()) {
                            placeName = addresses.get(0).getAddressLine(0);
                            Log.d("Place Name", placeName);
                            locationtxt.setText(placeName);

                        } else {
                            Log.d("Place Name", "Unknown");
                        }


                    }

                }
            });

        } else {
            Log.d("else", "getLocation: request permission");
            // Permission not granted, request the permission from the user
            checkLocationPermission();
        }
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
        mUser = mAuth.getCurrentUser();
        mainActivity = (MainActivity) getActivity();
        storage = FirebaseStorage.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        job_url = null;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Proceed with your code logic
            } else {
                // Permission denied
                // Handle the user's response to the permission request
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_job, container, false);
        job_name = view.findViewById(R.id.j_name);
        job_desc = view.findViewById(R.id.j_desc);
        job_pay = view.findViewById(R.id.j_pay);
        job_time = view.findViewById(R.id.j_dur);
        jobPost = view.findViewById(R.id.post_btn);
        backBtn = view.findViewById(R.id.post_back);
        job_img = view.findViewById(R.id.j_img);
        locBtn = view.findViewById(R.id.locatebtn);
        locationtxt = view.findViewById(R.id.loctext);
        locBtn.setVisibility(View.VISIBLE);


        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locBtn.setVisibility(View.INVISIBLE);
                getLocation();

            }
        });



        if(job_url != null){
            Glide.with(getContext()).load(job_url)
                    .into(job_img);
        }



        job_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new CameraFragment(PostJob.this),"posttocamera");

            }
        });


        jobPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String jobName = job_name.getText().toString();
                String x = String.valueOf(latitude);
                String y = String.valueOf(longitude);
                String job_desc_val = job_desc.getText().toString();
                String status = "available";
                String wageVal = job_pay.getText().toString();
                String jobDur = job_time.getText().toString();
                if (jobDur.isEmpty()) {
                    Toast.makeText(getContext(), "job duration cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else if (jobName.isEmpty()) {
                    Toast.makeText(getContext(), "job name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (job_desc_val.isEmpty()) {
                    Toast.makeText(getContext(), "job description cannot be empty", Toast.LENGTH_SHORT).show();

                } else if (wageVal.isEmpty()) {
                    Toast.makeText(getContext(), "wage cannot be empty", Toast.LENGTH_SHORT).show();

                } else if (x.isEmpty() || y.isEmpty()) {
                    Toast.makeText(getContext(), "location cannot be empty", Toast.LENGTH_SHORT).show();

                } else if (job_url == null) {
                    Toast.makeText(getContext(), "job image cannot be empty", Toast.LENGTH_SHORT).show();

                }

                else{
                storageReference = storage.getReference().child("images/" + job_url.getLastPathSegment());
                UploadTask uploadImage = storageReference.putFile(job_url);

                uploadImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Log.d("x,y", "onSuccess: " + x + " " + y);
                                Jobs jobs = new Jobs(job_name.getText().toString(), job_desc.getText().toString(), mUser.getEmail(), null, uri.toString(), "available", null, Double.parseDouble(job_pay.getText().toString()), job_time.getText().toString(), x, y);
                                jobs.setLocation(placeName);
                                /*if(longitude != 0 && latitude != 0)
                                {
                                    jobs.setLattitude(String.valueOf(latitude));
                                    jobs.setLongitude(String.valueOf(longitude));

                                }*/
                                CollectionReference colref = db.collection("jobs");
                                DocumentReference docref = colref.document();
                                docref.set(jobs).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Job posted successfully", Toast.LENGTH_SHORT).show();
                                                DocumentReference dr = db.collection("users").document(mUser.getEmail());
                                                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot doc = task.getResult();
                                                            if (doc.exists()) {
                                                                List<String> jobs = (List<String>) doc.get("postedJobs");
                                                                if (jobs == null) {
                                                                    jobs = new ArrayList<>();
                                                                }
                                                                String docId = docref.getId();
                                                                jobs.add(docId);

                                                                dr.update("postedJobs", jobs)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Log.d("success", "doc updated successfully ");
                                                                                MainActivity.getCurrentBio().getPostedJobs().add(docId);
                                                                                mainActivity.replaceFragment(new HomeFragment(),"");
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.d("fail", "error update doc ");
                                                                            }
                                                                        });


                                                            } else {
                                                                Log.d("nodoc", "onComplete: no such doc");
                                                            }
                                                        } else {
                                                            Log.d("fail", "onComplete: task fail");
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Job post error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });


                    }
                });
            }



            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.popBackstack("");
            }
        });







        return view;
    }

    @Override
    public void displayphoto(Uri URI) {
        job_url = URI;
        Glide.with(getActivity()).load(job_url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("glide", "onLoadFailed: " + e.getMessage());
                return false;

            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                parentActivity.replaceFragment(SignUpFragment.newInstance(URI),"signUpWithpic");
                Log.d("glide", "onsuccess: " );
                mainActivity.getSupportFragmentManager().popBackStackImmediate();
                //mainActivity.replaceFragment(new PostJob(URI),"");
                return false;
            }
        }).into(job_img);


    }

    @Override
    public void displayphotoFromgallery(Uri URI) {
        job_url = URI;




    }
}