package com.fourhcistudents.activo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;


public class SituationChooserActivity extends ActionBarActivity {
    private SharedPreferences sp;
    private HashMap<String, String> situationPrefsHash = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situation_chooser);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        setDefaults();
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
            case R.id.trigger_notification:
                Toast.makeText(this, "Notifications should trigger", Toast.LENGTH_SHORT).show();
                MainActivity.sendNotification(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void onRadioButtonClicked(View view) {
        String button = "";
        RadioGroup rg = (RadioGroup) findViewById(R.id.situation_radioGroup_user_activity);
        int buttonID = rg.getCheckedRadioButtonId();
        SharedPreferences.Editor e = sp.edit();

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.situation_radioButton_in_meeting:
                if (checked) {
                    button = "situation_radioButton_in_meeting";
                }
                break;
            case R.id.situation_radioButton_on_call:
                if (checked) {
                    button = "situation_radioButton_on_call";
                }
                break;
            default:
                button = "situation_radioButton_normal";
                break;
        }
        e.putString("user_activity", button);
        e.putInt("situation_radioButton_user_activity_checked", buttonID);
        e.apply();
    }

    // save to prefs
    public void setInPocket(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("phone_location", on);
        e.apply();
    }

    // save to prefs
    public void setManualSituationsEnabled(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("manual_situations_enabled", on);
        e.apply();
    }

    // save to prefs
    public void setSilentMode(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("phone_mode", on);
        e.apply();
    }

    // create toasts
    public void msg(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    // read prefs
    public void setDefaults() {
        // Check default radio button
        RadioGroup rg = (RadioGroup) findViewById(R.id.situation_radioGroup_user_activity);
        if (sp.getInt("situation_radioButton_user_activity_checked", 0) != 0) {
            rg.check(sp.getInt("situation_radioButton_user_activity_checked", 0));
            msg("checked a button");
        }

        // Set switch states
        Switch s_location = (Switch) findViewById(R.id.situation_switch_phone_location);
        Switch s_mode = (Switch) findViewById(R.id.situation_switch_phone_mode);
        Switch s_enabled = (Switch) findViewById(R.id.manual_situations_enabled);

        s_location.setChecked(sp.getBoolean("situation_switch_phone_location", false));
        s_mode.setChecked(sp.getBoolean("situation_switch_phone_mode", false));
        s_enabled.setChecked(sp.getBoolean("manual_situations_enabled", false));
    }
}

