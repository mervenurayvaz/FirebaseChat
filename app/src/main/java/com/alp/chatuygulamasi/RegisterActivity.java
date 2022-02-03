package com.alp.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
 EditText mailText,passwordText,nameText;
 CheckBox praPoliCheckBox;
 TextView regPageToLogPageText;
 Button sıgnUpButton;
 FirebaseAuth firebaseAuth;
 FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mailText = findViewById(R.id.edt_sigUp_userMail_EditText);
        passwordText =findViewById(R.id.edt_signUp_userPassword_EditText);
        nameText = findViewById(R.id.edt_sigUp_userName_EditText);

        praPoliCheckBox = findViewById(R.id.chx_signUp_privacyPollicy);
        regPageToLogPageText = findViewById(R.id.txt_signUp_signUpTextView);
        sıgnUpButton = findViewById(R.id.btn_signUp_signUpButton);

        regPageToLogPageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
            }
        });

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sıgnUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });


    }

    private void userRegister() {

        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();
        String name = nameText.getText().toString();

        if(praPoliCheckBox.isChecked()) {
            if (mail.equals("")) {
                mailText.setError("Please do not leave blank.");
            } else if (password.equals("")) {
                passwordText.setError("Please do not leave blank.");
            } else if (name.equals("")){
                nameText.setError("Please do not leave blank.");
            } else {
                firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        DatabaseReference databaseReference = database.getReference("Users").child(firebaseUser.getUid());
                        HashMap<String,Object> hashMap  = new HashMap<>();
                        hashMap.put("name",name);

                        hashMap.put("userId",firebaseUser.getUid());
                        hashMap.put("profilImageUrl","https://firebasestorage.googleapis.com/v0/b/chatuygulamasi-9c111.appspot.com/o/ProfilFiles%2FplaceHolder.jpg?alt=media&token=e19f50a3-c2c5-4a73-a2c8-faf9b400c8ea");
                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterActivity.this, "You have successfully registered.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,HomePageActivity.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else {
            praPoliCheckBox.setError("");
            Toast.makeText(RegisterActivity.this,"Please accept the confdentiality agreement.",Toast.LENGTH_LONG).show();
        }
    }
}