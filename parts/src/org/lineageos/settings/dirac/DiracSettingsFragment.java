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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;

import org.lineageos.settings.R;

public class DiracSettingsFragment extends PreferenceFragment
        implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private static final String PREF_HEADSET = "dirac_headset_pref";
    private static final String PREF_PRESET = "dirac_preset_pref";
    private static final String PREF_SCENARIO = "dirac_scenario_pref";

    private static final String PREF_SURROUND = "dirac_surround_level";
    private static final String PREF_VOICE = "dirac_voice_level";

    private static final String PREF_SEND_INT = "baikalos_sendint";
    private static final String PREF_SEND_STRING = "baikalos_sendstring";

    private static final String KEY_DIRAC_EQ_BAND = "dirac_eq_band_";

    private Preference mSendInt;
    private Preference mSendSendString;

    private PreferenceCategory mEqCategory;
    private EQSeekBarPreference mEqBand0;
    private EQSeekBarPreference mEqBand1;
    private EQSeekBarPreference mEqBand2;
    private EQSeekBarPreference mEqBand3;
    private EQSeekBarPreference mEqBand4;
    private EQSeekBarPreference mEqBand5;
    private EQSeekBarPreference mEqBand6;

    private int[] mCustomEq = { 0, 0, 0, 0, 0, 0, 0 };

    boolean mIsCustomEq = false;

    private TextView mTextView;
    // private View mSwitchBar;

    private ListPreference mHeadsetType;
    private ListPreference mPreset;
    private ListPreference mScenario;
    private Preference mSurround;
    private Preference mVoice;
    private boolean mEnhancerEnabled;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.dirac_settings);
        /// final ActionBar actionBar = getActivity().getActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(true);

        try {
            DiracUtils.initialize(getActivity());
            DiracUtils.setMusic(true);
            DiracUtils.setMovie(true);
            mEnhancerEnabled = true;
        } catch (Exception x) {
            mEnhancerEnabled = false;
        }

        boolean isDebug = SystemProperties.get("persist.baikalos.opt.debug", "0").equals("1");

        mSendInt = (Preference) findPreference(PREF_SEND_INT);
        if (mSendInt != null) {
            if (isDebug) {
                mSendInt.setOnPreferenceClickListener(this);
            } else {
                mSendInt.setVisible(false);
            }
        }
        mSendSendString = (Preference) findPreference(PREF_SEND_STRING);
        if (mSendSendString != null) {
            if (isDebug) {
                mSendSendString.setOnPreferenceClickListener(this);
            } else {
                mSendSendString.setVisible(false);
            }
        }

        mHeadsetType = (ListPreference) findPreference(PREF_HEADSET);
        if (mHeadsetType != null) {
            mHeadsetType.setOnPreferenceChangeListener(this);
            mHeadsetType.setEnabled(mEnhancerEnabled);
        }

        mPreset = (ListPreference) findPreference(PREF_PRESET);
        if (mPreset != null) {
            mPreset.setOnPreferenceChangeListener(this);
            mPreset.setEnabled(mEnhancerEnabled);
        }

        mScenario = (ListPreference) findPreference(PREF_SCENARIO);
        if (mScenario != null) {
            mScenario.setOnPreferenceChangeListener(this);
            mScenario.setEnabled(mEnhancerEnabled);
        }

        mSurround = (Preference) findPreference(PREF_SURROUND);
        if (mSurround != null) {
            mSurround.setOnPreferenceChangeListener(this);
            mSurround.setEnabled(mEnhancerEnabled);
        }

        mVoice = (Preference) findPreference(PREF_VOICE);
        if (mVoice != null) {
            mVoice.setOnPreferenceChangeListener(this);
            mVoice.setEnabled(mEnhancerEnabled);
        }

        mEqCategory = (PreferenceCategory) findPreference("dirac_eq_settings");

        if (mEqCategory != null) {

            if (mEnhancerEnabled) {
                mEqBand0 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "0");
                mEqBand1 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "1");
                mEqBand2 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "2");
                mEqBand3 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "3");
                mEqBand4 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "4");
                mEqBand5 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "5");
                mEqBand6 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "6");

                mEqBand0.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(0," + newValue + ")");

                        DiracUtils.setLevel(0, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(1," + newValue + ")");

                        DiracUtils.setLevel(1, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(2," + newValue + ")");

                        DiracUtils.setLevel(2, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(3," + newValue + ")");

                        DiracUtils.setLevel(3, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(4," + newValue + ")");

                        DiracUtils.setLevel(4, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(5," + newValue + ")");

                        DiracUtils.setLevel(5, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

                mEqBand6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.e("DiracEQ", " setValue(6," + newValue + ")");

                        DiracUtils.setLevel(6, (int) newValue);
                        updateCustomEq();
                        return true;
                    }
                });

            }

            mEqCategory.setEnabled(mEnhancerEnabled);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dirac, container, false);
        ((ViewGroup) view).addView(super.onCreateView(inflater, container, savedInstanceState));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean enhancerEnabled = true; // DiracUtils.isDiracEnabled();

        // mTextView = view.findViewById(R.id.switch_text);
        // mTextView.setText(getString(enhancerEnabled ? R.string.switch_bar_on :
        // R.string.switch_bar_off));

        // mSwitchBar = view.findViewById(R.id.switch_bar);
        // Switch switchWidget = mSwitchBar.findViewById(android.R.id.switch_widget);
        // switchWidget.setChecked(enhancerEnabled);
        // switchWidget.setOnCheckedChangeListener(this);
        // mSwitchBar.setActivated(enhancerEnabled);
        // mSwitchBar.setOnClickListener(v -> {
        // switchWidget.setChecked(!switchWidget.isChecked());
        // mSwitchBar.setActivated(switchWidget.isChecked());
        // });

        /*
         * setEqLevels(SystemProperties.get("persist.dirac.eq","0,0,0,0,0,0,0"));
         * 
         * if( SystemProperties.get("persist.dirac.custom","0").equals("1") ){
         * mPreset.setValue("manual"); mIsCustomEq = true; mEqCategory.setEnabled(true);
         * DiracUtils.setLevel(SystemProperties.get("persist.dirac.eq","0,0,0,0,0,0,0"))
         * ; } else { try { Log.e("DiracEQ"," onViewCreated() mPreset.setValue");
         * mPreset.setValue(DiracUtils.getLevel()); mIsCustomEq = false;
         * mEqCategory.setEnabled(false);
         * SystemProperties.set("persist.dirac.custom","0"); } catch (Exception e) {
         * Log.e("DiracEQ"," onViewCreated() exception:", e); mIsCustomEq = false;
         * mEqCategory.setEnabled(false);
         * SystemProperties.set("persist.dirac.custom","0"); } }
         */

        if (mEnhancerEnabled)
            return;

        if (mEqCategory != null) {
            if (mPreset != null) {
                mEqCategory.setEnabled(
                        mEnhancerEnabled && mPreset.getValue() != null && mPreset.getValue().equals("manual"));
            } else {
                mEqCategory.setEnabled(false);
            }
        }

        if (mScenario != null && mSurround != null && mVoice != null) {
            if (mScenario.getValue() != null) {
                try {
                    int scenario = Integer.valueOf(mScenario.getValue());
                    if (scenario == 2 || scenario == 4) {
                        mSurround.setEnabled(mEnhancerEnabled & true);
                        mVoice.setEnabled(mEnhancerEnabled & true);
                    } else {
                        mSurround.setEnabled(false);
                        mVoice.setEnabled(false);
                    }
                    if( scenario < 1 || scenario > 4 ) {
                        mScenario.setValue("1");        
                    }
                } catch (Exception ex) {
                    mScenario.setValue("1");
                }
            }
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.e("DiracEQ", " onPreferenceClick(" + preference.getKey());
        switch (preference.getKey()) {
        case PREF_SEND_INT:
            DiracUtils.setTestInt(SystemProperties.get("test.dirac.index", "0"),
                    SystemProperties.get("test.dirac.int", "0"));
            return true;

        case PREF_SEND_STRING:
            DiracUtils.setTestString(SystemProperties.get("test.dirac.index", "0"),
                    SystemProperties.get("test.dirac.str", "0"));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Log.e("DiracEQ", " onPreferenceChange(" + preference.getKey() + "," + newValue + ")");

        switch (preference.getKey()) {

        case PREF_SURROUND:
            DiracUtils.setSurround(Integer.parseInt(newValue.toString()));
            return true;

        case PREF_VOICE:
            DiracUtils.setVoice(Integer.parseInt(newValue.toString()));
            return true;

        case PREF_SEND_INT:
            DiracUtils.setTestInt(SystemProperties.get("test.dirac.index", "0"),
                    SystemProperties.get("test.dirac.int", "0"));
            return true;

        case PREF_SEND_STRING:
            DiracUtils.setTestString(SystemProperties.get("test.dirac.index", "0"),
                    SystemProperties.get("test.dirac.str", "0"));
            return true;

        case PREF_SCENARIO: {
            int scenario = Integer.parseInt(newValue.toString());
            DiracUtils.setScenario(Integer.parseInt(newValue.toString()));
            if (scenario == 2 || scenario == 4) {
                mSurround.setEnabled(true);
                mVoice.setEnabled(true);
            } else {
                mSurround.setEnabled(false);
                mVoice.setEnabled(false);
            }
        }
            return true;
        case PREF_HEADSET:
            DiracUtils.setHeadsetType(Integer.parseInt(newValue.toString()));
            return true;
        case PREF_PRESET:
            if (newValue.equals("manual")) {
                mIsCustomEq = true;
                mEqCategory.setEnabled(true);
                DiracUtils.setLevel(getEqLevels());
                SystemProperties.set("persist.dirac.custom", "1");
            } else {
                mIsCustomEq = true;
                mEqCategory.setEnabled(false);
                DiracUtils.setLevel((String) newValue);
                SystemProperties.set("persist.dirac.custom", "0");
            }
            return true;
        default:
            return false;
        }
    }

    // @Override
    // public void onCheckedChanged(CompoundButton compoundButton, boolean
    // isChecked) {
    // mTextView.setText(getString(isChecked ? R.string.switch_bar_on :
    // R.string.switch_bar_off));
    // mSwitchBar.setActivated(isChecked);

    // DiracUtils.setMusic(isChecked);
    // mHeadsetType.setEnabled(isChecked);
    // mPreset.setEnabled(isChecked);
    // mScenario.setEnabled(isChecked);
    // mEqCategory.setEnabled(isChecked);
    // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    private String getEqLevels() {
        return mEqBand0.getProgress() + "," + mEqBand1.getProgress() + "," + mEqBand2.getProgress() + ","
                + mEqBand3.getProgress() + "," + mEqBand4.getProgress() + "," + mEqBand5.getProgress() + ","
                + mEqBand6.getProgress();
    }

    private void setEqLevels(String preset) {
        String[] level = preset.split("\\s*,\\s*");

        mEqBand0.setProgress(Integer.valueOf(level[0]));
        mEqBand1.setProgress(Integer.valueOf(level[1]));
        mEqBand2.setProgress(Integer.valueOf(level[2]));
        mEqBand3.setProgress(Integer.valueOf(level[3]));
        mEqBand4.setProgress(Integer.valueOf(level[4]));
        mEqBand5.setProgress(Integer.valueOf(level[5]));
        mEqBand6.setProgress(Integer.valueOf(level[6]));

    }

    private void updateCustomEq() {
        // SystemProperties.set("persist.dirac.eq",DiracUtils.getLevel());
    }

}
