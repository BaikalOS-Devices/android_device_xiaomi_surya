/*
 * Copyright (C) 2018,2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.dirac;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

public final class DiracUtils {

    protected static DiracSound mDiracSound;
    private static boolean mInitialized;
    private static Context mContext;

    public static void initialize(Context context) {
        if (!mInitialized) {
            mContext = context;
            mDiracSound = new DiracSound(1, 1);
            mInitialized = true;
        }
    }

    protected static void setMusic(boolean enable) {
        mDiracSound.setMusic(enable ? 1 : 0);
    }

    protected static void setMovie(boolean enable) {
        mDiracSound.setMovie(enable ? 1 : 0);
    }

    protected static void setSurround(int level) {
        mDiracSound.setSurround(level);
    }

    protected static void setVoice(int level) {
        mDiracSound.setVoice(level);
    }

    protected static boolean isDiracEnabled() {
        return mDiracSound != null && mDiracSound.getMusic() == 1;
    }

    protected static void setLevel(String preset) {
        String[] level = preset.split("\\s*,\\s*");

        for (int band = 0; band <= level.length - 1; band++) {
            mDiracSound.setLevel(band, Float.valueOf(level[band]));
        }
    }

    protected static String getLevel() {
        String selected = "";
        for (int band = 0; band <= 6; band++) {
            int temp = (int) mDiracSound.getLevel(band);
            selected += String.valueOf(temp);
            if (band != 6) selected += ",";
        }
        return selected;
    }

    protected static void setLevel(int band, int level) {
        mDiracSound.setLevel(band, Float.valueOf(level));
    }

    protected static int getLevel(int band) {
        return (int)mDiracSound.getLevel(band);
    }

    protected static void setHeadsetType(int paramInt) {
        mDiracSound.setHeadsetType(paramInt);
    }

    protected static void setScenario(int paramInt) {
        mDiracSound.setScenario(paramInt);
    }


    protected static void setTestInt(String index, String value) {
        mDiracSound.setInt(index,value);
    }

    protected static void setTestString(String index, String value) {
        mDiracSound.setString(index,value);
    }

}
