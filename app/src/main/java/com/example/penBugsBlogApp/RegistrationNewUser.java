package com.example.penBugsBlogApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationNewUser extends AppCompatActivity {

    // Declare the variable for edit text and button in the activity .xml

    private EditText userName, userEmail, userPassword, userConfirmPassword;
    private Button registerButton;
    private ProgressBar loadingProcess;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_new_user);

        //Find the edit text and button id using findviewByID

        userName = findViewById(R.id.registerNewUser);
        userEmail = findViewById(R.id.registerNewEmailID);
        userPassword = findViewById(R.id.registerNewPassword);
        userConfirmPassword = findViewById(R.id.registerNewConformPassword);

        // Registration Button
        registerButton = findViewById(R.id.registerButton);

        //FireBase
        mAuth = FirebaseAuth.getInstance();

        //Set loading spinner to invisible
        loadingProcess = findViewById(R.id.registrationProgressBar);
        loadingProcess.setVisibility(View.INVISIBLE);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setVisibility(View.INVISIBLE);
                loadingProcess.setVisibility(View.VISIBLE);

                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmPassword = userConfirmPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)){
                    showMessage("Please Enter Valid Details");
                    loadingProcess.setVisibility(View.INVISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                } else{
                    createUserAccount(name, email, password); //user to create a user a new account
                }
            }
        });

    }

    private void createUserAccount(String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            showMessage("Account Created Successfully");
                            updateUI(); // user will logged in moved to Post recycler activity

                        } else {
                            showMessage("Account Creation Failed");
                            registerButton.setVisibility(View.VISIBLE);
                            loadingProcess.setVisibility(View.INVISIBLE);

                        }

                    }
                });


    }

    private void updateUI() {
        Intent postActivity = new Intent(getApplicationContext(), BottomBarNewFeeds.class);
        startActivity(postActivity);
        finish();
    }

    //Used to show a message to Enter a valid details in Toast message
    private void showMessage(String message) {
        Toast.makeText(RegistrationNewUser.this, message, Toast.LENGTH_LONG).show();
    }
}
