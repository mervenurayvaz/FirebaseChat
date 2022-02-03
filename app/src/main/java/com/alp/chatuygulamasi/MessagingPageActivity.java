package com.alp.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MessagingPageActivity extends AppCompatActivity {

    TextView userNameText;
    ImageView backImage,sendImage;
    RecyclerView recyclerView;
    EditText messageText;
    String userName,userId,profilImgUrl,time,date;
    MesageAdapter mesageAdapter;
    List<MessageModel> messageModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_page);

        userNameText = findViewById(R.id.messagePage_userName);
        backImage =findViewById(R.id.messagePage_backImage);
        sendImage = findViewById(R.id.messagePage_sendImage);
        recyclerView = findViewById(R.id.mesagePage_recyclerView);
        messageText = findViewById(R.id.messagePage_massageEditText);

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        if (getIntent() !=null){
            userName = getIntent().getStringExtra("userName");
            userId = getIntent().getStringExtra("userId");
            profilImgUrl = getIntent().getStringExtra("profilImgUrl");
        }

        userNameText.setText(userName);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadMessege();
        messageModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MessagingPageActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        mesageAdapter = new MesageAdapter(messageModels, userId);
        recyclerView.setAdapter(mesageAdapter);

    }

    private void loadMessege() {
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Message") .child(FirebaseAuth.getInstance().getUid()).child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(model);
                    System.out.println(model.getMessage());
                }
                mesageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageModels.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendMessage() {
        String message = messageText.getText().toString();
        if (message.equals("")){

        }else {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
           final String key = FirebaseDatabase.getInstance().getReference("Message") .child(firebaseUser.getUid()).child(userId).push().getKey();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message") .child(firebaseUser.getUid()).child(userId).child(key);

            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Message") .child(userId).child(firebaseUser.getUid()).child(key);
            HashMap<String,Object> map = new HashMap<>();
            map.put("message",message);
            map.put("sender",firebaseUser.getUid());
            databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    messageText.setText("");
                    databaseReference2.setValue(map);
                }
            });
        }


    }
}