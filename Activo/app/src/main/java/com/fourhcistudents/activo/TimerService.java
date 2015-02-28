package com.fourhcistudents.activo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
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

    private long mInterval;
    private Handler mHandler;



    /**
     * Class for clients to access.
     */
    public class LocalBinder extends Binder {
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

//        // Load settings
//        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
//        mActivoSettings = new ActivoSettings(mSettings);
//        mInterval = mSettings.getTimerInterval();

        mUtils = Utils.getInstance();
        mUtils.setService(this);
//        mUtils.initTTSIfNeeded();

        acquireWakeLock();

//        // Start detecting
//        mStepDetector = new StepDetector();
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        registerDetector();
//
        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
        // code be called whenever the phone enters standby mode.
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        mHandler = new Handler();

            // Tell the user we started.
        Toast.makeText(this, "Timer service created", Toast.LENGTH_SHORT).show();
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
     * Receives messages from activity.
     */
    private final IBinder mBinder = new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[SERVICE] onBind");
        return mBinder;
    }

    public void startTimer(long interval) {
        Log.i(TAG, "Timer started, interval: " + interval);
        mInterval = interval;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyListeners();
            }
        }, mInterval);
    }

    public void stopTimer() {
        mHandler.removeCallbacksAndMessages(null);
    }


    public interface ICallback {
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb) {
        mCallback = cb;
        //mStepDisplayer.passValue();
        //mPaceListener.passValue();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.app_name) + "-timer";
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

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Reregisters the wakelock (not sure why)
                wakeLock.release();
                acquireWakeLock();
            }
        }
    };


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
        public void timerExpired(long t);
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    public void notifyListeners() {
//        mUtils.ding();
        for (Listener listener : mListeners) {
            listener.timerExpired(Utils.currentTimeInMillis());
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
