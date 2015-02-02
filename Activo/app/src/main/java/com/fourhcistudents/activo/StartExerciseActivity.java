package com.fourhcistudents.activo;


import android.os.Bundle;
import android.view.Menu;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;




public class StartExerciseActivity extends ActionBarActivity {

    ProgressBar progressBar;
    CountDownTimer exerciseCountDownTimer;
    private int timeLeft;

    // private static long countDownInterval = 1000;
    //private CountDownTimer cdt;
    //private CountDownTimer model;
    //long millisInFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexercise);

        TextView timeLeft = (TextView) findViewById(R.id.time_left_value);
        timeLeft.setText("" + timeLeft / 1000);


        progressBar = (ProgressBar)findViewById(R.id.progressbar);
// Pause Button
        Button pausebtn = (Button) findViewById(R.id.pause_btn);
        pausebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onPause() {
                cdt.cancel();
                super.onPause();
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    public void setProgress(int progress) {
        progress = 100;

        final int actualTime = progress * 1000;
        progressBar.setProgress(progress);

        exerciseCountDownTimer = new CountDownTimer(actualTime, 1000) {
            int totalTime = actualTime;
            @Override
            public void onTick(long millisUntilFinished) {
                progress = (int) ( millisUntilFinished/ (double)totalTime * 100);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progress =0;
                progressBar.setProgress(progress);
            }
        }.start();


    }
}

