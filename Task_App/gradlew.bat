package com.example.inclass12_group7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CreateContact extends AppCompatActivity {
    ImageView iv_createContactImage;
    EditText et_name,et_email,et_phone;
    Button bt_submit;
    FirebaseStorage storage;
    StorageReference storageRef;

    Bitmap bitmap;
    Boolean isTakenPhoto = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        iv_createContactImage=findViewById(R.id.iv_createContactImage);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_phone=findViewById(R.id.et_phone);
        bt_submit=findViewById(R.id.bt_submit);

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTakenPhoto){
                    final StorageReference imageStorageRef = storageRef.child("images/image.jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = imageStorageRef.putBytes(data);

                    Task<Uri> urlTask = 