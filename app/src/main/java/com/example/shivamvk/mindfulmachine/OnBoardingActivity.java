package com.example.shivamvk.mindfulmachine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        /*findViewById(R.id.bt_on_boarding_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        },2000);

        /*findViewById(R.id.bt_on_boarding_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });*/
    }
}
