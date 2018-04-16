package com.ask.atw.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.ask.atw.Class.ImageData;
import com.ask.atw.Class.PostAdapter;
import com.ask.atw.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPost extends AppCompatActivity{

    EditText searchtext;
    TextView sBtn;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference Fdatabase,Idatabase;
    private int occur=0;
    SharedPreferences prefs;
    private static final String mypref = "userdetails.conf";
    List<ImageData> data;

    String keyPlace="99";
    RecyclerView homeRecy;
    private RecyclerView.Adapter postAdap;
    private RecyclerView.LayoutManager RecyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchpost);
        searchtext= findViewById(R.id.searchtxt);
        sBtn=findViewById(R.id.sbtn);

        homeRecy= findViewById(R.id.post_recycler);
        RecyLayout = new LinearLayoutManager(this);
        int resId = R.anim.layout_anim;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,resId);
        homeRecy.setLayoutAnimation(animation);

        homeRecy.setLayoutManager(RecyLayout);
        homeRecy.setHasFixedSize(true);

        homeRecy.setHasFixedSize(true);
        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReferenceFromUrl("gs://around-the-world-ab95e.appspot.com/");

        //recycler


        Fdatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://around-the-world-ab95e.firebaseio.com/");

        //searchtext.setOnQueryTextListener(this);


        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence search = searchtext.getText();

                if (search.length()!=0)
                {
                    keyPlace=search.toString().trim();
                }
                else
                    keyPlace="99";

                Thread data_fetch= new Thread(fetch_data);
                data_fetch.start();
            }
        });


    }



    Runnable fetch_data = new Runnable() {
        @Override
        public void run() {
            Fdatabase.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    data = new ArrayList<>();
                    occur=0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        //Log.d("-----data "+snapshot.getKey(),snapshot.getValue().toString());

                        if(! snapshot.getKey().equals("images"))
                        {
                            for (DataSnapshot snappic : snapshot.getChildren()) {
                                //Log.d("-----data " + snappic.getKey(), snappic.getValue().toString());

                                ImageData temp = snappic.getValue(ImageData.class);

                                if (temp.getPlace().toUpperCase().contains(keyPlace.toUpperCase()))
                                data.add(temp);
                            }
                        }


                    }
                    Log.d("-----adp","completed");
                    if (data!=null) {

                        Collections.shuffle(data);

                        if (occur==0) {
                            occur=1;
                            postAdap = new PostAdapter(getApplicationContext(), data);
                            homeRecy.setAdapter(postAdap);
                        }else {
                            final Context context = homeRecy.getContext();
                            final LayoutAnimationController controller =
                                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim);

                            homeRecy.setLayoutAnimation(controller);
                            homeRecy.getAdapter().notifyDataSetChanged();
                            homeRecy.scheduleLayoutAnimation();
                            postAdap.notifyDataSetChanged();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    };
}
