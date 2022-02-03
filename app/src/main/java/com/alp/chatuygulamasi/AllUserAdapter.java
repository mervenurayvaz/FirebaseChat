package com.alp.chatuygulamasi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.HomeHolder> {

    List<UserList> userList;
    FragmentActivity context;

    public AllUserAdapter(List<UserList> userList, FragmentActivity context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_view,parent,false);

        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
      final UserList list = userList.get(position);

      holder.userNameText.setText(list.getName());
      trackingControl(list.getUserId(),holder.addFriendsBtn);
          FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Picasso.get().load(list.getProfilImageUrl()).into(holder.porfilImage);
      if (firebaseUser.getUid().equals(list.getUserId())){
          holder.addFriendsBtn.setVisibility(View.GONE);
      }
      holder.addFriendsBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               if (holder.addFriendsBtn.getText().equals("Add to friends")){
                   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                   FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid()) .child(list.getUserId()).setValue(true);
                   FirebaseDatabase.getInstance().getReference("Friends").child(list.getUserId()) .child(firebaseUser.getUid()).setValue(true);

               }

               else {
                   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                   FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid()) .child(list.getUserId()).removeValue();
                   FirebaseDatabase.getInstance().getReference("Friends").child(list.getUserId()). child(firebaseUser.getUid()).removeValue();

               }
          }
      });


    }


    @Override
    public int getItemCount() {
        return userList.size();
    }



    class HomeHolder extends RecyclerView.ViewHolder {
        ShapeableImageView porfilImage;
        TextView userNameText;
        Button addFriendsBtn;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            porfilImage = itemView.findViewById(R.id.ımg_userProfilImage_homeView);
            userNameText = itemView.findViewById(R.id.txt_userName_homeView);
            addFriendsBtn = itemView.findViewById(R.id.btn_addToFriends_homeView);
        }
    }

    private void trackingControl (String userId ,Button button){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid()) ;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()){
                    button.setText("Your Friend");
                }
                else {
                    button.setText("Add to friends");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
