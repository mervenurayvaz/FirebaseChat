package com.alp.chatuygulamasi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }

    Bitmap selectedImage;
    Uri imageData;
    ShapeableImageView userProfilImage;
    TextView userMailText;
    EditText userNameText;

    Button updateButton,signOutBtn;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profil, container, false);
        userNameText = view.findViewById(R.id.profilFragment_userNameEditText);
        userProfilImage = view.findViewById(R.id.fragmentProfil_userProfilimageView);
        userMailText = view.findViewById(R.id.profilFragment_userMailTextView);

        updateButton =view.findViewById(R.id.profilFragment_updateProfilButton);
        signOutBtn = view.findViewById(R.id.profilFragment_signOutButton);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),SignInActivity.class));
                getActivity().finish();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();

            }
        });

        userProfilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });

        registerLauncher();
        userInfromation();
        return view;
    }

    private void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }
    }
    public void registerLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intentFromResult = result.getData();
                            if (intentFromResult != null) {
                                imageData = intentFromResult.getData();
                                try {

                                    if (Build.VERSION.SDK_INT >= 28) {
                                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageData);
                                        selectedImage = ImageDecoder.decodeBitmap(source);
                                        userProfilImage.setImageBitmap(selectedImage);

                                    } else {
                                        selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageData);
                                        userProfilImage.setImageBitmap(selectedImage);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                });


        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            //permission granted
                            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intentToGallery);

                        } else {
                            //permission denied
                            Toast.makeText(getActivity() , "Permisson needed!", Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }
    private void updateUserInformation() {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please Wait");

        if (imageData !=null){

            pd.show();
            UUID uuid = UUID.randomUUID();
            final String imageName =  uuid + ".jpg";

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilFiles/");
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = FirebaseStorage.getInstance().getReference("ProfilFiles/" ) ;
                    newReference.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            String nameText = userNameText.getText().toString();
                            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("profilImageUrl",downloadUrl);
                            map.put("name",nameText);
                            databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            });

                        }
                    });
                }
            });
        }
    }

    private void userInfromation() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   UserList userList =snapshot.getValue(UserList.class);
                   userNameText.setText(userList.getName());
                   userMailText.setText(firebaseUser.getEmail());
                   Picasso.get().load(userList.getProfilImageUrl()).into(userProfilImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}