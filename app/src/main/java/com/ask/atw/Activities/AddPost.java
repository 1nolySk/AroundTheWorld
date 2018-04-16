package com.ask.atw.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ask.atw.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class AddPost extends AppCompatActivity {

    ImageView imadd,imshow;
    EditText place,description;
    SharedPreferences prefs;
    private static final String mypref = "userdetails.conf";
    Integer REQUEST_CAMERA=1,SELECT_FILE=0;
    Button addpost;
    Bitmap image;
    ProgressDialog progressDialog;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    DatabaseReference Fdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image=null;

        setContentView(R.layout.activity_add_post);
        prefs = getSharedPreferences(mypref,MODE_PRIVATE);
        place = findViewById(R.id.placetext);
        description = findViewById(R.id.descriptiontext);
        imadd = findViewById(R.id.addimage);
        imshow = findViewById(R.id.imagehead);
        imadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        addpost=findViewById(R.id.addpost);

        final String id=prefs.getString("username","unknown");

        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReferenceFromUrl("gs://around-the-world-ab95e.appspot.com/");

        Fdatabase=FirebaseDatabase.getInstance().getReferenceFromUrl("https://around-the-world-ab95e.firebaseio.com/").child(id);


        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (place!=null && description!=null)
                    uploadImage(id);
                else
                {

                    Toast.makeText(AddPost.this,"Please fill All the Fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void SelectImage(){
        final CharSequence[] items={"Camera","Gallery","Cancel"};
        final AlertDialog.Builder imbuilder=new AlertDialog.Builder(AddPost.this);
        imbuilder.setTitle("Add Image");
        imbuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera")){
                    Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camintent,REQUEST_CAMERA);
                }
                else if(items[which].equals("Gallery")){
                    Intent galintent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galintent.setType("image/*");
                    startActivityForResult(galintent.createChooser(galintent,"Select File"),SELECT_FILE);
                }
                else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        imbuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_CAMERA){

                Bundle cambundle = data.getExtras();
                image = (Bitmap)cambundle.get("data");
                imshow.setImageBitmap(image);
                imadd.setVisibility(View.GONE);
                imshow.setVisibility(View.VISIBLE);

            }
            else if(requestCode==SELECT_FILE){
                try {
                    Uri selectedImageUri = data.getData();
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                    imshow.setImageBitmap(image);
                    imadd.setVisibility(View.GONE);
                    imshow.setVisibility(View.VISIBLE);
                }
                catch(Exception e)
                {
                    System.out.print(e.toString());
                }
            }
        }
    }

    private void uploadImage(String id) {

        if(image != null)
        {
            if(Build.VERSION.SDK_INT <= 21){
                progressDialog = new ProgressDialog(this, android.app.AlertDialog.THEME_HOLO_DARK);
            }
            else{
               progressDialog = new ProgressDialog(this);
            }
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final String timeStamp = Long.toString(System.currentTimeMillis());

            StorageReference ref = storageReference.child(timeStamp);


            final DatabaseReference temp=Fdatabase.child(timeStamp);

            temp.child("place").setValue(place.getText().toString());
            temp.child("desc").setValue(description.getText().toString());
            temp.child("author").setValue(prefs.getString("firstname","")+" "+prefs.getString("lastname",""));
            temp.child("name").setValue(timeStamp);
            temp.child("gender").setValue(prefs.getString("gender",""));




            //Image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPost.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            //Log.d("-----Link --",taskSnapshot.getDownloadUrl().toString());
                            temp.child("url").setValue(taskSnapshot.getDownloadUrl().toString());

                            clear();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(AddPost.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

            //Data





        }
        else
            Toast.makeText(AddPost.this,"Please Choose an image",Toast.LENGTH_SHORT).show();
    }

    void clear()
    {
        finish();
        startActivity(getIntent());
    }
}
