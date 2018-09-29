package com.example.shivamvk.mindfulmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout flHomeActivity;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_place_order:
                    getSupportActionBar().show();
                    fragment = new CreateOrderFragment();
                    break;
                case R.id.navigation_orders:
                    getSupportActionBar().hide();
                     fragment = new MyOrdersFragment();
                    break;
                case R.id.navigation_account:
                    getSupportActionBar().hide();
                    fragment = new AccountFragment();
                    break;
            }

            if (fragment != null){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home_activity, fragment);
                fragmentTransaction.commit();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (SharedPrefManager.getInstance(this).isLoggedIn() == null){
            startActivity(new Intent(HomeActivity.this, OnBoardingActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        flHomeActivity = findViewById(R.id.fl_home_activity);

        Fragment fragment = new CreateOrderFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_home_activity, fragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
