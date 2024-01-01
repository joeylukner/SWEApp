package com.example.samplewebapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EditProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected String message;
    protected String name;
    protected String age;
    protected String bio;
    protected String ageRangePreference;
    protected String genderPreference;
    protected String dietaryRestrictions;
    protected String diningGroupSizePreference;

    public final static int MAIN_ID = 1;
    public void onReturn(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }
    public void onSubmit(View v){
        Toast.makeText(this, "Changes Successfully Saved!", Toast.LENGTH_LONG).show();
        TextView tv = findViewById(R.id.statusField);
        EditText nameField = findViewById(R.id.nameField);
        EditText ageField = findViewById(R.id.ageField);
        EditText bioField = findViewById(R.id.bioField);
        EditText agePrefField = findViewById(R.id.agePrefField);
        EditText genderPrefField = findViewById(R.id.genderPrefField);
        EditText dietRestField = findViewById(R.id.dietRestField);
        EditText dGSizePrefField = findViewById(R.id.dGSizePrefField);
        try{
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try{
                    URL url = new URL("http://10.0.2.2:3004/saveEditApp?name=" + nameField.getText().toString() +"&age="+ageField.getText().toString()+
                            "&bio="+bioField.getText().toString() + "&ageRangePref="+agePrefField.getText().toString() + "&genderPref="+genderPrefField.getText().toString() +
                            "&dietaryRestrictions=" +dietRestField.getText().toString() + "&diningGroupSizePref="+dGSizePrefField.getText().toString());
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


    public void onPersonButtonClick(View v) {

        EditText et = findViewById(R.id.statusField);
        TextView nameView = findViewById(R.id.nameView);
        TextView ageView = findViewById(R.id.ageView);
        EditText nameField = findViewById(R.id.nameField);
        EditText ageField = findViewById(R.id.ageField);
        EditText bioField = findViewById(R.id.bioField);
        EditText agePrefField = findViewById(R.id.agePrefField);
        EditText genderPrefField = findViewById(R.id.genderPrefField);
        EditText dietRestField = findViewById(R.id.dietRestField);
        EditText dGSizePrefField = findViewById(R.id.dGSizePrefField);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"

                            URL url = new URL("http://10.0.2.2:3004/getPerson?name=" + et.getText().toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            JSONObject jo = new JSONObject(response);

                            // need to set the instance variable in the Activity object
                            // because we cannot directly access the TextView from here

                            // message = jo.getString("message");
                            name = jo.getString("name");
                            age = jo.getString("age");
                            bio = jo.getString("bio");
                            ageRangePreference = jo.getString("ageRangePref");
                            genderPreference = jo.getString("genderPref");
                            dietaryRestrictions = jo.getString("dietaryRestrictions");
                            diningGroupSizePreference = jo.getString("diningGroupSizePref");
                            message = response;
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

            // now we can set the status in the TextView
            //et.setText(message);
            nameField.setText(name);
            ageField.setText(age);
            bioField.setText(bio);
            agePrefField.setText(ageRangePreference);
            genderPrefField.setText(genderPreference);
            dietRestField.setText(dietaryRestrictions);
            dGSizePrefField.setText(diningGroupSizePreference);

        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
            et.setText(e.toString());
        }
    }
}
