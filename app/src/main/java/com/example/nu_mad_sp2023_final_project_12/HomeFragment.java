package com.example.nu_mad_sp2023_final_project_12;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nu_mad_sp2023_final_project_12.Adapter.ViewPagerAdapter;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private ImageButton pBtn;

    private TextView name;
    private CircleImageView profilepic;
    private ImageView changeProfilePic;
    private UserData currentUserBio;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        currentUserBio = MainActivity.getCurrentBio();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        name = view.findViewById(R.id.id_fullName_TextView);
        profilepic = view.findViewById(R.id.profilePicId);
        changeProfilePic = view.findViewById(R.id.changeProfilePicId);
        name.setText(currentUserBio.getName());
//        Glide.with(getContext()).load(currentUserBio.getProfilepicture()).into(profilepic);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment());
        fragments.add(new Fragment());
        fragments.add(new Fragment());
        viewPager = (ViewPager2) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),getLifecycle(), fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the tab title based on the position of the current page
            switch (position) {
                case 0:
                    tab.setText("Jobs");
                    break;
                case 1:
                    tab.setText("History");
                    break;
                case 2:
                    tab.setText("Chats");
                    break;
            }
        }).attach();
        pBtn = view.findViewById(R.id.postbtn);
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootLayoutId, new PostJob(),"hometopost").addToBackStack(null).commit();
            }
        });

        return view;
    }
}