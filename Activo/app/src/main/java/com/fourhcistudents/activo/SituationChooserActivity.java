package com.fourhcistudents.activo;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;



public class SituationChooserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situation_chooser);
        // I've added this for those who like to have the shadow below the actionbar


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String url=null;
        switch(item.getItemId())
        {
            case R.id.action_settings:
                url="https://play.google.com/store/apps/developer?id=AndroidDeveloperLB";
                break;
            case R.id.situation_chooser:
                url="https://github.com/AndroidDeveloperLB";
                break;

        }
        if(url==null)
            return true;
        final Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
        return true;
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
