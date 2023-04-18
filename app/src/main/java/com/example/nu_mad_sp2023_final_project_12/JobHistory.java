package com.example.nu_mad_sp2023_final_project_12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nu_mad_sp2023_final_project_12.Adapter.jobHistoryAdapter;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    FirebaseFirestore dataBase;

    private RecyclerView jobHistory;
    private jobHistoryAdapter jobhistoryAdapter;
    List<Jobs> jobList = new ArrayList<>();

    public JobHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static JobHistory newInstance(String param1, String param2) {
        JobHistory fragment = new JobHistory();
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
        Log.d("oncreate", "onCreate: oncreate called");
        dataBase.collection("jobs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> postedJobs = MainActivity.getCurrentBio().getPostedJobs();
                if(postedJobs == null || postedJobs.size() == 0) {
                    Toast.makeText(getContext(), "no job history to show", Toast.LENGTH_SHORT).show();
                }
                else if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        if(postedJobs.contains(documentSnapshot.getId())) {
                            Jobs job = documentSnapshot.toObject(Jobs.class);
                            jobList.add(job);

                        }
                    }
                    jobhistoryAdapter.setJobs(jobList);
                    jobhistoryAdapter.notifyDataSetChanged();

                }
                else{
                    Log.d("fail", "onComplete: job history failiure");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_list, container, false);

        jobHistory = view.findViewById(R.id.historyList);
        jobHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        jobhistoryAdapter = new jobHistoryAdapter(jobList,getContext());
        jobHistory.setAdapter(jobhistoryAdapter);



        return view;
    }
}