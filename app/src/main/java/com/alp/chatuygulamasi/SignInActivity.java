package com.alp.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    TextView registrationPageTransitionText;
    EditText mailText ,passwordtext;
    Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        registrationPageTransitionText = findViewById(R.id.txt_signIn_signUpTextView);

        registrationPageTransitionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,RegisterActivity.class));
            }
        });
         
        mailText = findViewById(R.id.edt_sigIn_userMail_EditText);
        passwordtext = findViewById(R.id.edt_signIn_userPassword_EditText);
        signInBtn = findViewById(R.id.btn_signIn_signInButton);
        
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignInActivity.this,HomePageActivity.class));
            finish();
        }
    }

    private void signIn() {
        String mail = mailText.getText().toString();
        String password = passwordtext.getText().toString();
        
        if(mail.equals("")){
            mailText.setError("Please do not leave blank.");
        }else if (password.equals("")){
            passwordtext.setError("Please do not leave blank.");
            
        }else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignInActivity.this, "You have successfully logged in.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this,HomePageActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    passwordtext.setError("Wrong Password");
                }
            });
            
        }
    }
}