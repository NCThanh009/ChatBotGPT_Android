package com.example.chatgptapplication;

import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    List<Message> messageList;
    public MessageAdapter(List<Message> messList){
        this.messageList= messList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViewHolder myViewHolder= new MyViewHolder(chatView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message mess= messageList.get(position);
        if(mess.getSendBy().equals(Message.SENT_BY_ME)){
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.txt_rightChat.setText(mess.getMessage());
        }else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.txt_leftChat.setText(mess.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatView, rightChatView;
        TextView txt_leftChat, txt_rightChat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView= itemView.findViewById(R.id.vw_leftChat);
            rightChatView= itemView.findViewById(R.id.vw_rightChat);
            txt_leftChat= itemView.findViewById(R.id.txt_leftChat);
            txt_rightChat= itemView.findViewById(R.id.txt_rightChat);
        }
    }
}
