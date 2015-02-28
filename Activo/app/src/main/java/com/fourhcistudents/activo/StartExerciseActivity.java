package com.fourhcistudents.activo;

import android.content.ActivityNotFoundException;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.media.MediaPlayer;

public class StartExerciseActivity extends ActionBarActivity {

    // Speech
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int audioLength = 0;

    private MediaPlayer mediaPlayer;

    // Buttons
    private Button nextButton;
    private Button previousButton;
    private Button playButton;
    private Button pauseButton;
    private Button toggleSoundBtn;
    Drawable soundIcon;

    private boolean mPhoneIsSilent;

    // Images
    private ImageView image;

    // Instructions
    private TextView instruction;
    private String [] instructions = {"Jump!", "Stretch!", "Run!"};

    // Counter to check what exercise is currently shown
    public int count = 0;

    public boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // Play, Pause, Next
        addListenerOnButton();

        mPhoneIsSilent = false; // Initial setting
        toggleUI();

        toggleSoundBtn = (Button)findViewById(R.id.sound);
        toggleSoundBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mPhoneIsSilent) {
                    mediaPlayer.start();
                    mPhoneIsSilent = false;
                } else {
                    mediaPlayer.pause();
                    mPhoneIsSilent = true;
                }
                toggleUI();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
                return true;
            case R.id.situation_chooser:
                startActivity(new Intent(this, SituationChooserActivity.class));
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Button Listeners
    public void addListenerOnButton() {

        image = (ImageView) findViewById(R.id.exercise);
        instruction = (TextView)findViewById(R.id.instructions);
        nextButton = (Button) findViewById(R.id.next_btn);
        previousButton = (Button) findViewById(R.id.previous_btn);
        pauseButton = (Button) findViewById(R.id.pause_btn);
        playButton = (Button) findViewById(R.id.play_btn);

        // First run
        if (count == 0) {
            previousButton.setVisibility(View.INVISIBLE);       // Hide previous button
            playButton.setVisibility(View.INVISIBLE);          // Hide play button
            mediaPlayer = MediaPlayer.create(this, R.raw.jump);   // Initial sound
            mediaPlayer.start();
        }

        // NEXT
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                exercise("next");
            }
        });

        // PREVIOUS
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                exercise("previous");
            }
        });

        // PLAY
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                exercise("play");
            }
        });

        // PAUSE
        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise("pause");
            }
        });

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

    // Get the right sound to playback
    public void getSound() {

        if (count == 0) {
            mediaPlayer = MediaPlayer.create(this, R.raw.jump);
        }
        else if (count == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.stretch);
        }
        else if (count == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.run);
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

        if (firstWord.equals("next")) {
            exercise("next");
            // System often mistakes previous with previews
        } else if (firstWord.equals("previous") || firstWord.equals("previews")) {
            exercise("previous");
        } else if (firstWord.equals("play")) {
            exercise("play");
        } else if (firstWord.equals("pause") || firstWord.equals("boss")) {
            exercise("pause");
        } else {
            System.out.println("--NOT RECOGNIZED--");
        }

    }

    public void exercise(String s) {

        final TypedArray exe = getResources().obtainTypedArray(R.array.exercise_images);

        if (s.equals("next")) {
            mPhoneIsSilent = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            previousButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            if (count < 2) {
                image.setImageResource(exe.getResourceId(count + 1, 0));
                instruction.setText(instructions[count + 1]);
                count++;
                getSound();
                mediaPlayer.start();
                if (count == 2) {
                    nextButton.setVisibility(View.INVISIBLE);
                }
            } else {
                limitReached();
            }
            //isPaused = false;
        } else if (s.equals("previous")) {
            mPhoneIsSilent = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            nextButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            if (count > 0) {
                image.setImageResource(exe.getResourceId(count - 1, 0));
                instruction.setText(instructions[count - 1]);
                count--;
                getSound();
                mediaPlayer.start();
                if (count == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                }
            } else {
                limitReached();
            }
            isPaused = false;
        } else if (s.equals("play")) {
            pauseButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            if (!mediaPlayer.isPlaying()) { // Do this to ensure that you will not play more than once
                if (!isPaused) {
                    getSound();
                    mediaPlayer.start();
                } else {
                    mediaPlayer.seekTo(audioLength);
                    mediaPlayer.start();
                    isPaused = false;
                }
            }
        } else if (s.equals("pause")) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                audioLength = mediaPlayer.getCurrentPosition();
            }
            isPaused = true;
        }
        toggleUI();
    }

    // Pop up when there's no more previous or next exercise
    public void limitReached() {
        new AlertDialog.Builder(this)
                .setMessage("You reached the limit")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                })
                .show();
    }

    //Toggles the UI images from silent to normal and vice-versa

    private void toggleUI() {
        Button imageView = (Button) findViewById(R.id.sound);
        if (mPhoneIsSilent) {
            soundIcon = getResources().getDrawable(R.drawable.sound_off);
        } else {
            soundIcon = getResources().getDrawable(R.drawable.sound_on);
        }
        //Using deprecated method to support minSDKlevel below 16. Maybe change minSDKlevel in manifest and use setBackground instead?
        imageView.setBackgroundDrawable(soundIcon);
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

}

