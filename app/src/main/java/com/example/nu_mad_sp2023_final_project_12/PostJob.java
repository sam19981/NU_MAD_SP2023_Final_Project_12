package com.example.nu_mad_sp2023_final_project_12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostJob#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostJob extends Fragment {

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
    private String job_url;
    private Button jobPost;

    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    private FirebaseFirestore db;

    public PostJob() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_post_job, container, false);
        job_name = view.findViewById(R.id.j_name);
        job_desc = view.findViewById(R.id.j_desc);
        job_pay = view.findViewById(R.id.j_pay);
        job_time = view.findViewById(R.id.j_dur);
        jobPost = view.findViewById(R.id.post_btn);

        jobPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Jobs jobs = new Jobs(job_name.getText().toString(), job_desc.getText().toString(), mUser.getEmail(), null, null, "available", null, Double.parseDouble(job_pay.getText().toString()),job_time.getText().toString());


                db.collection("jobs").document().set(jobs).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Job posted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Job post error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });







        return view;
    }
}