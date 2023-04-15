package com.example.nu_mad_sp2023_final_project_12;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.models.Jobs;

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
    private ImageView job_img;
    private TextView job_desc;
    private TextView job_pay;
    private TextView job_time;

    private ImageView backbutton;

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

        backbutton = view.findViewById(R.id.backbtn);

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









        return view;
    }
}