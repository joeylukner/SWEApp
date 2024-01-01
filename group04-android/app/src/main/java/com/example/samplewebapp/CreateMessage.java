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


public class CreateMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_message);
    }

    protected String message;
    protected String sender;
    protected String recip;

    public final static int MAIN_ID = 1;
    public void onReturnClick(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }

    public void onSubmitClick(View v){

        TextView tv = findViewById(R.id.statusField);
        EditText senderField = findViewById(R.id.senderField);
        String txt = senderField.getText().toString();
        if(!txt.equals("")){
            Toast.makeText(this, "Message Successfully Created!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "A Sender is Required", Toast.LENGTH_LONG).show();
        }
        EditText recipField = findViewById(R.id.recipField);
        txt = recipField.getText().toString();

        if(txt.equals("")){
            Toast.makeText(this, "A Recipient is Required", Toast.LENGTH_LONG).show();
        }

        EditText messageField = findViewById(R.id.inputField);
        txt = messageField.getText().toString();

        if(txt.equals("")){
            Toast.makeText(this, "A Message is Required", Toast.LENGTH_LONG).show();
        }

        try{
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try{
                    //add a +"&bio="+bio.getText().toString()
                    URL url = new URL("http://10.0.2.2:3004/messageCreateApp?sender=" + senderField.getText().toString() +"&recipient="+recipField.getText().toString()+"&message=" + messageField.getText().toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    JSONObject jo = new JSONObject(response);

                } catch (Exception e) {
                    e.printStackTrace();
                    message = e.toString();
                }

            });

        } catch (Exception e){
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }
}