package com.example.shivamvk.mindfulmachine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    Context context;
    List<Order> listOfOrders;

    public MyOrdersAdapter(Context context, List<Order> listOfOrders) {
        this.context = context;
        this.listOfOrders = listOfOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_order_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order order = listOfOrders.get(i);

        String orderid = "Order id: " + generateHash(
                order.getLoadingPoint(),
                order.getTripDestination(),
                order.getTruckType(),
                order.getMaterialType(),
                order.getLoadingDate()
        );

        // viewHolder.tvOrderItemOrderId.setText(orderid);

        String loadingPoint = order.getLoadingPoint();
        String destinationPoint = order.getTripDestination();
        String trucktype = order.getTruckType();
/*
        char chartruck = trucktype.charAt(0);

        switch (chartruck){
            case 'O':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.open);
                break;
            case 'C':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.container);
                break;
            case 'T':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.trailer);
        }*/

        if(loadingPoint.length() > 34){
            char loading[] = loadingPoint.toCharArray();
            loading[34] = '.';
            loading[32] = '.';
            loading[33] = '.';
            loadingPoint = String.valueOf(loading);
        }

        if (destinationPoint.length() > 34){
            char destination[] = destinationPoint.toCharArray();
            destination[34] = '.';
            destination[32] = '.';
            destination[33] = '.';
            destinationPoint = String.valueOf(destination);
        }

        Log.i("CHECK", "onBindViewHolder: loading :  " + loadingPoint.length());

        viewHolder.tvOrderItemLoadingPoint.setText(loadingPoint);
        viewHolder.tvOrderItemTripDestination.setText(destinationPoint);
        // viewHolder.tvOrderItemTruckType.setText(order.getTruckType());
        //viewHolder.tvOrderItemMaterialType.setText(order.getMaterialType());
        viewHolder.tvOrderItemLoadingTime.setText(order.getLoadingTime());
        viewHolder.tvOrderItemLoadingDate.setText(order.getLoadingDate());
        // viewHolder.tvOrderItemRemarks.setText(order.getRemarks());

       /* if(order.getCompleted().equals("Yes")){
            viewHolder.btOrderItemStatus.setText("Accepted (Click to see details)");
            viewHolder.btOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
        }*/

       viewHolder.btOrderId.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               context.startActivity(new Intent(context,OrderDetailsActivity.class));
           }
       });
    }

    private String generateHash(String s, String s1, String s2, String s3, String s4) {
        int hash = 21;
        String main = s + s1 + s2 + s3 + s4;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    @Override
    public int getItemCount() {
        return listOfOrders.size();

        // return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvOrderItemLoadingPoint,tvOrderItemLoadingDate,tvOrderItemLoadingTime,
                tvOrderItemTripDestination;
        private Button btOrderId;

        // private Button btOrderItemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderItemLoadingPoint = itemView.findViewById(R.id.tv_order_item_loading_point);
            tvOrderItemTripDestination = itemView.findViewById(R.id.tv_order_item_destination_point);
            tvOrderItemLoadingDate = itemView.findViewById(R.id.tv_order_item_loading_date);
            tvOrderItemLoadingTime = itemView.findViewById(R.id.tv_order_item_loading_time);
            btOrderId = itemView.findViewById(R.id.bt_order_item_order_id);
            //  btOrderItemStatus =itemView.findViewById(R.id.bt_order_item_status);
        }
    }
}
