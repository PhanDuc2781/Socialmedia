package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.socialmediaapp.Fragments.ChatFragment;
import com.example.socialmediaapp.Fragments.UserChatFragment;
import com.example.socialmediaapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class ChatActivity extends AppCompatActivity {
    private SmoothBottomBar bottomNavChat  ;
    private CircleImageView img_UserCurrentChat ;
    private ImageView back_Chat ;


    FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        img_UserCurrentChat = findViewById(R.id.img_UserCurrentChat);
        back_Chat = findViewById(R.id.back_Chat);



        back_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Load img_User profile
        loadImagUserProfile();

        bottomNavChat = findViewById(R.id.bottomNavChat);


        FragmentTransaction chat = getSupportFragmentManager().beginTransaction();
        chat.replace(R.id.container_Chat, new ChatFragment());
        chat.commit();


        bottomNavChat.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction  transaction = getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.container_Chat , new ChatFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.container_Chat , new UserChatFragment());
                        break;
                }
                transaction.commit();
                return false;
            }
        });
    }

    private void loadImagUserProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);

                try {
                    Glide.with(getApplicationContext()).load(user1.getImg_Profile()).into(img_UserCurrentChat);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("status" , status);
        reference.updateChildren(map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("calculatorTimeOff" , "");
        reference.updateChildren(map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");


        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        final  String strDate = format.format(date);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String , Object> map = new HashMap<>();
        map.put("calculatorTimeOff" , strDate);
        reference.updateChildren(map);
    }


}