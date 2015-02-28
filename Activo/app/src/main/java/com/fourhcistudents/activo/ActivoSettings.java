/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fourhcistudents.activo;

import android.content.SharedPreferences;

import com.fourhcistudents.pedometer.Utils;

/**
 * Wrapper for {@link android.content.SharedPreferences}, handles preferences-related tasks.
 * @author Levente Bagi
 */
public class ActivoSettings {

    private SharedPreferences mSettings;

    public ActivoSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    public boolean isManualSituationsEnabled() {
        return mSettings.getBoolean("manual_situations_enabled", false);
    }

    public boolean isInPocket() {
        return mSettings.getBoolean("phone_location", false);
    }

    public boolean isSilentMode() {
        return mSettings.getBoolean("phone_mode", false);
    }

    public String getUserActivity() {
        String p = mSettings.getString("user_activity", "none");
        return
            p.equals("situation_radioButton_in_meeting") ? "in_meeting" : (
            p.equals("situation_radioButton_on_call") ? "on_call" : (
            "situation_radioButton_normal"));
    }

    public long getTimerInterval() {
        long t = mSettings.getLong("timer_interval_ms", 5000);
        return t;
    }

    public void setTimerInterval(float t) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat("timer_interval", t);
        editor.commit();
    }


    //
    // Internal

    public void saveServiceRunningWithTimestamp(String serviceType, boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(serviceType + "service_running", running);
        editor.putLong(serviceType + "last_seen", com.fourhcistudents.activo.Utils.currentTimeInMillis());
        editor.commit();
    }

    public void saveServiceRunningWithNullTimestamp(String serviceType, boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(serviceType + "service_running", running);
        editor.putLong(serviceType + "last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning(String serviceType) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(serviceType + "service_running", false);
        editor.putLong(serviceType + "last_seen", 0);
        editor.commit();
    }

    public void saveTimerServiceRunningWithTimestamp(boolean running) {
        saveServiceRunningWithTimestamp("timer", running);
    }

    public void saveTimerServiceRunningWithNullTimestamp(boolean running) {
         saveServiceRunningWithNullTimestamp("timer", running);
    }

    public void clearTimerServiceRunning() {
        clearServiceRunning("timer");
    }

    public void saveStepServiceRunningWithTimestamp(boolean running) {
        saveServiceRunningWithTimestamp("step", running);
    }

    public void saveStepServiceRunningWithNullTimestamp(boolean running) {
        saveServiceRunningWithNullTimestamp("step", running);
    }

    public void clearStepServiceRunning() {
        clearServiceRunning("step");
    }

    public boolean isTimerServiceRunning() {
        return mSettings.getBoolean("time_service_running", false);
    }

    public boolean isStepServiceRunning() {
        return mSettings.getBoolean("step_service_running", false);
    }

    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < Utils.currentTimeInMillis() - 1000*60*10;
    }

    public int maxAllowedInactiveSeconds() {
        return mSettings.getInt("max_user_idle_allowed", 20);
    }
}
