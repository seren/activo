package com.fourhcistudents.activo;


import android.os.Bundle;
import android.view.Menu;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;




public class StartExerciseActivity extends ActionBarActivity {

    private static long countDownInterval = 1000;
    private CountDownTimer cdt;
    private CountDownTimer model;
    long millisInFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexercise);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onResume() {

            cdt = new CountDownTimer(millisInFuture, countDownInterval) {
                TextView timeLeft = (TextView) findViewById(R.id.time_left);

                ProgressBar m_bar = (ProgressBar) findViewById(R.id.progressbar);
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft.setText("" + millisUntilFinished / 1000);

                    m_bar.setProgress ( (int) (millisInFuture/1000) );
                }

                @Override
                public void onFinish() {

                }
            };


        super.onResume();
    }

}
