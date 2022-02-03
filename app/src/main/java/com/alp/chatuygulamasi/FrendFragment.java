package com.alp.chatuygulamasi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FrendFragment extends Fragment {

    FrendsAdapter frendsAdapter;
    RecyclerView recyclerView;
    List<UserList> lists;
    List<String> trackingList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frend, container, false);
        recyclerView = view.findViewById(R.id.frendFragment_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        lists = new ArrayList<>();
        frendsAdapter = new FrendsAdapter(lists, getActivity());
        recyclerView.setAdapter(frendsAdapter);
         trackingList = new ArrayList<>();
         trackingControl();


        return view;
    }
    private void trackingControl (){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid()) ;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trackingList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    trackingList.add(snapshot1.getKey());
                }

                userInformation();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void userInformation (  ) {





            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lists.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        UserList userList = dataSnapshot.getValue(UserList.class);

                        for (String id : trackingList){


                            if (id.equals(userList.getUserId())){
                                lists.add(userList);
                            }
                        }
                    }




                    frendsAdapter.notifyDataSetChanged();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





    }
}