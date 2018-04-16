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
import android.widget.Toast;

import com.ask.atw.R;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText email,pass;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    private static final String myprefs="userdetails.conf";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginmain);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.userEmaillogin);
        pass = findViewById(R.id.userPasswordlogin);
        prefs = getSharedPreferences(myprefs,MODE_PRIVATE);
        if(Build.VERSION.SDK_INT <= 21){
            progressDialog = new ProgressDialog(this, android.app.AlertDialog.THEME_HOLO_DARK);
        }
        else{
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("Loginng In");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaillogin = email.getText().toString();
                String password = pass.getText().toString();

                if (TextUtils.isEmpty(emaillogin)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(emaillogin,password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"No account with this email-id exists !",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    progressDialog.dismiss();
                                    editor = prefs.edit();
                                    editor.putString("logintype", "email");
                                    editor.apply();
                                    if (prefs.getString("detailset", "").equalsIgnoreCase("no") || prefs.getString("detailset", "").equalsIgnoreCase("")) {
                                        startActivity(new Intent(LoginActivity.this, SetupAccount.class));
                                        finish();
                                    } else if (prefs.getString("detailset", "").equalsIgnoreCase("yes")) {
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
