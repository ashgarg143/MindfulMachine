package com.example.shivamvk.mindfulmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UploadActivity extends AppCompatActivity {

    private TextView tvDocumentName,tvDocumentFrontSide,tvDocumentBackSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        setTitle("Submit Documents");

        String documentName = getIntent().getStringExtra("documentname");

        tvDocumentName = findViewById(R.id.tv_document_name_activity_upload);
        tvDocumentFrontSide = findViewById(R.id.tv_document_front_side_activity_upload);
        tvDocumentBackSide = findViewById(R.id.tv_document_back_side_activity_upload);

        String panCard = "PAN CARD";
        String visitingCard = "VISITING CARD";
        String aadharCard = "AADHAR CARD";

        if(documentName!=null){
            if(documentName.equals(panCard)){
                tvDocumentName.setText(panCard);
                tvDocumentFrontSide.setText(panCard + " FRONT SIDE");
                tvDocumentBackSide.setText(panCard + " BACK SIDE");
            } else if(documentName.equals(visitingCard)){
                tvDocumentName.setText(visitingCard);
                tvDocumentFrontSide.setText(visitingCard + " FRONT SIDE");
                tvDocumentBackSide.setText(visitingCard + " BACK SIDE");
            }
        } else {
            tvDocumentName.setText(aadharCard);
            tvDocumentFrontSide.setText(aadharCard + " FRONT SIDE");
            tvDocumentBackSide.setText(aadharCard + " BACK SIDE");
        }
    }
}
