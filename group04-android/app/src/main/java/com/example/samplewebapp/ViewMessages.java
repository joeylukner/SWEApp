package com.example.samplewebapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ViewMessages extends AppCompatActivity {

    private ArrayList<String> messages;

    private ArrayAdapter<String> adapter;

    private Context packageContext = this;
    private ListView list;
    public final static int MAIN_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_messages);

        list = findViewById(R.id.list);
        Button returnButton = new Button(this);
        returnButton.setText("Return to Home");
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(packageContext, MainActivity.class);
                startActivityForResult(i, MAIN_ID);
            }
        });
        list.addFooterView(returnButton);


    }


    public void onReturnClick(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }

    public void onMessButtonClick(View v) {
        EditText et = findViewById(R.id.statusField);

        list = findViewById(R.id.list);
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        list.setAdapter(adapter);

        Button badBtn = findViewById(R.id.startButton);
        badBtn.setVisibility(View.GONE);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"

                            URL url = new URL("http://10.0.2.2:3004/viewrecievedmessagesApp?recipient=" + et.getText().toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            JSONArray ja = new JSONArray(response);

                            for(int i = 0; i < ja.length(); i++){
                                JSONObject m = (JSONObject) ja.get(i);
                                String newMessage = m.get("sender").toString() + ": " +m.get("message").toString();
                                messages.add(newMessage);


                            }
                            adapter.notifyDataSetChanged();
                           // JSONObject m = new JSONObject(response);

                            //String temp = m.get("sender").toString();
                            //String temp2 = m.get("message").toString();
                            //TextView mess = findViewById(R.id.messageText);

                            //mess.setText(temp + ": " + temp2 + "\n");

//                            for(int i = 0; i < jo.length(); i++){
//                                JSONObject m = (JSONObject) jo.get(i);
//                                Pair<String,String> item = new Pair<>((String) m.get("sender"),(String) m.get("message"));
//                                messages.add(item);
//                            }
//
//                            LinearLayout messageView = (LinearLayout) findViewById(R.id.messageTable);
//
//                            for(Pair<String,String> m : messages){
//
//                                String t1 = m.first;
//                                String t2 = m.second;
//                                TableRow row = new TableRow(this);
//                                TextView text = new TextView(this);
//
//                                text.setText(t1 + ": " + t2);
//
//                                row.addView(text);
//                                messageView.addView(text);
//                            }

                            // need to set the instance variable in the Activity object
                            // because we cannot directly access the TextView from here

                            // message = jo.getString("message");

                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

            // now we can set the status in the TextView
            //et.setText(message)
        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();

        }
    }

}
