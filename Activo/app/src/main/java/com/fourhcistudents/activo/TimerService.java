package com.fourhcistudents.activo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fourhcistudents.pedometer.StepService;

public class TimerService extends Service {
    private static final String TAG = "TimerService";
    private NotificationManager mNM;

    /**
     * Class for clients to access.
     */
    public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }


//    public TimerService() {
//        // get the timeout from the settings
//        // register with the settings object to
//    }

    @Override
    public void onCreate() {
        Log.i(TAG, "[SERVICE] onCreate");
        super.onCreate();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        // Load settings
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mActivoSettings = new ActivoSettings(mSettings);
        mState = getSharedPreferences("state", 0);

        mUtils = Utils.getInstance();
        mUtils.setService(this);
        mUtils.initTTSIfNeeded();

        acquireWakeLock();

        // Start detecting
        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();

        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
        // code be called whenever the phone enters standby mode.
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        mStepDisplayer = new StepDisplayer(mActivoSettings, mUtils);
        mStepDisplayer.setSteps(mSteps = mState.getInt("steps", 0));
        mStepDisplayer.addListener(mStepListener);
        mStepDetector.addStepListener(mStepDisplayer);

        mPaceNotifier     = new PaceNotifier(mActivoSettings, mUtils);
        mPaceNotifier.setPace(mPace = mState.getInt("pace", 0));
        mPaceNotifier.addListener(mPaceListener);
        mStepDetector.addStepListener(mPaceNotifier);

        mDistanceNotifier = new DistanceNotifier(mDistanceListener, mActivoSettings, mUtils);
        mDistanceNotifier.setDistance(mDistance = mState.getFloat("distance", 0));
        mStepDetector.addStepListener(mDistanceNotifier);

        mSpeedNotifier    = new SpeedNotifier(mSpeedListener,    mActivoSettings, mUtils);
        mSpeedNotifier.setSpeed(mSpeed = mState.getFloat("speed", 0));
        mPaceNotifier.addListener(mSpeedNotifier);

        mCaloriesNotifier = new CaloriesNotifier(mCaloriesListener, mActivoSettings, mUtils);
        mCaloriesNotifier.setCalories(mCalories = mState.getFloat("calories", 0));
        mStepDetector.addStepListener(mCaloriesNotifier);

        mSpeakingTimer = new SpeakingTimer(mActivoSettings, mUtils);
        mSpeakingTimer.addListener(mStepDisplayer);
        mSpeakingTimer.addListener(mPaceNotifier);
        mSpeakingTimer.addListener(mDistanceNotifier);
        mSpeakingTimer.addListener(mSpeedNotifier);
        mSpeakingTimer.addListener(mCaloriesNotifier);
        mStepDetector.addStepListener(mSpeakingTimer);

        // Used when debugging:
        // mStepBuzzer = new StepBuzzer(this);
        // mStepDetector.addStepListener(mStepBuzzer);

        // Start voice
        reloadSettings();

        // Tell the user we started.
        Toast.makeText(this, getText(R.string.started), Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Timer service starting", Toast.LENGTH_SHORT).show();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Timer service done", Toast.LENGTH_SHORT).show();
    }


    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        Notification notification = new Notification(R.drawable.ic_notification, null,
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        Intent activoIntent = new Intent();
        activoIntent.setComponent(new ComponentName(this, HomeActivity.class));
        activoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                activoIntent, 0);
        notification.setLatestEventInfo(this, text,
                getText(R.string.notification_subtitle), contentIntent);

        mNM.notify(R.string.app_name, notification);
    }



}



//    public void startTimer(listener) {
//
//    }
//
//    public void timerExpired() {
//
//    }
//


// Method: counter that either counts time constantly, or sets a wake-up call for the future

// Method: reset method

// Method: change the timer interval

// Method: alert listeners (usernotifier) that timer went off

//    final int interval = 3000; // 3 Seconds
//    Handler handler = new Handler();
//    Runnable runnable = new Runnable(){
//        public void run() {
//            Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();
//
//
//        }
//    };
//    handler.postAtTime(runnable, System.currentTimeMillis()+interval);
//    handler.postDelayed(runnable, interval);
