/*
 * Copyright (C) 2020 The LineageOS Project
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

package org.lineageos.settings;

import android.content.om.IOverlayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;

public class DevicePreferenceFragment extends PreferenceFragment {
    private static final String OVERLAY_NO_FILL_PACKAGE = "me.waveproject.overlay.notch.hide";
    private static final String OVERLAY_NO_FILL_PACKAGE_SYSTEMUI = "me.waveproject.overlay.notch.hide.systemui";

    private static final String KEY_HIDE_CAMERA_CUTOUT = "pref_hide_camera_cutout";
    private static final String KEY_DYNAMIC_FPS = "pref_dynamic_fps";
    private static final String KEY_BLUR = "pref_blur";
    private static final String KEY_GPU_SCALE = "pref_gpu_scale";

    private IOverlayManager mOverlayService;

    //private ListPreference mPrefMinRefreshRate;
    private SwitchPreference mPrefHideCameraCutout;
    //private SwitchPreference mPrefDynamicFps;
    private SwitchPreference mPrefBlur;
    private SwitchPreference mPrefGpuScale;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        mOverlayService = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.device_prefs);
        mPrefHideCameraCutout = (SwitchPreference) findPreference(KEY_HIDE_CAMERA_CUTOUT);
        mPrefHideCameraCutout.setOnPreferenceChangeListener(PrefListener);

        //Boolean param = SystemProperties.get("persist.vendor.smart_dfps","0").equals("1");
        //mPrefDynamicFps = (SwitchPreference) findPreference(KEY_DYNAMIC_FPS);
        //mPrefDynamicFps.setOnPreferenceChangeListener(PrefListener);

        mPrefBlur = (SwitchPreference) findPreference(KEY_BLUR);
        mPrefBlur.setOnPreferenceChangeListener(PrefListener);

        mPrefGpuScale = (SwitchPreference) findPreference(KEY_GPU_SCALE);
        mPrefGpuScale.setOnPreferenceChangeListener(PrefListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mPrefHideCameraCutout.setChecked(
                    mOverlayService.getOverlayInfo(OVERLAY_NO_FILL_PACKAGE, 0).isEnabled()
                    || mOverlayService.getOverlayInfo(OVERLAY_NO_FILL_PACKAGE_SYSTEMUI, 0).isEnabled());

            //mPrefDynamicFps.setChecked(SystemProperties.get("persist.vendor.smart_dfps","0").equals("1"));
            mPrefBlur.setChecked(SystemProperties.get("persist.sys.sf.disable_blurs","0").equals("0"));
            mPrefGpuScale.setChecked(SystemProperties.get("persist.baikal.gpuscale","0").equals("1"));
        } catch (RemoteException e) {
            // We can do nothing
        }
    }

    private final Preference.OnPreferenceChangeListener PrefListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    final String key = preference.getKey();

                    if (KEY_DYNAMIC_FPS.equals(key)) {
                        if( (boolean) value ) {
                            SystemProperties.set("persist.vendor.smart_dfps", "1" );
                        } else {
                            SystemProperties.set("persist.vendor.smart_dfps", "0" );
                        }
                    } else if (KEY_BLUR.equals(key)) {
                        if( (boolean) value ) {
                            SystemProperties.set("persist.sys.sf.disable_blurs", "0" );
                        } else {
                            SystemProperties.set("persist.sys.sf.disable_blurs", "1" );
                        }
                    } else if (KEY_GPU_SCALE.equals(key)) {
                        if( (boolean) value ) {
                            SystemProperties.set("persist.baikal.gpuscale", "1" );
                        } else {
                            SystemProperties.set("persist.baikal.gpuscale", "0" );
                        }
                    } else if (KEY_HIDE_CAMERA_CUTOUT.equals(key)) {
                        try {
                            mOverlayService.setEnabled(
                                    OVERLAY_NO_FILL_PACKAGE, (boolean) value, 0);
                            mOverlayService.setEnabled(
                                    OVERLAY_NO_FILL_PACKAGE_SYSTEMUI, (boolean) value, 0);
                        } catch (RemoteException e) {
                            // We can do nothing
                        }
                        Toast.makeText(getContext(),
                                R.string.msg_device_need_restart, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            };
}
