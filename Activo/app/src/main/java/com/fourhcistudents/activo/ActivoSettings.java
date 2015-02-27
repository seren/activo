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

    SharedPreferences mSettings;

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

    public String userActivity() {
        return mSettings.getString("user_activity", "situation_radioButton_normal");
    }

    public String getMaintainOption() {
        String p = mSettings.getString("user_activity", "none");
        return
            p.equals("situation_radioButton_in_meeting") ? "in_meeting" : (
            p.equals("situation_radioButton_on_call") ? "on_call" : (
            "situation_radioButton_normal"));
    }



    //
    // Internal

    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", com.fourhcistudents.activo.Utils.currentTimeInMillis());
        editor.commit();
    }

    public void saveServiceRunningWithNullTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }

    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < Utils.currentTimeInMillis() - 1000*60*10;
    }

}
