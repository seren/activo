package com.fourhcistudents.activo;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;

public class SituationChooserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situation_chooser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.situation_chooser:
                startActivity(new Intent(this, SituationChooserActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.situation_radioButton_in_meeting:
                if (checked)
// set pref to meeting
                    break;
            case R.id.situation_radioButton_on_call:
                if (checked)
// set pref to meeting
                    break;
            default:
// set pref to normal
        }
    }

    public void setInPocket(View view) {

    }

    public void setSilentMode(View view) {

    }


}
