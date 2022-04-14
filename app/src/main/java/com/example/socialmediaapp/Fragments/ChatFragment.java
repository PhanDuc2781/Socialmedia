package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaapp.Adater.UserChatAdapter;
import com.example.socialmediaapp.Model.ChatList;
import com.example.socialmediaapp.Model.User;
import com.example.socialmediaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView rec_UserChat ;
    UserChatAdapter adapter ;
    List<User> list;
    FirebaseUser fuser;
    List<ChatList> userList;
    DatabaseReference reference ;



    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        //Init UI views

        rec_UserChat = view.findViewById(R.id.rec_UserChat);
        rec_UserChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rec_UserChat.setLayoutManager(linearLayoutManager);
        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatLists").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ChatList chatList = ds.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view ;
    }



    private void chatList() {
        list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                   User user = ds.getValue(User.class);
                   for (ChatList id:userList){
                       //If id = userId add to fragment
                       if(user.getuId().equals(id.getId())){
                           list.add(user);
                           Collections.reverse(list);
                       }
                   }
                }
                adapter = new UserChatAdapter(getActivity() , list);
                rec_UserChat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}