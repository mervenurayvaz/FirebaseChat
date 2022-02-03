package com.alp.chatuygulamasi;

import android.content.Intent;
import android.text.style.LineBackgroundSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FrendsAdapter extends RecyclerView.Adapter<FrendsAdapter.FriendsHolder> {

    List<UserList> userList;
    FragmentActivity context;

    public FrendsAdapter(List<UserList> userList, FragmentActivity context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_view,parent,false);

        return new FriendsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsHolder holder, int position) {
        UserList userList1 = userList.get(position);

        holder.addFriendsBtn.setText("send message");
        holder.userNameText.setText(userList1.getName());
        Picasso.get().load(userList1.getProfilImageUrl()).into(holder.porfilImage);
        holder.addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MessagingPageActivity.class);
                intent.putExtra("userName",userList1.getName());
                intent.putExtra("userId",userList1.getUserId());
                intent.putExtra("profilImgUrl",userList1.getProfilImageUrl());

              context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class FriendsHolder extends RecyclerView.ViewHolder {
        ShapeableImageView porfilImage;
        TextView userNameText;
        Button addFriendsBtn;
        public FriendsHolder(@NonNull View itemView) {
            super(itemView);
            porfilImage = itemView.findViewById(R.id.ımg_userProfilImage_homeView);
            userNameText = itemView.findViewById(R.id.txt_userName_homeView);
            addFriendsBtn = itemView.findViewById(R.id.btn_addToFriends_homeView);
        }
    }
}
