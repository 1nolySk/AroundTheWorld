package com.ask.atw.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ask.atw.Activities.AboutActivity;
import com.ask.atw.Activities.SettingsActivity;
import com.ask.atw.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    LinearLayout about,logout,share,settings;
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    private static final String mypref = "userdetails.conf";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        about = getActivity().findViewById(R.id.aboutbut);
        logout = getActivity().findViewById(R.id.logoutbut);
        share = getActivity().findViewById(R.id.sharebut);
        settings = getActivity().findViewById(R.id.settingsbut);
        prefs = getActivity().getSharedPreferences(mypref,Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                prefs.edit().clear().apply();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=net.rention.mind.skillz");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        super.onStart();
    }
}
