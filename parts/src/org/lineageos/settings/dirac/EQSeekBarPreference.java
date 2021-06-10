/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lineageos.settings.dirac;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import android.os.Handler;
import android.os.Message;

import android.util.Log;


import org.lineageos.settings.R;

public class EQSeekBarPreference extends Preference {

    public int minimum = -12;
    public int maximum = 12;
    public int def = 0;
    public int interval = 1;

    final int UPDATE = 0;

    int currentValue = def;

    private OnPreferenceChangeListener changer;

    public EQSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EQSeekBarPreference, 0, 0);

        minimum = typedArray.getInt(R.styleable.EQSeekBarPreference_min_value, minimum);
        maximum = typedArray.getInt(R.styleable.EQSeekBarPreference_max_value, maximum);
        def = typedArray.getInt(R.styleable.EQSeekBarPreference_default_value, def);

        typedArray.recycle();
        setPersistent(true);
       
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private void bind(final PreferenceViewHolder layout) {
        final SeekBar bar = (SeekBar) layout.findViewById(R.id.eq_seek_bar);

        bar.setMax(maximum);
        bar.setMin(minimum);
        bar.setProgress(currentValue);        

        Log.d("DiracEQ"," maximum=" + (maximum - minimum));
        Log.d("DiracEQ"," progress=" + (currentValue));

        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = Math.round(((float) progress) / interval) * interval;
                currentValue = progress;
                Log.d("DiracEQ"," progress=" + progress + ", currentValue=" + (progress));
                persistInt(progress);
                changer.onPreferenceChange(EQSeekBarPreference.this, currentValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                
            }
        });
    }

    @Override
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        changer = onPreferenceChangeListener;
        super.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder  view) {
        super.onBindViewHolder(view);
        bind(view);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        Log.d("DiracEQ"," restoreValue=" + restoreValue + ", defaultValue=" + defaultValue);
        setProgress(restoreValue ? getPersistedInt(currentValue)
                : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue (TypedArray a, int index) {
        return a.getInt(index, 0);
    }


    public int getProgress() {
        return currentValue;
    }

    public void setProgress(int progress) {
        Log.d("DiracEQ"," setProgress=" + progress);
        currentValue = progress;
        persistInt(progress);
        notifyChanged();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */

        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Save the instance state
        final SavedState myState = new SavedState(superState);
        myState.progress = currentValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        currentValue = myState.progress;
        notifyChanged();
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        public SavedState(Parcel source) {
            super(source);

            // Restore the click counter
            progress = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            // Save the click counter
            dest.writeInt(progress);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unused")
        public static final @android.annotation.NonNull Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


}
