package com.example.nu_mad_sp2023_final_project_12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobDescription extends Fragment {

    private MainActivity parentActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Jobs currJob;

    private TextView job_title;
    private Button getLocBtn;
    private ImageView job_img;
    private TextView job_desc;
    private TextView job_pay;
    private TextView job_time;

    private ImageView backbutton;
    private Button sndbtn;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private UserData otherUser;
    private static CollectionReference collRef;
    String  currentConversation="";




    public JobDescription() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobDescription.
     */
    // TODO: Rename and change types and number of parameters
    public static JobDescription newInstance(String param1, String param2, Jobs job) {
        JobDescription fragment = new JobDescription();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.currJob = job;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        parentActivity = (MainActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_description, container, false);
        job_title = view.findViewById(R.id.jd_title);
        job_img = view.findViewById(R.id.jd_img);
        job_desc = view.findViewById(R.id.jdesc);
        job_time = view.findViewById(R.id.jd_time);
        job_pay = view.findViewById(R.id.jd_wage);
        getLocBtn = view.findViewById(R.id.getloc);


        backbutton = view.findViewById(R.id.backbtn);
        sndbtn = view.findViewById(R.id.sendbtnId);

        job_title.setText(currJob.getName());
        Glide.with(getContext())
                        .load(currJob.getImgUrl())
                                .into(job_img);
        job_time.setText(currJob.getTime());
        job_pay.setText(String.valueOf(currJob.getWage()));
        job_desc.setText(currJob.getDescription());

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.replaceFragment(new HomeFragment(),"descriptiontojob");
                //parentActivity.popBackstack("");
            }
        });

        getLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoUriString = "google.navigation:q=" + currJob.getX() + "" + currJob.getY();
                Uri geoUri = Uri.parse(geoUriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });



        sndbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collRef = db.collection("conversations");
                List<String> userIds = Arrays.asList(MainActivity.getCurrentBio().getEmail(), currJob.getPostedBy());

                Collections.sort(userIds);

                String chatRecordID = Utils.generateUniqueID(userIds);

                CollectionReference collRef = db.collection("conversations");
                DocumentReference docref = db.collection("users").document(MainActivity.getCurrentBio().getEmail());
                docref.update("takenJobs",FieldValue.arrayUnion(currJob.getJob_id())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("id", "added to takenjobs" + currJob.getJob_id());
                        MainActivity.getCurrentBio().getTakenJobs().add(currJob.getJob_id());
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("taken", MainActivity.getCurrentBio().getEmail());
                        updates.put("status", "ongoing : " + MainActivity.getCurrentBio().getName());

                        db.collection("jobs").document(currJob.getJob_id()).update(updates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        collRef.document(chatRecordID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                                               @Override
                                                                                               public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                                                   CollectionReference documentReference = db.collection("conversations");
                                                                                                   CollectionReference userReference = db.collection("users");
                                                                                                   if (error != null) {
                                                                                                       Log.d("Conversation", "Error");
                                                                                                       return;

                                                                                                   }
                                                                                                   if (!value.exists()) {
                                                                                                       Log.d("Conversation", "Value Empty");

                                                                                                       documentReference.document(chatRecordID).set(new Conversation(userIds)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                           @Override
                                                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                                                               Log.d("Conversation", "created Conversation");
                                                                                                               currentConversation = chatRecordID;
                                                                                                               userReference.document(MainActivity.getCurrentBio().getEmail()).update("friendList", FieldValue.arrayUnion(currJob.getPostedBy())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                   @Override
                                                                                                                   public void onSuccess(Void unused) {
                                                                                                                       MainActivity.getCurrentBio().getFriendList().add(currJob.getPostedBy());
                                                                                                                       userReference.document(currJob.getPostedBy()).update("friendList", FieldValue.arrayUnion(MainActivity.getCurrentBio().getEmail())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                           @Override
                                                                                                                           public void onSuccess(Void unused) {
                                                                                                                               Log.d("friend","added to friendList");

                                                                                                                               db.collection("users").document(currJob.getPostedBy()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                   @Override
                                                                                                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                       if(task.isSuccessful()){
                                                                                                                                           otherUser = task.getResult().toObject(UserData.class);
                                                                                                                                           parentActivity.replaceFragment(new DisplayChatFragment(otherUser),"desctochat");
                                                                                                                                       }
                                                                                                                                   }
                                                                                                                               });
                                                                                                                           }
                                                                                                                       });

                                                                                                                   }
                                                                                                               });
                                                                                                           }
                                                                                                       });
                                                                                                   } else {
                                                                                                       Log.d("Conversation", "onEvent: Convo exists");
                                                                                                       db.collection("users").document(currJob.getPostedBy()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                           @Override
                                                                                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                               if(task.isSuccessful()){
                                                                                                                   otherUser = task.getResult().toObject(UserData.class);
                                                                                                                   parentActivity.replaceFragment(new DisplayChatFragment(otherUser),"desctochat");
                                                                                                               }
                                                                                                           }
                                                                                                       });
                                                                                                       //currentConversation = chatRecordID;



                                                                                                   }
                                                                                               }
                                                                                           }
                                        );
                                        Log.d("taken", "onComplete: added taken in job");
                                    }
                                });
                    }
                });





            }
        });
        return view;
    }

    public class Conversation {
        private List<String> userIds;

        public Conversation() {
            // Required empty constructor for Firestore
        }

        public Conversation(List<String> userIds) {
            this.userIds = userIds;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }
    }

}