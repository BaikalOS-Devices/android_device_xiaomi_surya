/*
 * Copyright (C) 2018 The LineageOS Project
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

import android.media.audiofx.AudioEffect;

import android.util.Log;

import java.util.UUID;

public class DiracSound extends AudioEffect {

    private static final int DIRACSOUND_PARAM_HEADSET_TYPE = 1;
    private static final int DIRACSOUND_PARAM_EQ_LEVEL = 2;
    private static final int DIRACSOUND_PARAM_MODE = 3;
    private static final int DIRACSOUND_PARAM_MUSIC = 4;
    private static final int DIRACSOUND_PARAM_MOVIE = 5;
    private static final int DIRACSOUND_PARAM_SCENARIO = 15;
    private static final int DIRACSOUND_PARAM_SURROUND = 16;
    private static final int DIRACSOUND_PARAM_VOICE = 17;

    private static final UUID EFFECT_TYPE_DIRACSOUND =
            UUID.fromString("5b8e36a5-144a-4c38-b1d7-0002a5d5c51b");
    private static final String TAG = "DiracSound";

    public DiracSound(int priority, int audioSession) {
        super(EFFECT_TYPE_NULL, EFFECT_TYPE_DIRACSOUND, priority, audioSession);
    }

    public void setMusic(int enable) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(DIRACSOUND_PARAM_MUSIC, enable));
    }

    public void setMovie(int enable) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(DIRACSOUND_PARAM_MOVIE, enable));
    }

    public int getMusic() throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(DIRACSOUND_PARAM_MUSIC, value));
        return value[0];
    }

    public void setHeadsetType(int type) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(DIRACSOUND_PARAM_HEADSET_TYPE, type));
    }

    public int getHeadsetType(int type) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        int[] arrayOfInt = new int[1];
        checkStatus(getParameter(DIRACSOUND_PARAM_HEADSET_TYPE, arrayOfInt));
        return arrayOfInt[0];
    }

    public void setScenario(int type) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        if( type < 1 || type > 4 ) type = 1;
        checkStatus(setParameter(DIRACSOUND_PARAM_SCENARIO, type));
    }

    public void setSurround(int type) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        if( type < 0 || type > 3 ) type = 1;
        checkStatus(setParameter(DIRACSOUND_PARAM_SURROUND, type));
    }

    public void setVoice(int type) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        if( type < 0 || type > 3 ) type = 1;
        checkStatus(setParameter(DIRACSOUND_PARAM_VOICE, type));
    }

    public void setLevel(int band, float level) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        Log.e("DiracEQ"," setLevel(" + band + "," + level +  ")");
        checkStatus(setParameter(new int[]{DIRACSOUND_PARAM_EQ_LEVEL, band},
                String.valueOf(level).getBytes()));
    }

    public float getLevel(int band) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[2];
        byte[] value = new byte[10];
        param[0] = DIRACSOUND_PARAM_EQ_LEVEL;
        param[1] = band;
        Log.e("DiracEQ"," getLevel(" + band + ")");
        checkStatus(getParameter(param, value));
        return new Float(new String(value)).floatValue();
    }

    public void setInt(String index, String value) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        Log.e("DiracEQ"," setInt(" + index + "," + value +  ")");

        try {
            checkStatus(setParameter(Integer.valueOf(index), Integer.valueOf(value)));
        } catch(Exception e) {
            Log.e("DiracEQ"," setInt exception: " + e);
        }
    }

    public void setString(String index, String value) throws IllegalStateException,
            IllegalArgumentException, UnsupportedOperationException {
        Log.e("DiracEQ"," setString(" + index + "," + value +  ")");

        try {
            checkStatus(setParameter(Integer.valueOf(index), String.valueOf(value).getBytes()));
        } catch(Exception e) {
            Log.e("DiracEQ"," setString exception: " + e);
        }
    }

}
