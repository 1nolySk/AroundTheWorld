package com.ask.atw.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ask.atw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Button signup;
    TextView textsignin;
    EditText email,pass;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private static final String mypref = "userdetails.conf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.signupmain);
        textsignin = findViewById(R.id.logintext);
        email = findViewById(R.id.userEmail);
        pass= findViewById(R.id.userPassword);
        mAuth = FirebaseAuth.getInstance();
        prefs  = getSharedPreferences(mypref,MODE_PRIVATE);
        editor = prefs.edit();
        if(Build.VERSION.SDK_INT <= 21){
            progressDialog = new ProgressDialog(this, android.app.AlertDialog.THEME_HOLO_DARK);
        }
        else{
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.setTitle("Sign Up");
        textsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailtext = email.getText().toString().trim();
                final String passwordtext = pass.getText().toString().trim();

                if (TextUtils.isEmpty(emailtext)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordtext)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordtext.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(emailtext,passwordtext)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    mAuth.signInWithEmailAndPassword(emailtext,passwordtext)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    editor.putString("email",emailtext);
                                                    editor.putString("password",passwordtext);
                                                    editor.putString("detailset","no");
                                                    editor.putString("logintype","email");
                                                    editor.apply();
                                                    startActivity(new Intent(SignUpActivity.this, SetupAccount.class));
                                                    finish();
                                                }
                                            });

                                }
                            }
                        });
            }
        });
    }
}
