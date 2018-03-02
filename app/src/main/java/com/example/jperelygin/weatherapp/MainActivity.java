package com.example.jperelygin.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText cityRequest;
    public Button searchButton;
    public TextView temperatureText;
    public TextView cityText;
    public ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.button);
        cityRequest = findViewById(R.id.editText);
        temperatureText = findViewById(R.id.temperature);
        cityText = findViewById(R.id.cityName);
        bar = findViewById(R.id.progressBar);

    }

    public void searchCity(View view) {
        WeatherResponse r = new WeatherResponse();
        r.execute();
    }

    public void goToTest(View view) {
        Intent goTotest = new Intent(MainActivity.this, CatTestAsyncActivity.class);
        startActivity(goTotest);
    }


    private class WeatherResponse extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected void onProgressUpdate(Void... values) {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params){

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            String urlAPI = "http://api.apixu.com/v1/current.json?"; // weather api
            String APIkey = "key=f10687e83ef8444d945180734182301";
            String city = cityRequest.getText().toString();
            String url = urlAPI + APIkey + "&q=" + city;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.print("Responce:" + response.toString());
                    bar.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });

            queue.add(jsonObjReq);

            ArrayList<String> result = new ArrayList<String>();

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> list){

            cityText.setText(list.get(1)); // 1 element = city name
            temperatureText.setText(list.get(2)); // 2 element = current temperature
        }
    }

}
