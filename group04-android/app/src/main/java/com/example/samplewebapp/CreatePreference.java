package com.example.samplewebapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreatePreference extends AppCompatActivity {

    public static final int MAIN_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_preferences);



    }

    public void onReturnClick(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }

    public void onMakePreferenceClick(View v){

        TextView tv = findViewById(R.id.statusField);

        EditText userField = findViewById(R.id.userField);

        String txt = userField.getText().toString();
        if(!txt.equals("")){
            Toast.makeText(this, "Rating Successfully Saved!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "A Name is Required", Toast.LENGTH_LONG).show();
        }
        EditText restaurantField = findViewById(R.id.restaurantField);
        txt = restaurantField.getText().toString();

        if(txt.equals("")){
            Toast.makeText(this, "A Restaurant is Required", Toast.LENGTH_LONG).show();
        }

        EditText scoreField = findViewById(R.id.scoreField);

        txt = scoreField.getText().toString();


        if(txt.equals("")){
            Toast.makeText(this, "A Score is Required", Toast.LENGTH_LONG).show();
        }

        try{
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try{
                    //add a +"&bio="+bio.getText().toString()
                    URL url = new URL("http://10.0.2.2:3004/createpreferenceApp?name=" + userField.getText().toString() +"&restaurant="+restaurantField.getText().toString()+"&score=" + scoreField.getText().toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    JSONObject jo = new JSONObject(response);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            });

        } catch (Exception e){
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }
}
