package com.fourhcistudents.activo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;


public class TimerService extends Service {
    private static final String TAG = "TimerService";
    private SharedPreferences mSettings;
    private ActivoSettings mActivoSettings;
    private SharedPreferences mState;
    private SharedPreferences.Editor mStateEditor;
    private Utils mUtils;

    private PowerManager.WakeLock wakeLock;
    private NotificationManager mNM;



    /**
     * Class for clients to access.
     */
    public class TimerBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
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
//        mUtils.initTTSIfNeeded();

        acquireWakeLock();

//        // Start detecting
//        mStepDetector = new StepDetector();
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        registerDetector();
//
//        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
//        // code be called whenever the phone enters standby mode.
//        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(mReceiver, filter);
//
//
//
//        // Start voice
//        reloadSettings();

        // Tell the user we started.
        Toast.makeText(this, "Timer service created", Toast.LENGTH_SHORT).show();
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

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
//        if (mActivoSettings.wakeAggressively()) {
//            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
//        }
//        else if (mActivoSettings.keepScreenOn()) {
//            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
//        }
//        else {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
//        }
        wakeLock = pm.newWakeLock(wakeFlags, TAG);
        wakeLock.acquire();
    }



    //-----------------------------------------------------
    // Listener

    public interface Listener {
        public void timeExpired();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    public void notifyListeners() {
//        mUtils.ding();
        for (Listener listener : mListeners) {
            listener.timeExpired();
        }
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
