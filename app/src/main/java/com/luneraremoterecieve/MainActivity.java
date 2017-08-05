package com.luneraremoterecieve;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        Log.e("ActivitySplash", "Activity");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
