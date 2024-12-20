package com.example.chat_app.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.chat.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder > {
private List<MessagesList> messagesLists;
private final Context context;



    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2 = messagesLists.get(position);

        if (list2.getProfilePic() != null && !list2.getProfilePic().isEmpty()){
            Picasso.get().load(list2.getProfilePic()).into(holder.profile_image);
        }
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());
        if (list2.getUnseenMessages() == 0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
            holder.lastMessage.setTextColor(Color.parseColor(String.valueOf(context.getResources().getColor(R.color.theme_color_80))));
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("mobile",list2.getPhone());
                intent.putExtra("name",list2.getName());
                intent.putExtra("profile_pic",list2.getProfilePic());
                intent.putExtra("chat_key",list2.getChatKey());
                context.startActivity(intent);
            }
        });


    }



    public void updateData(List<MessagesList> messagesLists){
        this.messagesLists = messagesLists;
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        // il permet de comter le nombre des messages

        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profile_image;
        private TextView name,unseenMessages,lastMessage;
        private LinearLayout rootLayout ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.name);
            unseenMessages=itemView.findViewById(R.id.unseenMessages);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }
}
