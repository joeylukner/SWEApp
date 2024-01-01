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


public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
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
    public void onReturnClick(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }

    public void onSubmitClick(View v){

        TextView tv = findViewById(R.id.statusField);
        EditText nameField = findViewById(R.id.nameField);
        String txt = nameField.getText().toString();
        if(!txt.equals("")){
            Toast.makeText(this, "Profile Successfully Created!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "A Name is Required", Toast.LENGTH_LONG).show();
        }
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
                    //add a +"&bio="+bio.getText().toString()
                    URL url = new URL("http://10.0.2.2:3004/saveCreateApp?name=" + nameField.getText().toString() +"&age="+ageField.getText().toString()+
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
}
