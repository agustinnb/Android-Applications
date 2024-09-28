package com.anb.coinconverter.feature;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        ConnectivityManager conexion=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo status= conexion.getActiveNetworkInfo();
        if (status != null && status.isConnected()==true){
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(LaunchActivity.this, "There is not a network available", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }






}

