package com.example.nu_mad_sp2023_final_project_12.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.JobDescription;
import com.example.nu_mad_sp2023_final_project_12.R;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;

import java.util.List;

public class jobListAdapter extends RecyclerView.Adapter<jobListAdapter.ViewHolder> {

    private List<Jobs> jobs;

    private Context context;

    public jobListAdapter(List<Jobs> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView j_img;
        private final TextView j_title;
        private final TextView j_pay;
        private final TextView j_loc;

        View currentJob;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            j_img = itemView.findViewById(R.id.h_img);
            j_title = itemView.findViewById(R.id.h_name);
            j_pay = itemView.findViewById(R.id.job_pay);
            j_loc = itemView.findViewById(R.id.h_status);
            currentJob = itemView;

        }

        public ImageView getJ_img() {
            return j_img;
        }

        public TextView getJ_title() {
            return j_title;
        }

        public TextView getJ_pay() {
            return j_pay;
        }

        public TextView getJ_loc() {
            return j_loc;
        }
    }


    @NonNull
    @Override
    public jobListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jobListAdapter.ViewHolder holder, int position) {
        Jobs currJob = jobs.get(position);
        Glide.with(context)
                        .load(R.drawable.dog)
                                .into(holder.getJ_img());
        holder.getJ_pay().setText(String.valueOf(jobs.get(position).getWage()));
        holder.getJ_title().setText(jobs.get(position).getName());
        holder.getJ_loc().setText(jobs.get(position).getLocation());
        holder.currentJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity)context;
                appCompatActivity.getSupportFragmentManager()
                        .beginTransaction().replace(R.id.rootLayoutId, JobDescription.newInstance("","",currJob)).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
