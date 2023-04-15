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


import com.example.nu_mad_sp2023_final_project_12.R;
import com.example.nu_mad_sp2023_final_project_12.models.UserData;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


   List<UserData> userList;

   Context context;

    public ChatAdapter() {
    }

    public ChatAdapter(List<UserData> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public void setUserList(List<UserData> userList) {
        this.userList = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName;
        TextView lastMessage;
        View currentUser;

        public ImageView getProfilePic() {
            return profilePic;
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getLastMessage() {
            return lastMessage;
        }

        public View getCurrentUser() {
            return currentUser;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.chatUserimg);
            userName = itemView.findViewById(R.id.chatUserId);
            lastMessage = itemView.findViewById(R.id.lastChatId);
            currentUser = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.chat,parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserData userHere = userList.get(position);


        holder.getUserName().setText(userHere.getName());

        holder.getCurrentUser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
