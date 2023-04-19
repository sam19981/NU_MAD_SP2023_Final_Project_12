package com.example.nu_mad_sp2023_final_project_12.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.nu_mad_sp2023_final_project_12.R;
import com.example.nu_mad_sp2023_final_project_12.models.MessageData;

import java.util.ArrayList;
import java.util.List;

public class DisplayChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_USER_MESSAGE = 0;

    final int VIEW_TYPE_OTHER_PERSON_MESSAGE =1;

    List<MessageData> conversation;
    String toUserEmail;
    String toUser;

    public DisplayChatAdapter() {
    }

    public DisplayChatAdapter(List<MessageData> conversation) {
        this.conversation = conversation;
    }

    public DisplayChatAdapter(String touserEmail,String toUser) {
        this.toUserEmail = touserEmail;
        this.toUser = toUser;
        conversation = new ArrayList<>();
    }

    public List<MessageData> getConversation() {
        return conversation;
    }

    public void setConversation(List<MessageData> conversation) {
        this.conversation = conversation;
    }

    public class UserMessageViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView messageSender;
        TextView message;
        ImageView sendImg;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.chatContainer);
            messageSender = itemView.findViewById(R.id.msgUserNameId);
            message = itemView.findViewById(R.id.chatmessageId);
            sendImg = itemView.findViewById(R.id.otherSendImgId);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getMessageSender() {
            return messageSender;
        }

        public TextView getMessage() {
            return message;
        }

        public void bind(MessageData message,String user) {
            messageSender.setText(user);
            if(message.getMessageType().equals("Image"))
            {   sendImg.setVisibility(View.VISIBLE);
                Glide.with(cardView.getContext()).load(message.getMessage()).into(sendImg);
                this.message.setVisibility(View.INVISIBLE);
            }
            else {
                this.message.setVisibility(View.VISIBLE);
                this.message.setText(message.getMessage());
            }
        }
    }

    public class OtherPersonMessageViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView messageSender;
        TextView message;

        ImageView sendImage;

        public OtherPersonMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.otherChatContainer);
            messageSender = itemView.findViewById(R.id.msgotherUserNameId);
            message = itemView.findViewById(R.id.chatothermessageId);
            sendImage = itemView.findViewById(R.id.sendImgId);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getMessageSender() {
            return messageSender;
        }

        public TextView getMessage() {
            return message;
        }

        public void bind(MessageData message,String user) {
            messageSender.setText(user);
            if(message.getMessageType().equals("Image"))
            {   sendImage.setVisibility(View.VISIBLE);
                Glide.with(cardView.getContext()).load(message.getMessage()).into(sendImage);
                this.message.setVisibility(View.INVISIBLE);
            }
            else {
                this.message.setVisibility(View.VISIBLE);
                this.message.setText(message.getMessage());
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatmessage, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.otheruserchat, parent, false);
            return new OtherPersonMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageData msg = conversation.get(position);
        Log.d("positon", "getItemViewType: "+position);
        if (msg.getUserId().equals(toUserEmail)) {
            OtherPersonMessageViewHolder otherPersonMessageViewHolder = (OtherPersonMessageViewHolder) holder;
            otherPersonMessageViewHolder.bind(msg,toUser);
        } else {

            UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) holder;
            userMessageViewHolder.bind(msg,"you");
        }

    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageData m =conversation.get(position);
        if (m.getUserId().equals(toUserEmail)) {
            return VIEW_TYPE_OTHER_PERSON_MESSAGE;
        } else {
            return VIEW_TYPE_USER_MESSAGE;
        }
    }

}
