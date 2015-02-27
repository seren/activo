package com.fourhcistudents.activo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;


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
            case R.id.situation_chooser:
                startActivity(new Intent(this, SituationChooserActivity.class));
                return true;
            case R.id.trigger_notification:
                Toast.makeText(this, "Notifications should trigger", Toast.LENGTH_SHORT).show();
                MainActivity.sendNotification(this);
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


        public void bodypart_view (View arg0){
            // Move to the next view!
            Intent i = new Intent(HomeActivity.this, BodyPartChooserActivity.class);
            startActivity(i);
        }


}

