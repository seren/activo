package com.fourhcistudents.activo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    private String[] situationPrefs = {
            "phone_mode",
            "user_activity",
            "phone_location",
    };

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
        getMenuInflater().inflate(R.menu.menu_situation_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    msg("in meeting");
                    button = "situation_radioButton_in_meeting";
                }
                break;
            case R.id.situation_radioButton_on_call:
                if (checked) {
                    msg("on call");
                    button = "situation_radioButton_on_call";
                }
                break;
            default:
                msg("normal");
                button = "situation_radioButton_normal";
                break;
        }
        e.putString("user_activity", button);
        e.putInt("situation_radioButton_user_activity_checked", buttonID);
        e.commit();
    }

    // save to prefs
    public void setInPocket(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("phone_location", on);
        e.commit();
        if (on) {
            msg("in pocket");
        } else {
            msg("out of pocket");
        }
    }

    // save to prefs
    public void setSilentMode(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("phone_mode", on);
        e.commit();
        if (on) {
            msg("silent mode on");
        } else {
            msg("silent mode off");
        }
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

        s_location.setChecked(sp.getBoolean("situation_switch_phone_location", false));
        s_mode.setChecked(sp.getBoolean("situation_switch_phone_mode", false));
    }
}

