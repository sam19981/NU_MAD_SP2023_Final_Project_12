package com.example.nu_mad_sp2023_final_project_12.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.HistoryDetails;
import com.example.nu_mad_sp2023_final_project_12.JobDescription;
import com.example.nu_mad_sp2023_final_project_12.MainActivity;
import com.example.nu_mad_sp2023_final_project_12.R;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class jobHistoryAdapter extends RecyclerView.Adapter<jobHistoryAdapter.ViewHolder> {

    private List<Jobs> jobs;

    public void setJobs(List<Jobs> jobs) {
        this.jobs = jobs;
    }

    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    public jobHistoryAdapter(List<Jobs> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;

    }

    @NonNull
    @Override
    public jobHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history,parent, false);
        return new jobHistoryAdapter.ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView j_img;
        private final TextView j_title;
        private final TextView j_status;



        private final ImageView completed;
        View currentHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            j_img = itemView.findViewById(R.id.h_img);
            j_title = itemView.findViewById(R.id.h_name);
            j_status = itemView.findViewById(R.id.h_status);
            completed = itemView.findViewById(R.id.mark_as_cmp);
            currentHistory = itemView;
        }
        public ImageView getCompleted() {
            return completed;
        }

        public ImageView getJ_img() {
            return j_img;
        }

        public TextView getJ_title() {
            return j_title;
        }

        public TextView getJ_status() {
            return j_status;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull jobHistoryAdapter.ViewHolder holder, int position) {

        Jobs currJob = jobs.get(position);
        Glide.with(context)
                .load(currJob.getImgUrl())
                .into(holder.getJ_img());
        holder.getJ_title().setText(jobs.get(position).getName());
        holder.getJ_status().setText(jobs.get(position).getStatus());
        if(currJob.getPostedBy().equals(MainActivity.getCurrentBio().getEmail()) && !currJob.getStatus().contains("completed")){
            holder.getCompleted().setVisibility(View.VISIBLE);
            holder.getCompleted().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String new_status = "";

                    if(currJob.getStatus().contains(":")){
                        String[] arr = currJob.getStatus().split(":");
                        arr[0] = "completed : ";
                        new_status = arr[0] + arr[1];
                    }
                    else{
                        new_status = "completed";
                    }
                    db.collection("jobs").document(currJob.getJob_id()).update("status",new_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("completed job","completed");
                            holder.getCompleted().setVisibility(View.INVISIBLE);
                        }
                    });

                }
            });
        }

//        holder.currentHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppCompatActivity appCompatActivity = (AppCompatActivity)context;
//                appCompatActivity.getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.rootLayoutId, HistoryDetails.newInstance("","",currJob)).addToBackStack(null).commit();
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
