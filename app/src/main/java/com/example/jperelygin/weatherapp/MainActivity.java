package com.example.jperelygin.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private class WeatherResponse extends AsyncTask<String, Void, Map<String, String>>{

        @Override
        protected void onProgressUpdate(Void... values) {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Map<String, String> doInBackground(String... params){

            final Map<String, String> result = new HashMap<String, String>();

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            String urlAPI = "http://api.apixu.com/v1/current.json?"; // weather api
            String APIkey = "key=f10687e83ef8444d945180734182301";
            String city = cityRequest.getText().toString();
            String url = urlAPI + APIkey + "&q=" + city;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w("res", "Responce:" + response.toString());
                    try{
                        String city = response.getJSONObject("location")
                                .getString("region");
                        Log.w("res", city);
                        result.put("city", city);
                        String temperature = response.getJSONObject("current")
                                .getString("temp_c");
                        Log.w("res", temperature);
                        result.put("temp",temperature);
                    } catch (JSONException e){
                        Log.w("EXCEPTION", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });

            queue.add(jsonObjReq);

            return result;

        }


        @Override
        protected void onPostExecute(Map<String, String> result){
            super.onPostExecute(result);

            bar.setVisibility(View.INVISIBLE);

            /**
            cityText.setText(result.get("city")); // 1 element = city name
            temperatureText.setText(result.get("temp")); // 2 element = current temperature
            */
            Log.w("in onPostExecute", "!!!!" + result.get("city"));

        }
    }

}
