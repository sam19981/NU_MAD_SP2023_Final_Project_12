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
import com.example.nu_mad_sp2023_final_project_12.HistoryDetails;
import com.example.nu_mad_sp2023_final_project_12.JobDescription;
import com.example.nu_mad_sp2023_final_project_12.R;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;

import java.util.List;

public class jobHistoryAdapter extends RecyclerView.Adapter<jobHistoryAdapter.ViewHolder> {

    private List<Jobs> jobs;

    private Context context;



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
        View currentHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            j_img = itemView.findViewById(R.id.h_img);
            j_title = itemView.findViewById(R.id.h_name);
            j_status = itemView.findViewById(R.id.h_status);
            currentHistory = itemView;
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
                .load(R.drawable.dog)
                .into(holder.getJ_img());
        holder.getJ_title().setText(jobs.get(position).getName());
        holder.getJ_status().setText(jobs.get(position).getStatus());

        holder.currentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity)context;
                appCompatActivity.getSupportFragmentManager()
                        .beginTransaction().replace(R.id.rootLayoutId, HistoryDetails.newInstance("","",currJob)).addToBackStack(null).commit();
            }
        });




    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
