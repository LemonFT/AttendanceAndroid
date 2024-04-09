package com.example.certainlyhereiamfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.certainlyhereiamfinal.Global;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.User;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.SigninViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView avatar;
    private EditText edt_email, edt_identifier, edt_name;
    private String avatarUrl = "";
    private Button btn_update_profile;
    StorageReference storageRef;

    private SigninViewModel signinViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseApp.initializeApp(this);
        storageRef = storage.getReference();
        signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        User user = new Gson().fromJson(DataLocalManager.getUser(), User.class);
        avatar = findViewById(R.id.avatar_pf);
        edt_email = findViewById(R.id.email_pf);
        edt_identifier = findViewById(R.id.identifier_pf);
        edt_name = findViewById(R.id.name_pf);
        btn_update_profile = findViewById(R.id.btn_update_profile);
        edt_email.setText(user.getEmail());
        edt_identifier.setText(user.getIdentifier());
        edt_name.setText(user.getFullname());

        if (user.getAvatar() != null && !user.getAvatar().equals("")) {
            Picasso.get().load(user.getAvatar()).into(avatar);
            avatarUrl = user.getAvatar();
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avatar = avatarUrl;
                String emailStr = edt_email.getText().toString();
                String identifierStr = edt_identifier.getText().toString();
                String nameStr = edt_name.getText().toString();
                User userUpdate = new User(user.getId(), emailStr, nameStr, identifierStr, user.getPassword(), avatar);
                signinViewModel.updateProfile(userUpdate).observe((LifecycleOwner) ProfileActivity.this, data -> {
                    if(data != null){
                        Global.showAlertSuccess("Update successful", ProfileActivity.this);
                        user.setAvatar(avatar);
                        user.setEmail(emailStr);
                        user.setFullname(nameStr);
                        user.setIdentifier(identifierStr);
                        DataLocalManager.setUser(new Gson().toJson(user));
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Global.showAlert("Email already exist", ProfileActivity.this);
                    }
                });
            }
        });
    }

    private void uploadImage(Uri imageUri) {
        StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        avatarUrl = imageUrl;
                        Picasso.get().load(imageUrl).into(avatar);
                    });
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(ProfileActivity.this, "Upload failed: " + exception, Toast.LENGTH_SHORT).show();
                });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }
}
