package com.fourhcistudents.activo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fourhcistudents.pedometer.StepDetector;
import com.fourhcistudents.pedometer.StepService;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MainActivity extends Activity {

    private SharedPreferences mSettings;
    private ActivoSettings mActivoSettings;
    private static final String TAG = "Activo";
    private StepDetector mStepDetector;
    private SensorManager mSensorManager;
    private Utils mUtils;

    private long mLastStepTimestamp;

    private boolean mStepServiceIsRunning;
    private boolean mTimerServiceIsRunning;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "[MAINACTIVITY] onCreate");
        super.onCreate(savedInstanceState);

        mUtils = Utils.getInstance();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "[MAINACTIVITY] onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "[MAINACTIVITY] onResume");
        super.onResume();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mActivoSettings = new ActivoSettings(mSettings);

        // Read from preferences if the service was running on the last onPause
        mTimerServiceIsRunning = mActivoSettings.isTimerServiceRunning();
        mStepServiceIsRunning = mActivoSettings.isStepServiceRunning();

        // Start the service if this is considered to be an application start (last onPause was long ago)
        if (mActivoSettings.isNewStart()) {
            if (mTimerServiceIsRunning) { startTimerService(); }
            if (mStepServiceIsRunning) { startStepService(); }
        }
        bindTimerService();
        bindStepService();

        // Start detecting
        TimerService mTimerService = new TimerService();

        // test timer for debugging
        mTimerService.startTimer(5000);
    }


    private void startStepService() {
        if (! mStepServiceIsRunning) {
            Log.i(TAG, "[STEPSERVICE] Start");
            mStepServiceIsRunning = true;
            startService(new Intent(MainActivity.this,
                    StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[STEPSERVICE] Bind");
        bindService(new Intent(MainActivity.this,
                StepService.class), mStepServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[STEPSERVICE] Unbind");
        unbindService(mStepServiceConnection);
    }

    private void stopStepService() {
        Log.i(TAG, "[STEPSERVICE] Stop");
        if (mStepService != null) {
            Log.i(TAG, "[STEPSERVICE] stopService");
            stopService(new Intent(MainActivity.this,
                    StepService.class));
        }
        mStepServiceIsRunning = false;
    }

    private void startTimerService() {
        if (! mTimerServiceIsRunning) {
            Log.i(TAG, "[TIMERSERVICE] Start");
            mTimerServiceIsRunning = true;
            startService(new Intent(MainActivity.this,
                    TimerService.class));
        }
    }

    private void bindTimerService() {
        Log.i(TAG, "[TIMERSERVICE] Bind");
        bindService(new Intent(MainActivity.this,
                TimerService.class), mTimerServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindTimerService() {
        Log.i(TAG, "[TIMERSERVICE] Unbind");
        unbindService(mTimerServiceConnection);
    }

    private void stopTimerService() {
        Log.i(TAG, "[TIMERSERVICE] Stop");
        if (mTimerService != null) {
            Log.i(TAG, "[TIMERSERVICE] stopService");
            stopService(new Intent(MainActivity.this,
                    TimerService.class));
        }
        mTimerServiceIsRunning = false;
    }




    public void getSystemSettings() {
        // if we're faking state
        // get situations (user activity, location(proximity), silent mode) from prefs or phone state
        // else
        // proximity sensor
        // volume vs silent
        // vibration
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
    public String[] chooseModalities(){
//        ModalityChooser mc = new ModalityChooser();
//        String[] outputModalities = {"vibration", "text"};
        String[] modalities = new String[0];
        return modalities;
    }



    private StepService mStepService;

    private ServiceConnection mStepServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mStepService = ((StepService.StepBinder)service).getService();
            mStepService.registerCallback(mStepCallback);
        }
        public void onServiceDisconnected(ComponentName className) {
            mStepService = null;
        }
    };

    private TimerService mTimerService;

    private ServiceConnection mTimerServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mTimerService = ((TimerService.LocalBinder)service).getService();
            mTimerService.registerCallback(mTimerCallback);
        }
        public void onServiceDisconnected(ComponentName className) {
            mTimerService = null;
        }
    };


    private static final int TIME_EXPIRED_MSG = 1;

    private TimerService.ICallback mTimerCallback = new TimerService.ICallback() {
        public void timerExpired(long timeStamp) {
            mTimerHandler.sendMessage(mTimerHandler.obtainMessage(TIME_EXPIRED_MSG, 0, 0));
            mLastStepTimestamp = timeStamp;
        }
    };

    Context context = this;

    private Handler mTimerHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    long maxIdleMs = (long)mActivoSettings.maxAllowedInactiveSeconds() * 1000;
                    long msOfInactivity = Utils.currentTimeInMillis() - mLastStepTimestamp;
                    if ( msOfInactivity > maxIdleMs ) {
                        String[] modalities = chooseModalities();
//                        com.fourhcistudents.activo.NotificationManager.notifyUser(modalities);
                        Toast.makeText(context, "Notification triggered (in MainActivity)", Toast.LENGTH_LONG).show();
                    } else {
                        mTimerService.startTimer(maxIdleMs - msOfInactivity);
                    }
                    Log.i(TAG, "Timer expired at " + Utils.currentTimeInMillis() +
                            "\n  maxIdleMs: " + maxIdleMs +
                            "\n  msOfInactivity: " + msOfInactivity);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };







    private static final int STEPS_MSG = 1;

    private StepService.ICallback mStepCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mStepHandler.sendMessage(mStepHandler.obtainMessage(STEPS_MSG, value, 0));
        }
    };

    private Handler mStepHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    mLastStepTimestamp = Utils.currentTimeInMillis();
                    Log.i(TAG, "Step at " + mLastStepTimestamp);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

}
