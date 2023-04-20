package com.example.nu_mad_sp2023_final_project_12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.Adapter.ChatAdapter;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private UserData mParam1;

    FirebaseAuth mAuth;

    FirebaseFirestore db;

    FirebaseUser mUser;

    ImageView profilePic;
    TextView userName;
    RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    List<UserData> users;
    List<UserData> allUsers;

    Button logOut;
    UserData currentUser;



    public ChatFragment() {
        // Required empty public constructor
        users = new ArrayList<>();
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment chatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(UserData user) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        allUsers = new ArrayList<>();
        loadData();
        eventListner();

    }

    public void updateRecyclerView(List<UserData> allUsers){
        chatAdapter.setUserList(allUsers);
        chatAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        CollectionReference collRef = db.collection("users");

        collRef.document(MainActivity.getCurrentBio().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {   DocumentSnapshot documentSnapshot = task.getResult();
                    UserData  User = documentSnapshot.toObject(UserData.class);
                    MainActivity.setCurrentBio(User);
                    collRef.get()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("UserList", "Could Not load User list ");
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        allUsers.clear();
                                        List<String> friends = MainActivity.getCurrentBio().getFriendList();
                                        if(task.getResult().size()>1 && friends.size()>=1) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("UserList", document.getId() + " => " + document.getData());
                                                UserData user = document.toObject(UserData.class);
                                                if(friends.contains(user.getEmail()))
                                                {
                                                    allUsers.add(user);
                                                }
                                            }
                                            updateRecyclerView(allUsers);
                                        }
                                        else{
                                            Log.d("UserList", "No user to Chat with please pick a job.", task.getException());
                                        }
                                    } else {
                                        Log.w("UserList", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            }
        });


    }



 private void eventListner()
 {
     CollectionReference collRef = db.collection("users");
     db.collection("users").document(MainActivity.getCurrentBio().getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
         @Override
         public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
             UserData  User = value.toObject(UserData.class);
             MainActivity.setCurrentBio(User);
             collRef.get()
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Log.d("UserList", "Could Not load User list ");
                         }
                     })
                     .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
                             if (task.isSuccessful()) {
                                 List<String> friends = MainActivity.getCurrentBio().getFriendList();
                                 allUsers.clear();
                                 if(task.getResult().size()>1 && friends.size()>=1) {
                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                         Log.d("UserList", document.getId() + " => " + document.getData());
                                         UserData user = document.toObject(UserData.class);
                                         if(friends.contains(user.getEmail()))
                                         {
                                             allUsers.add(user);
                                         }
                                     }
                                     updateRecyclerView(allUsers);
                                 }
                                 else{
                                     Log.d("UserList", "No user to Chat with please pick a job.", task.getException());
                                 }
                             } else {
                                 Log.w("UserList", "Error getting documents.", task.getException());
                             }
                         }
                     });

         }
     });
 }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerId);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        chatAdapter = new ChatAdapter(users,getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(chatAdapter);

        return view;
    }
}