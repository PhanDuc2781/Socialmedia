package com.example.socialmediaapp.Adater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.ChatUserActivity;
import com.example.socialmediaapp.Fillter.SearchUserContact;
import com.example.socialmediaapp.Model.Chat;
import com.example.socialmediaapp.Model.User;
import com.example.socialmediaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder>  {
    private Context context;
    public List<User> list;
    String lastMassage ;
    Chat chat ;

    FirebaseUser fuser ;

    public UserChatAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chat , parent , false);
       fuser = FirebaseAuth.getInstance().getCurrentUser();
       return new UserChatAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserChatAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.name_UserChat.setText(user.getName());
        Picasso.get().load(user.getImg_Profile()).into(holder.img_UserChat1);
        holder.check_seen.setVisibility(View.GONE);

        //Check user Status
        if(user.getStatus().equals("online")){
            holder.img_Online.setVisibility(View.VISIBLE);
            holder.txt_Off.setVisibility(View.GONE);
        }if(user.getStatus().equals("offline")) {
            holder.img_Online.setVisibility(View.GONE);
            holder.txt_Off.setVisibility(View.VISIBLE);

            String time_Off = calculator(user.getCalculatorTimeOff());
            holder.txt_Off.setText(time_Off);
        }

        lastMassage(user.getuId() , holder.last_Chat , holder.check_seen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , ChatUserActivity.class);
                intent.putExtra("uId" , user.getuId());
                context.startActivity(intent);
            }
        });

    }

    private String calculator(String calculatorTimeOff) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        try {
            long time = sdf.parse(calculatorTimeOff).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_UserChat1 , img_Online;
        private TextView name_UserChat  , txt_Off , last_Chat;
        private ImageView check_seen ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_UserChat1 = itemView.findViewById(R.id.img_UserChat1);
            name_UserChat = itemView.findViewById(R.id.name_UserChat);
            img_Online = itemView.findViewById(R.id.img_Online);
            txt_Off = itemView.findViewById(R.id.txt_Off);
            last_Chat = itemView.findViewById(R.id.last_Chat);
            check_seen = itemView.findViewById(R.id.check_seen);
        }
    }

    //Check last Message

    private void lastMassage(String uId, TextView last_Message, ImageView check_seen){

        lastMassage = "default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    chat = ds.getValue(Chat.class);
                    if (chat.getRecipient().equals(fuser.getUid()) && chat.getSenderId().equals(uId) ||
                                 chat.getRecipient().equals(uId) && chat.getSenderId().equals(fuser.getUid())){
                        lastMassage = chat.getMessage();
                    }
                }

                switch (lastMassage){
                    case "default":
                    {
                        last_Message.setText("");
                        break;
                    }
                    default:
                        if (chat.getSenderId().equals(fuser.getUid())){
                            last_Message.setText("Bạn : " + lastMassage);
                        }else {
                            if(chat.getIsseen().equals("false")){
                                check_seen.setVisibility(View.VISIBLE);
                                last_Message.setTextColor(Color.BLACK);
                                last_Message.setText(lastMassage);
                            }else {
                                check_seen.setVisibility(View.GONE);
                                last_Message.setText(lastMassage);
                            }
                        }

                        break;
                }
                lastMassage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
