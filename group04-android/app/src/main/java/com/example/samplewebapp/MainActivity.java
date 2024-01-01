package com.example.samplewebapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    @Override
     protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.start);
    }

    public static final int CREATE_PROFILE_ID = 1;
    public static final int ADDRESTAURANTSID = 1;
    public static final int MESSAGE_PROFILE_ID = 1;
    public static final int EDIT_PROFILE_ID = 1;
    public void onStartButtonClick(View v){
        Intent i = new Intent(this, CreateProfile.class);
        startActivityForResult(i, CREATE_PROFILE_ID);
    }

    public void onEditButtonClick(View v){
        Intent i = new Intent(this, EditProfile.class);
        startActivityForResult(i, EDIT_PROFILE_ID);
    }
    public void onMessageButtonClick(View v){
        Intent i = new Intent(this, ViewMessages.class);
        startActivityForResult(i, MESSAGE_PROFILE_ID);

    }
    public void onCreateMessageButtonClick(View v){
        Intent i = new Intent(this, CreateMessage.class);
        startActivity(i);
    }
    public void onRateRestaurantButtonClick(View v){
        Intent i = new Intent(this, CreatePreference.class);
        startActivity(i);
    }

    public void onAddRestaurantsButtonClick(View v){
        Intent i = new Intent(this, AddRestaurants.class);
        startActivityForResult(i, ADDRESTAURANTSID);
    }


}
