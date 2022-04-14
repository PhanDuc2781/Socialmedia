package com.example.socialmediaapp.Adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.Model.User;
import com.example.socialmediaapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserContactAdapter extends RecyclerView.Adapter<UserContactAdapter.ViewHolder> implements Filterable {

    private Context context ;
    public List<User> list ;

    @NonNull
    @Override
    public UserContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_contact , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserContactAdapter.ViewHolder holder, int position) {
        User user = list.get(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_UserChat1  , img_Online ;
        private TextView txt_Off ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_UserChat1 = itemView.findViewById(R.id.img_UserChat1);
            img_Online = itemView.findViewById(R.id.img_Online);
            txt_Off = itemView.findViewById(R.id.txt_Off);
        }
    }
}
