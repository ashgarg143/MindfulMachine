package com.example.shivamvk.mindfulmachine;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderDetailsActivity extends AppCompatActivity {

    private RelativeLayout rlNotificationsHeader;

    private TextView tvOrderDetails;
    private EditText etOrigin,etDestination,etTruck,etMaterial,etDate,etTime,etPaymentType,etNoOfTrucks,etRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();*/
        setContentView(R.layout.fragment_place_order);

        setTitle("Order details");

        String orderId = getIntent().getStringExtra("orderid");
        String loadingPoint = getIntent().getStringExtra("loadingpoint");
        String destinationPoint = getIntent().getStringExtra("destinationpoint");
        String truckType = getIntent().getStringExtra("trucktype");
        String materialType = getIntent().getStringExtra("materialtype");
        String loadingDate = getIntent().getStringExtra("loadingdate");
        String loadingTime = getIntent().getStringExtra("loadingtime");
        String paymentType = getIntent().getStringExtra("paymenttype");
        String noOfTrucks = getIntent().getStringExtra("nooftrucks");
        String remarks = getIntent().getStringExtra("remarks");

        rlNotificationsHeader = findViewById(R.id.rl_notification_header);

        tvOrderDetails = findViewById(R.id.tv_order);
        etOrigin = findViewById(R.id.et_place_order_loading_point);
        etDestination = findViewById(R.id.et_place_order_trip_destination);
        etTruck = findViewById(R.id.et_place_order_truck_type);
        etMaterial = findViewById(R.id.et_place_order_material_type);
        etDate = findViewById(R.id.et_place_order_loading_date);
        etTime = findViewById(R.id.et_place_order_loading_time);
        etPaymentType = findViewById(R.id.et_place_order_payment_type);
        etNoOfTrucks = findViewById(R.id.et_place_order_no_of_trucks);
        etRemarks = findViewById(R.id.et_place_order_remarks);

        rlNotificationsHeader.setVisibility(View.GONE);

        etDestination.setVisibility(View.VISIBLE);
        etTruck.setVisibility(View.VISIBLE);
        etMaterial.setVisibility(View.VISIBLE);
        etDate.setVisibility(View.VISIBLE);
        etTime.setVisibility(View.VISIBLE);
        etPaymentType.setVisibility(View.VISIBLE);
        etNoOfTrucks.setVisibility(View.VISIBLE);
        etRemarks.setVisibility(View.VISIBLE);

        tvOrderDetails.setText("Order id: "+orderId);
        etOrigin.setText(loadingPoint);
        etDestination.setText(destinationPoint);
        etTruck.setText(truckType);
        etMaterial.setText(materialType);
        etDate.setText(loadingDate);
        etTime.setText(loadingTime);
        etPaymentType.setText(paymentType);
        etNoOfTrucks.setText("No of Trucks: "+noOfTrucks);
        etRemarks.setText(remarks);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
