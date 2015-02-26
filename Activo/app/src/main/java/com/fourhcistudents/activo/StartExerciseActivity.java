package com.fourhcistudents.activo;

import android.content.ActivityNotFoundException;
import android.content.res.TypedArray;
import android.media.AudioManager;
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

    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;
    // Buttons
    private Button nextButton;
    private Button previousButton;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;

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

        // get info for audio manager
        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        checkIfPhoneIsSilent();

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        addListenerOnButton();
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
                return true;
            case R.id.situation_chooser:
                startActivity(new Intent(this, SituationChooserActivity.class));
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
        stopButton = (Button) findViewById(R.id.stop_btn);

        // First run
        if (count == 0) {
            previousButton.setVisibility(View.INVISIBLE);       // Hide previous button
            pauseButton.setVisibility(View.INVISIBLE);          // Hide pause button
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1);   // Initial sound

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

        // STOP
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise("stop");
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
                    txtSpeechInput.setText(result.get(0));
                    // Check for keyword
                    voiceCommand(result.get(0));
                }
                break;
            }
        }
    }

    // Get the right sound to playback
    public void getSound() {

        //TODO: real audio

        if (count == 0) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
        }
        else if (count == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
        }
        else if (count == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
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
        } else if (firstWord.equals("pause")) {
            exercise("pause");
        } else if (firstWord.equals("stop")) {
            exercise("stop");
        } else {
            System.out.println("--NOT RECOGNIZED--");
        }

    }

    public void exercise(String s) {

        final TypedArray exe = getResources().obtainTypedArray(R.array.exercise_images);

        if (s.equals("next")) {
            previousButton.setVisibility(View.VISIBLE);
            if (!isPaused) {
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
            }
            if (count < 2) {
                image.setImageResource(exe.getResourceId(count + 1, 0));
                instruction.setText(instructions[count + 1]);
                count++;
                mediaPlayer.stop();
                if (count == 2) {
                    nextButton.setVisibility(View.INVISIBLE);
                }
            } else {
                limitReached();
            }
            isPaused = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playButton.setVisibility(View.VISIBLE);
            }
        } else if (s.equals("previous")) {
            nextButton.setVisibility(View.VISIBLE);
            if (!isPaused) {
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
            }
            if (count > 0) {
                image.setImageResource(exe.getResourceId(count - 1, 0));
                instruction.setText(instructions[count - 1]);
                count--;
                mediaPlayer.stop();
                if (count == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                }
            } else {
                limitReached();
            }
            isPaused = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playButton.setVisibility(View.VISIBLE);
            }
        } else if (s.equals("play")) {
            pauseButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            if (!isPaused) {
                getSound();
                mediaPlayer.start();
            } else {
                mediaPlayer.start();
                isPaused = false;
            }
        } else if (s.equals("pause")) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            isPaused = true;
        } else if (s.equals("stop")) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
            mediaPlayer.stop();
            isPaused = false;
        }
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


    // Check to see the mode of the phone. If the phone is set to silent mode, the app will also be silent.
     
    private void checkIfPhoneIsSilent() {
        int ringerMode = mAudioManager.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
            mPhoneIsSilent = true;
            mediaPlayer.pause();
        } else {
            mPhoneIsSilent = false;
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

}

