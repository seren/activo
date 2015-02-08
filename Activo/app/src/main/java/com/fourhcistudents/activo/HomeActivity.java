package com.fourhcistudents.activo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Switch;


public class HomeActivity extends ActionBarActivity{

    private Switch oralswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        oralswitch = (Switch) findViewById(R.id.oralswitch);
        oralswitch.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startExercise(View arg0) {
        // Move to the next view!
        Intent i = new Intent(HomeActivity.this, StartExerciseActivity.class);
        startActivity(i);
    }

    public void chooseSituation(View arg0) {
        Intent i = new Intent(HomeActivity.this, SituationChooserActivity.class);
        startActivity(i);
    }


}

