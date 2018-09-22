package com.example.shivamvk.mindfulmachine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;

public class UploadActivity extends AppCompatActivity {

    private TextView tvDocumentName, tvDocumentFrontSide, tvDocumentBackSide;
    private String documentName,panCard,visitingCard,aadharCard;

    private Button btUploadFront, btUploadBack;
    private ImageView ivFront, ivBack;

    private static final int UPLOAD_PHOTO_FRONT = 1;
    private static final int UPLOAD_PHOTO_BACK = 2;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        setTitle("Submit Documents");

        documentName = getIntent().getStringExtra("documentname");

        tvDocumentName = findViewById(R.id.tv_document_name_activity_upload);
        tvDocumentFrontSide = findViewById(R.id.tv_document_front_side_activity_upload);
        tvDocumentBackSide = findViewById(R.id.tv_document_back_side_activity_upload);

        ivFront = findViewById(R.id.iv_upload_front);
        ivBack = findViewById(R.id.iv_upload_back);

        btUploadFront = findViewById(R.id.bt_upload_front_side);
        btUploadBack = findViewById(R.id.bt_upload_back_side);


        storageReference = FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(SharedPrefManager.getInstance(this).getNumber());

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(SharedPrefManager.getInstance(UploadActivity.this).getNumber());

        panCard = "PAN CARD";
        visitingCard = "VISITING CARD";
        aadharCard = "AADHAR CARD";

        if (documentName != null) {
            if (documentName.equals(panCard)) {
                tvDocumentName.setText(panCard);
                tvDocumentFrontSide.setText(panCard + " FRONT SIDE");
                tvDocumentBackSide.setText(panCard + " BACK SIDE");
            } else if (documentName.equals(visitingCard)) {
                tvDocumentName.setText(visitingCard);
                tvDocumentFrontSide.setText(visitingCard + " FRONT SIDE");
                tvDocumentBackSide.setText(visitingCard + " BACK SIDE");
            } else if(documentName.equals(aadharCard)){
                tvDocumentName.setText(aadharCard);
                tvDocumentFrontSide.setText(aadharCard + " FRONT SIDE");
                tvDocumentBackSide.setText(aadharCard + " BACK SIDE");
            }
        }


        loadImages();

        btUploadFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), UPLOAD_PHOTO_FRONT);
            }
        });

        btUploadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), UPLOAD_PHOTO_BACK);
            }
        });
    }

    private void loadImages() {
        if(documentName.equals(panCard)){
            if(!SharedPrefManager.getInstance(getBaseContext()).getPanCardFrontUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getPanCardFrontUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivFront);

                ivFront.setVisibility(View.VISIBLE);
                tvDocumentFrontSide.setVisibility(View.GONE);


            } else{
                ivFront.setVisibility(View.GONE);
                tvDocumentFrontSide.setVisibility(View.VISIBLE);
            }

            if(!SharedPrefManager.getInstance(this).getPanCardBackUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getPanCardBackUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivBack);
                ivBack.setVisibility(View.VISIBLE);
                tvDocumentBackSide.setVisibility(View.GONE);
            } else {
                ivBack.setVisibility(View.GONE);
                tvDocumentBackSide.setVisibility(View.VISIBLE);
            }
        } else if(documentName.equals(aadharCard)){
            if(!SharedPrefManager.getInstance(getBaseContext()).getAadharCardFrontUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getAadharCardFrontUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivFront);

                ivFront.setVisibility(View.VISIBLE);
                tvDocumentFrontSide.setVisibility(View.GONE);


            } else{
                ivFront.setVisibility(View.GONE);
                tvDocumentFrontSide.setVisibility(View.VISIBLE);
            }

            if(!SharedPrefManager.getInstance(this).getAadharCardBackUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getAadharCardBackUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivBack);

                ivBack.setVisibility(View.VISIBLE);
                tvDocumentBackSide.setVisibility(View.GONE);
            } else {
                ivBack.setVisibility(View.GONE);
                tvDocumentBackSide.setVisibility(View.VISIBLE);
            }
        } else if(documentName.equals(visitingCard)){
            if(!SharedPrefManager.getInstance(getBaseContext()).getVisitingCardFrontUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getVisitingCardFrontUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivFront);

                ivFront.setVisibility(View.VISIBLE);
                tvDocumentFrontSide.setVisibility(View.GONE);


            } else{
                ivFront.setVisibility(View.GONE);
                tvDocumentFrontSide.setVisibility(View.VISIBLE);
            }

            if(!SharedPrefManager.getInstance(this).getVisitingCardBackUrl().equals("Not provided")) {
                Picasso.get()
                        .load(SharedPrefManager.getInstance(this).getVisitingCardBackUrl())
                        .placeholder(R.drawable.aadharplaceholder)
                        .into(ivBack);

                ivBack.setVisibility(View.VISIBLE);
                tvDocumentBackSide.setVisibility(View.GONE);
            } else {
                ivBack.setVisibility(View.GONE);
                tvDocumentBackSide.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == UPLOAD_PHOTO_FRONT) {
            if (resultCode == RESULT_OK) {

                final Uri selectImageUri = data.getData();

                if (documentName.equals(panCard)) {

                    uploadImage("pancardfront",selectImageUri);
                    Log.i("CREATE", "onActivityResult: "+ selectImageUri);

                } else if(documentName.equals(aadharCard)){
                    uploadImage("aadharcardfront",selectImageUri);
                } else if(documentName.equals(visitingCard)){
                    uploadImage("visitingcardfront",selectImageUri);
                }
                ivFront.setImageURI(selectImageUri);
                ivFront.setVisibility(View.VISIBLE);
                tvDocumentFrontSide.setVisibility(View.GONE);

            }
        }else if(requestCode == UPLOAD_PHOTO_BACK){
            if(resultCode == RESULT_OK){

                final Uri selectImageUri = data.getData();

                if (documentName.equals(panCard)) {
                    uploadImage("pancardback",selectImageUri);

                }else if(documentName.equals(aadharCard)){
                    uploadImage("aadharcardback",selectImageUri);
                } else if(documentName.equals(visitingCard)){
                    uploadImage("visitingcardback",selectImageUri);
                }
                ivBack.setImageURI(selectImageUri);
                ivBack.setVisibility(View.VISIBLE);
                tvDocumentBackSide.setVisibility(View.GONE);
            }
        }
    }

    private String uploadImage(final String imagekey, final Uri selectImageUri) {

        final String[] imageUrl = {""};
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        storageReference.child("documentimages")
                .child(imagekey)
                .child(selectImageUri.getLastPathSegment())
                .putFile(selectImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();

                        Toast.makeText(UploadActivity.this, "Photo Successfully Uploaded", Toast.LENGTH_SHORT).show();

                        imageUrl[0] = taskSnapshot.getDownloadUrl().toString();

                        Log.i("CREATE", "onSuccess: 22 " + imageUrl[0]);

                        if(imagekey.equals("pancardfront"))
                            SharedPrefManager.getInstance(getBaseContext()).pancardfront(imageUrl[0]);
                        else if(imagekey.equals("pancardback"))
                            SharedPrefManager.getInstance(getBaseContext()).pancardback(imageUrl[0]);
                        else if(imagekey.equals("aadharcardfront"))
                            SharedPrefManager.getInstance(getBaseContext()).aadharcardfront(imageUrl[0]);
                        else if(imagekey.equals("aadharcardback"))
                            SharedPrefManager.getInstance(getBaseContext()).aadharcardback(imageUrl[0]);
                        else if(imagekey.equals("visitingcardfront"))
                            SharedPrefManager.getInstance(getBaseContext()).visitingcardfront(imageUrl[0]);
                        else if(imagekey.equals("visitingcardback"))
                            SharedPrefManager.getInstance(getBaseContext()).visitingcardback(imageUrl[0]);

                        databaseReference.child("documentimages").child(imagekey).setValue(imageUrl[0]);
                    }
                });
     return imageUrl[0];
    }
}
