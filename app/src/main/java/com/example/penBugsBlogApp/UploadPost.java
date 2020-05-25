package com.example.penBugsBlogApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.penBugsBlogApp.Models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPost extends AppCompatActivity {

    private static final int REQUESCODE = 1;
    private ImageButton uploadImageBtn;
    private Button uploadBtn, cricketPost;
    private EditText titleText, descriptionText;
    private Spinner uploadSpinner;
    private int PReqCode = 1;
    private Uri pickedImgUri = null;
    private ProgressBar loadingSpinner;
    FirebaseUser currentUser ;
    FirebaseAuth  mAuth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        uploadImageBtn = findViewById(R.id.uploadImageButton);
        uploadBtn = findViewById(R.id.uploadPostBtn);
        titleText = findViewById(R.id.uploadTextTitle);
        descriptionText = findViewById(R.id.uploadTextDescription);
        uploadSpinner = findViewById(R.id.uploadSpinner);
        loadingSpinner = findViewById(R.id.uploadProgressBar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UploadPost.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        uploadSpinner.setAdapter(adapter);

        loadingSpinner.setVisibility(View.INVISIBLE);

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    requestPermissionsForGallery();
                } else {
                    openGallery();
                }
            }
        });

        uploadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                switch (category){

                    case "Cricket" :
                        showMessage("Cricket");
                        break;
                    case "Editorial News":
                        showMessage("Editorial");

                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadBtn.setVisibility(View.INVISIBLE);
                loadingSpinner.setVisibility(View.VISIBLE);

                if (!titleText.getText().toString().isEmpty() && !descriptionText.getText().toString().isEmpty() && pickedImgUri != null){

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(titleText.getText().toString(),
                                            descriptionText.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid()
                                           );

                                        addPost(post);

                                    // Add post to firebase database


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture
                                    showMessage(e.getMessage());
                                    loadingSpinner.setVisibility(View.INVISIBLE);
                                    uploadBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                } else {
                    showMessage("Please fill the Field and Upload the Post");
                    loadingSpinner.setVisibility(View.INVISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                }
            }
        });



    }


    public void pushDataInFirebase(Post post){
        String key = myRef.getKey();
        post.setPostKey(key);
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added Successfully");
                loadingSpinner.setVisibility(View.INVISIBLE);
                uploadBtn.setVisibility(View.VISIBLE);

            }
        });
    }


    //Post added to the Firebase
    // if else condition is used to set or push the data to firebase and retrive them using the reference name
    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (uploadSpinner.getSelectedItem().equals("Cricket")){
            myRef = database.getReference("Posts").push();
            pushDataInFirebase(post);
        } else if (uploadSpinner.getSelectedItem().equals("Editorial News")){
            DatabaseReference myRef = database.getReference("Edit").push();
            pushDataInFirebase(post);
        }

        //Unique ID is set for the post

    }


    private void showMessage(String message) {
        Toast.makeText(UploadPost.this, message, Toast.LENGTH_LONG).show();
    }

    private void requestPermissionsForGallery() {
        if (ContextCompat.checkSelfPermission(UploadPost.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadPost.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(UploadPost.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(UploadPost.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();
    }

    private void    openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();;
            uploadImageBtn.setImageURI(pickedImgUri);


        }


    }
}
