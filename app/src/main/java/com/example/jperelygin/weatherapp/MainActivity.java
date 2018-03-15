package com.example.jperelygin.weatherapp;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public EditText cityRequest;
    public Button searchButton;
    public TextView temperatureText;
    public TextView cityText;
    public ProgressBar bar;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.button);
        cityRequest = findViewById(R.id.editText);
        temperatureText = findViewById(R.id.temperature);
        cityText = findViewById(R.id.cityName);
        bar = findViewById(R.id.progressBar);

        String urlAPI = "http://api.apixu.com/v1/current.json?"; // weather api
        String key = BuildConfig.ApiKey;
        String APIkey = "key=" + key; // weather api key
        url = urlAPI + APIkey + "&q="; //concatenation

    }

    public void searchCity(View view) {
        WeatherResponse r = new WeatherResponse();
        r.execute(url);
    }

    public void goToTest(View view) {

        //  TEST with Moscow. Set Invisible after tests
        cityRequest.setText("Moscow");


        WeatherResponse resp = new WeatherResponse();
        resp.execute(url);
    }


    private class WeatherResponse extends AsyncTask<String, Map<String, String>, Map<String, String>>{

        @Override
        protected void onPreExecute() {
            super.onProgressUpdate();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Map<String, String> result){
            super.onPostExecute(result);
            bar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Map <String, String>... result){
            super.onProgressUpdate(result);
            for (Map<String, String> List : result) {
                for (Map.Entry me : List.entrySet()){
                    Log.w("key", me.getKey() + " <- key");
                    Log.w("value", me.getValue() + " <- value");
                    if (me.getKey() == "city"){         // I dont like this way, but it works.
                        cityText.setText(me.getValue().toString());
                    } else if (me.getKey() == "temp"){
                        temperatureText.setText(me.getValue().toString());
                    } else {
                        temperatureText.setText("ERR");
                    }
                }
            }

        }

        @Override
        protected Map<String, String> doInBackground(String... params){

            final Map<String, String> result = new HashMap<String, String>();

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            String city = cityRequest.getText().toString();
            String RequestUrl = url + city;     // to keep "url" clear

            Log.w("res", "URL = " + url);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, RequestUrl,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w("res", "Responce:" + response.toString());
                    try{
                        String city = response.getJSONObject("location")
                                .getString("region");
                        Log.w("res", city);
                        result.put("city", city);
                        Log.w("result", "city " + result.get("city"));
                        String temperature = response.getJSONObject("current")
                                .getString("temp_c");
                        Log.w("res", temperature);
                        result.put("temp",temperature);
                        Log.w("result", "temp " + result.get("temp"));
                        publishProgress(result);
                    } catch (JSONException e){
                        Log.w("EXCEPTION", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // nothing here
                }
            });
            queue.add(jsonObjReq);
            return null;
        }

    }

}
