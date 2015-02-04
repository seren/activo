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


    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexercise);}
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressbar);
        TextView textTimer = (TextView)findViewById(R.id.time_left_value);

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    public void startTimer(final int minuti) {
        countDownTimer = new CountDownTimer(60 * minuti * 1000, 500) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;

                int barVal = (barMax) - ((int)(seconds/60*100)+(int)(seconds%60));

                progressBar.setProgress(barVal);
                textTimer.setText(String.valueOf(millisUntilFinished) );

            }

            @Override
            public void onFinish() {
                if(textTimer.getText().equals("00:00")){
                    textTimer.setText("STOP");
                }
                else{
                    textTimer.setText("2:00");
                }
            }
        }.start();


    }
}

