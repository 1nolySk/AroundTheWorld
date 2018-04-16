package com.ask.atw.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ask.atw.R;

public class AboutActivity extends AppCompatActivity {
    TextView privacypolicy,opl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        privacypolicy = findViewById(R.id.privacybut);
        opl = findViewById(R.id.liscencebut);

    }

}
