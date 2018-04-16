package com.ask.atw.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ask.atw.R;
import com.google.firebase.auth.FirebaseAuth;

public class SetupAccount extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb;
    Button submit;
    EditText fname,lname;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private static final String mypref = "userdetails.conf";
    String gender,firstname,lastname;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        rg=findViewById(R.id.genderselect);
        prefs = getSharedPreferences(mypref,MODE_PRIVATE);
        gender = "";
        editor = prefs.edit();
        firstname="A";
        lastname="A";
        mAuth = FirebaseAuth.getInstance();
        fname=findViewById(R.id.fnamesetup);
        lname=findViewById(R.id.lnamesetup);
        submit = findViewById(R.id.submitdetails);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = fname.getText().toString();
                lastname = lname.getText().toString();
                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(getApplicationContext(), "Enter First Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gender.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Gender!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("firstname",firstname);
                editor.putString("lastname",lastname);
                editor.putString("gender",gender);
                editor.putString("detailset","yes");
                editor.putString("email",mAuth.getCurrentUser().getEmail());
                editor.putString("username",mAuth.getCurrentUser().getUid());
                editor.apply();
                startActivity(new Intent(SetupAccount.this,HomeActivity.class));
                finish();
            }
        });
    }
    public void rbclick(View view)
    {
        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = findViewById(radiobuttonid);
        gender = rb.getText().toString();
    }
}
