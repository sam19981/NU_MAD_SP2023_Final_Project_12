package com.example.nu_mad_sp2023_final_project_12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nu_mad_sp2023_final_project_12.Adapter.jobListAdapter;

import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.Job;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore dataBase;

    private RecyclerView jobsDisplay;

    private jobListAdapter jobAdapter;
    List<Jobs> jobList = new ArrayList<>();

    CollectionReference collectionReference;

    public JobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobsFragment newInstance(String param1, String param2) {
        JobsFragment fragment = new JobsFragment();
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
        dataBase = FirebaseFirestore.getInstance();


        dataBase.collection("jobs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Jobs job =documentSnapshot.toObject(Jobs.class);
                        if(!job.getPostedBy().equals(MainActivity.getCurrentBio().getEmail())) {
                            jobList.add(job);
                        }

                    }
                    jobAdapter.setJobs(jobList);
                    jobAdapter.notifyDataSetChanged();
                    addeventlistener();

                }
                else{
                    Log.d("unsuccess", "job task failiure");
                }

            }
        });

    }

    public void addeventlistener(){
        collectionReference =  dataBase.collection("jobs");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("jobsfragment", "onEvent: "+ error.getMessage());
                }else{
                    jobList.clear();
                    for(DocumentSnapshot documentSnapshot: value.getDocuments()){
                            Jobs job =documentSnapshot.toObject(Jobs.class);
                            if(!job.getPostedBy().equals(MainActivity.getCurrentBio().getEmail())) {
                                jobList.add(job);
                            }
                        jobAdapter.setJobs(jobList);
                        jobAdapter.notifyDataSetChanged();

                    }

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        jobsDisplay = view.findViewById(R.id.jobsList);
        jobsDisplay.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new jobListAdapter(jobList,getContext());
        jobsDisplay.setAdapter(jobAdapter);






        return view;
    }
}