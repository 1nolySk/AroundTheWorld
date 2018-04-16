package com.ask.atw.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ask.atw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {

    TextView unametext,emaildisplay;
    EditText oldpass,newpass;
    Button passchangebtn,confirm,logout;
    ImageView logomale,logofemale;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final String mypref = "userdetails.conf";
    ProgressDialog waitdialog;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        unametext = getActivity().findViewById(R.id.unameprofile);
        oldpass = getActivity().findViewById(R.id.oldpass);
        newpass = getActivity().findViewById(R.id.newpass);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        waitdialog = new ProgressDialog(getContext(),ProgressDialog.THEME_HOLO_DARK);
        waitdialog.setMessage("Updating Password...");
        passchangebtn = getActivity().findViewById(R.id.changepass);
        confirm = getActivity().findViewById(R.id.passchangeconfirm);
        logout = getActivity().findViewById(R.id.profilelogout);
        prefs = getActivity().getSharedPreferences(mypref,Context.MODE_PRIVATE);
        logomale = getActivity().findViewById(R.id.logomale);
        logofemale = getActivity().findViewById(R.id.logofemale);
        emaildisplay = getActivity().findViewById(R.id.emailprofile);
        emaildisplay.setText(user.getEmail());
        if(prefs.getString("gender","").equalsIgnoreCase("female")){
            logomale.setVisibility(View.GONE);
            logofemale.setVisibility(View.VISIBLE);
        }
        unametext.setText(prefs.getString("firstname","")+" "+prefs.getString("lastname",""));
        passchangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("logintype","").equalsIgnoreCase("google")){
                    Toast.makeText(getContext(),"You are logged in with Google Account! Change password from Google Site",Toast.LENGTH_SHORT).show();
                    return;
                }
                oldpass.setVisibility(View.VISIBLE);
                newpass.setVisibility(View.VISIBLE);
                passchangebtn.setVisibility(View.GONE);
                confirm.setVisibility(View.VISIBLE);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=user.getEmail();
                final String old = oldpass.getText().toString();
                final String newpas = newpass.getText().toString();
                if (TextUtils.isEmpty(old)){
                    Toast.makeText(getContext(),"Enter Old Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newpas)){
                    Toast.makeText(getContext(),"Enter New Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                waitdialog.show();
                AuthCredential credential = EmailAuthProvider.getCredential(email,old);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpas).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(getContext(),"Sorry something went wrong",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getContext(),"Password Updated successfully",Toast.LENGTH_SHORT).show();
                                        oldpass.setVisibility(View.GONE);
                                        newpass.setVisibility(View.GONE);
                                        confirm.setVisibility(View.GONE);
                                        passchangebtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getContext(),"Please provide correct old password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                prefs.edit().clear().apply();
            }
        });
        super.onStart();
    }
}
