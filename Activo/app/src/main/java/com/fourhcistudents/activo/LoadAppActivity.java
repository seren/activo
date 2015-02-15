package com.fourhcistudents.activo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * The initial loading screen. Loads for three seconds and then launches the application.
 */

public class LoadAppActivity extends Activity {
    /**
     * The load screen timer.
     */
    private static int LOAD_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadapp);

        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(LoadAppActivity.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, LOAD_TIME_OUT);
    }

}

