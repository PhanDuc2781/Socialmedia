package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.socialmediaapp.Adater.ChatAdapter;
import com.example.socialmediaapp.Model.Chat;
import com.example.socialmediaapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserActivity extends AppCompatActivity {
    String uId , myName;
    private ImageView back_ChatDetail , audio_Call , video_Call , send_Message , more;
    private CircleImageView img_UserChatDetail ;
    private EditText edt_Message ;
    private TextView nameUserChatDetail , user_Status , txt_UserOff;
    String Service_KEY = "AAAAPnJ2nU0:APA91bEHTYXIwQWU-HDohnk8OPlmDPSUDGWNhDSzsgakjDHPns-SKOwoaWqlzkcvxNw9BVaFIYrcMjeSHLJm5OEZoVWnJ1A7LnPaa_-y22lygIL-fS0oitsRqrDx9xg6BQH2yZWA3ntZ";
    String chatId = null ;
    FirebaseUser fuser  ;
    DatabaseReference reference ;

    private ChatAdapter chatAdapter ;
    private List<Chat> chats ;
    RecyclerView rec_ChatUser2 ;

    ValueEventListener seen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        uId = getIntent().getStringExtra("uId");


        //Init UI views
        back_ChatDetail = findViewById(R.id.back_ChatDetail);
        audio_Call = findViewById(R.id.audio_Call);
        video_Call = findViewById(R.id.video_Call);
        more = findViewById(R.id.more);
        send_Message = findViewById(R.id.send_Message);
        edt_Message = findViewById(R.id.edt_Message);
        img_UserChatDetail = findViewById(R.id.img_UserChatDetail);
        nameUserChatDetail = findViewById(R.id.nameUserChatDetail);
        user_Status = findViewById(R.id.user_Status);
        txt_UserOff = findViewById(R.id.txt_UserOff);


        //Init Rec chat
        rec_ChatUser2 = findViewById(R.id.rec_ChatUser2);
        rec_ChatUser2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rec_ChatUser2.setLayoutManager(linearLayoutManager);
        chats = new ArrayList<>();


        back_ChatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        audio_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatUserActivity.this , "Chức năng chưa có!" , Toast.LENGTH_SHORT).show();
            }
        });

        video_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatUserActivity.this , "Chức năng chưa có!" , Toast.LENGTH_SHORT).show();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        send_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edt_Message.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    edt_Message.setError("Nhập tin nhắn!");
                }else {
                    sendMessage();
                }

            }
        });

        img_UserChatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("SHARE" ,MODE_PRIVATE).edit();
                editor.putString("uId" , uId);
                editor.apply();
                startActivity(new Intent(ChatUserActivity.this , ProfileUserActivity.class));
                finish();

            }
        });

        nameUserChatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("SHARE" ,MODE_PRIVATE).edit();
                editor.putString("uId" , uId);
                editor.apply();
                startActivity(new Intent(ChatUserActivity.this , ProfileUserActivity.class));
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(uId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
              //Check status User

                if(user.getStatus().equals("online")){
                    user_Status.setVisibility(View.VISIBLE);
                    txt_UserOff.setVisibility(View.GONE);
                    user_Status.setText("Đang hoạt động");

                }if(user.getStatus().equals("offline")){
                    user_Status.setVisibility(View.GONE);
                    txt_UserOff.setVisibility(View.VISIBLE);
                    String timeOff = calculator(user.getCalculatorTimeOff());
                    txt_UserOff.setText("Hoạt động " + timeOff);

                }

                nameUserChatDetail.setText(user.getName());

                try {
                    Glide.with(getApplicationContext()).load(user.getImg_Profile()).into(img_UserChatDetail);
                }catch (Exception e){
                    e.printStackTrace();
                }

                readChat(fuser.getUid() , uId , user.getImg_Profile());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(uId);

    }

    private void seenMessage(String userId){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seen = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if(chat.getRecipient().equals(fuser.getUid()) && chat.getSenderId().equals(userId) ){
                        HashMap<String , Object> map = new HashMap<>();
                        map.put("isseen" , "true");
                        ds.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


    private void sendMessage() {
        String id = ""+System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a | dd-M-yyyy");
        final  String strDate = format.format(date);

        reference = FirebaseDatabase.getInstance().getReference("Chats");

//        chat = new Chat(id , fuser.getUid() , edt_Message.getText().toString().trim() ,uId , strDate , false , );
        HashMap<String , Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("senderId" , fuser.getUid());
        map.put("message" ,edt_Message.getText().toString().trim());
        map.put("recipient" , uId);
        map.put("timeSend" , strDate);
        map.put("isseen" , "false");
        map.put("emoji" , "");
        reference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                edt_Message.setText("");
            }
        });


        //Add user to chat fragment
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ChatLists").child(fuser.getUid()).child(uId);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    reference1.child("id").setValue(uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Add user to chat fragment
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatLists").child(uId).child(fuser.getUid());
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    reference2.child("id").setValue(fuser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void readChat(String myId , String uId , String img_Profile){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if(chat.getRecipient().equals(myId) && chat.getSenderId().equals(uId) ||
                            chat.getRecipient().equals(uId) && chat.getSenderId().equals(myId)){
                        chats.add(chat);
                    }
                    chatAdapter = new ChatAdapter(ChatUserActivity.this , chats , img_Profile);
                    rec_ChatUser2.setAdapter(chatAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("status" , status);
        reference.updateChildren(map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("calculatorTimeOff" , "");
        reference.updateChildren(map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        reference.removeEventListener(seen);
        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        final  String strDate = format.format(date);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("calculatorTimeOff" , strDate);
        reference.updateChildren(map);
    }

}