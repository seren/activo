package com.fourhcistudents.activo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends Activity {

    private SharedPreferences mSettings;
    private ActivoSettings mActivoSettings;
    private boolean mStepServiceIsRunning;
    private boolean mTimerIsRunning;
    private static final String TAG = "Activo";



    public MainActivity() {

    /*

    reads: system settings, user prefs
    reads: user location, phone location, user activity
    invokes and listens: timer service, step counter service
    creates and calls: notification mgr with modalities and parameters (text, images, etc)

    create timer
    create pedometer

    loop:
        wait for timer
        check pedometer last step time
        if none since timer set,
            notify user()

     */
    }

    public void onStart() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mActivoSettings = new com.fourhcistudents.activo.ActivoSettings(mSettings);
    }

    public void onResume() {

        // create and init timer
        // create and init step counter
        // register listeners with the timer and stepcounter?


    }


    public void getSystemSettings() {
        // if we're faking state
        // get situations (user activity, location(proximity), silent mode) from prefs or phone state
        // else
        // proximity sensor
        // volume vs silent
        // vibration
    }

    public void timerListener() {
       // when timer goes off, check step counter to see how many minutes it's been since the last step
       // if too long, send notification
    }

    //
    static public void sendNotification(Context context) {
        HashMap<String, String> notificationParams = new HashMap<String, String>();
//        UserNotificationManager notifier = new com.fourhcistudents.activo.UserNotificationManager();

//        Array modalities = chooseModalities(inputs);
//        notificationParams.put("text", "bob");
//        notificationParams.put("img", "highfive");
//        notificationParams.put("speech", "Time to workout!");
//        notificationParams.put("alert", "bell");
//        notifier(notificationParams, );
          Toast.makeText(context, "Notification triggered (in MainActivity)", Toast.LENGTH_LONG).show();
    }


    // reasoner
    public void chooseModalities(HashMap inputs){
//        ModalityChooser mc = new ModalityChooser();
//        String[] outputModalities = {"vibration", "text"};
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    public



//    private StepService mStepService;
//
//    private ServiceConnection mStepConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            mStepService = ((StepService.StepBinder)service).getService();
//
//            mStepService.registerCallback(mStepCallback);
//            mStepService.reloadSettings();
//
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            mStepService = null;
//        }
//    };


//    private StepService.ICallback mStepCallback = new StepService.ICallback() {
//        public void stepsChanged(int value) {
//            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
//        }
//    };

}
