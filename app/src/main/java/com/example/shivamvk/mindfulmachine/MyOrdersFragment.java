package com.example.shivamvk.mindfulmachine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private ProgressBar pbMyOrders;
    private RecyclerView rvMyOrders;
    private TextView tvMyOrders;

    List<Order> listOfOrders;

    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_orders, null, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());

        viewPager = view.findViewById(R.id.container);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    loadMyPendingOrders();
                } else if (tab.getPosition() == 1){
                    loadMyCompletedOrders();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pbMyOrders = view.findViewById(R.id.pb_my_orders);
        rvMyOrders = view.findViewById(R.id.rv_my_orders);
        tvMyOrders = view.findViewById(R.id.tv_my_orders_no_order);

        rvMyOrders.setHasFixedSize(true);
        rvMyOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        listOfOrders = new ArrayList<>();

        loadMyPendingOrders();

    }

    public class PagerAdapter extends FragmentStatePagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0){
                return new AccountFragment();
            } else {
                return new PlaceOrderFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void loadMyCompletedOrders(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(SharedPrefManager.getInstance(getContext()).getNumber())
                .child("orders");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfOrders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order order = snapshot.getValue(Order.class);
                    if (order.getCompleted().equals("Yes")){
                        listOfOrders.add(order);
                    }
                }
                MyOrdersAdapter adapter = new MyOrdersAdapter(getActivity(), listOfOrders);
                rvMyOrders.setAdapter(adapter);
                pbMyOrders.setVisibility(View.GONE);
                tvMyOrders.setVisibility(View.GONE);
                if (listOfOrders.isEmpty()){
                    tvMyOrders.setText("No completed orders yet!");
                    tvMyOrders.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMyPendingOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(SharedPrefManager.getInstance(getContext()).getNumber())
                .child("orders");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfOrders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order order = snapshot.getValue(Order.class);
                    if (order.getCompleted().equals("No")){
                        listOfOrders.add(order);
                    }
                }
                MyOrdersAdapter adapter = new MyOrdersAdapter(getActivity(), listOfOrders);
                rvMyOrders.setAdapter(adapter);
                pbMyOrders.setVisibility(View.GONE);
                tvMyOrders.setVisibility(View.GONE);
                if (listOfOrders.isEmpty()){
                    tvMyOrders.setText("No pending orders yet!");
                    tvMyOrders.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String generateHash(String s) {
        int hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash*31 + s.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
