package com.fourhcistudents.activo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class HomeActivity extends ActionBarActivity{
    // Speech
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Speech
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // Notification listener handler
        // TODO: Triggered by timer
        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnShowNotification:
                        showNotification();
                        break;

                    case R.id.btnCancelNotification:
                        cancelNotification(0);
                        break;
                }
            }
        };

        // we will set the listeners
        findViewById(R.id.btnShowNotification).setOnClickListener(handler);
        findViewById(R.id.btnCancelNotification).setOnClickListener(handler);

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

    public void showNotification() {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(HomeActivity.this, StartExerciseActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(HomeActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle("You've been still for a while!")
                .setContentText("Here's an awesome exercise for you!")
                .setSmallIcon(R.drawable.activo_logo)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .setPriority(Notification.PRIORITY_MAX) // Fix for showing addAction when it's connected to PC
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    public void cancelNotification(int notificationId){

        if (Context.NOTIFICATION_SERVICE!=null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

    // Show google speech input dialog
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // TODO: comment out in final version
                    //txtSpeechInput.setText(result.get(0));
                    // Check for keyword
                    voiceCommand(result.get(0));
                }
                break;
            }
        }
    }

    // Speech command
    public void voiceCommand(String r) {
        System.out.println(r);
        //Separate each word and put in an array
        String [] wordList = r.split("\\s+");
        System.out.println("LIST: " + Arrays.toString(wordList));

        //Array[0] should contain the keyword
        System.out.println("FIRST WORD: " + wordList[0]);
        String firstWord = wordList[0];

        if (firstWord.equals("start")) {
            startActivity(new Intent(this, StartExerciseActivity.class));
        } else {
            System.out.println("--NOT RECOGNIZED--");
        }
    }

}
