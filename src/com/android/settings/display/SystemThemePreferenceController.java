/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.display;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.internal.util.pixeldust.PixeldustUtils;

import libcore.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class SystemThemePreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin, Preference.OnPreferenceChangeListener {

    private static final String SYSTEM_THEME = "system_theme_style";
    private static final String SUBS_PACKAGE = "projekt.substratum";

    private ListPreference mSystemThemeStyle;

    public SystemThemePreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return SYSTEM_THEME;
    }

    @Override
    public boolean isAvailable() {
        return !PixeldustUtils.isPackageInstalled(mContext, SUBS_PACKAGE);
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mSystemThemeStyle = (ListPreference) screen.findPreference(SYSTEM_THEME);
        if (!PixeldustUtils.isPackageInstalled(mContext, SUBS_PACKAGE)) {
            int systemThemeStyle = Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.SYSTEM_THEME, 0);
            int valueIndex = mSystemThemeStyle.findIndexOfValue(String.valueOf(systemThemeStyle));
            mSystemThemeStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
            mSystemThemeStyle.setSummary(mSystemThemeStyle.getEntry());
            mSystemThemeStyle.setOnPreferenceChangeListener(this);
        } else {
            mSystemThemeStyle.setEnabled(false);
            mSystemThemeStyle.setSummary(R.string.disable_themes_installed_title);
        }
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSystemThemeStyle) {
            String value = (String) newValue;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SYSTEM_THEME, Integer.valueOf(value));
            int valueIndex = mSystemThemeStyle.findIndexOfValue(value);
            mSystemThemeStyle.setSummary(mSystemThemeStyle.getEntries()[valueIndex]);
        }
        return true;
    }
}
