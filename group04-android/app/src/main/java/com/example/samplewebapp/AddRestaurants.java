package com.example.samplewebapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AddRestaurants extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurants);

//        System.out.println(restaurants);
//        Spinner spinner = (Spinner) findViewById(R.id.difficulty_spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurants);
//// Specify the layout to use when the list of choices appears.
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner.
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(this);
//
//        try {
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(() -> {
//                try {
//                    URL url = new URL("http://10.0.2.2:3004/populateRestaurantSpinner");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.connect();
//
//                    Scanner in = new Scanner(url.openStream());
//                    String response = in.nextLine();
//
//                    JSONArray ja = new JSONArray(response);
//
//                    for (int i = 0; i < ja.length(); i++) {
//                        JSONObject obj = (JSONObject) ja.get(i);
//                        String name = obj.get("name").toString();
//                        restaurants.add(name);
//                    }
//                    adapter.notifyDataSetChanged();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//
//            });
//        } catch (Exception e){
//            e.printStackTrace();
//
//        }

    }



    String restaurant;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        restaurant = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    protected String message;
    protected JSONArray restaurants;
    protected String restaurantString = "";
    public void onPersonButtonClickRestaurants(View v) {

        EditText et = findViewById(R.id.statusField);

        TextView currentRestaurantsView = findViewById(R.id.CurrentRestaurants);
        EditText enterRestaurantsView = findViewById(R.id.enterRestaurants);


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

                            try{
                                restaurants = jo.getJSONArray("restaurantList");
                                restaurantString = "";
                                for(int i = 0; i < restaurants.length(); i ++){
                                    restaurantString += restaurants.getString(i) + "\n";
                                }
                            }catch (Exception e){
                                restaurantString = "Your list is currently empty";
                            }

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

            currentRestaurantsView.setText(restaurantString);



        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
            et.setText(e.toString());
        }
    }
    protected String[] restaurantStringArray;
    public void onAddRestaurant(View v) {

        EditText et = findViewById(R.id.statusField);
        EditText restaurantView = findViewById(R.id.enterRestaurants);

        String restaurantName = restaurantView.getText().toString();

        Toast.makeText(this, restaurantName + " Successfully Saved to " + et.getText().toString() + "'s Profile!", Toast.LENGTH_LONG).show();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
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

                            try{
                                restaurants = jo.getJSONArray("restaurantList");
                                restaurantStringArray = new String[restaurants.length() + 1];
                                for (int i = 0; i < restaurants.length(); i++) {
                                    restaurantStringArray[i] = restaurants.getString(i);
                                }
                                restaurantStringArray[restaurants.length()] = restaurantName;
                            } catch (Exception e){
                                restaurantStringArray = new String[1];
                                restaurantStringArray[0] = restaurantName;
                            }


                            String urlString = "http://10.0.2.2:3004/saveAddRestaurantApp?name="+et.getText().toString()+"&restaurantList=";

                            for (int i = 0; i < restaurantStringArray.length - 1; i++) {
                                urlString += restaurantStringArray[i] + "&restaurantList=";
                            }
                            restaurantStringArray[restaurantStringArray.length-1]= restaurantName;
                            urlString += restaurantStringArray[restaurantStringArray.length - 1];
                            Log.v("anything",urlString);
                            URL editUrl = new URL( urlString);

                            HttpURLConnection conn1 = (HttpURLConnection) editUrl.openConnection();
                            conn1.setRequestMethod("GET");
                            conn1.connect();

                            Scanner in1 = new Scanner(editUrl.openStream());
                            String response1 = in.nextLine();

                            JSONObject jo1 = new JSONObject(response);


                            message = response;

                        } catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            et.setText(e.toString());
        }
    }
}
