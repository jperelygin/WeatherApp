package com.example.jperelygin.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CatTestAsyncActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button startButton;
    ProgressBar pBar;
    ProgressBar mHorizontalProgressBar;
    CatTask catTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_test_async);


        mTextView = findViewById(R.id.catgoesup);
        pBar = findViewById(R.id.pbar);
        startButton = findViewById(R.id.button);
        mHorizontalProgressBar = findViewById(R.id.progress2);
    }

    public void testProgressButtonClick(View view) {
        catTask = new CatTask();
        catTask.execute("cat1.jpg", "cat2.jpg", "cat3.jpg", "cat4.jpg");
    }

    public void getResultButtonClick(View view) {
        String status = catTask.getStatus().toString();
        Toast.makeText(CatTestAsyncActivity.this,
                status, Toast.LENGTH_SHORT).show();
        if (status == "RUNNING" || status == "FINISHED"){
            catTask.cancel(true);
        } else {

        }
    }

    class CatTask extends AsyncTask<String,Integer,Integer>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mHorizontalProgressBar.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            mTextView.setText("Полез на крышу");
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... urls){
            try {
                int counter = 0;
                for (String url : urls) {
                    getFloor(counter);
                    publishProgress(++counter);
                }

                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return 2018;
        }

        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            mTextView.setText("Level:" + values[0]);
            mHorizontalProgressBar.setProgress(values[0]);
        }

        protected void onCancelled(){
            super.onCancelled();
            mTextView.setText("Cancelled");
            startButton.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.INVISIBLE);
            mHorizontalProgressBar.setProgress(0);
        }

        @Override
        protected void onPostExecute(Integer result){
            super.onPostExecute(result);
            startButton.setVisibility(View.VISIBLE);
            mTextView.setText("Залез");
            pBar.setVisibility(View.INVISIBLE);
            mHorizontalProgressBar.setProgress(0);
        }

        private void getFloor(int floor) throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

    }
}
