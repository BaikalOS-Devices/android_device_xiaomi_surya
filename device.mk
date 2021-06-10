#
# Copyright (C) 2021 Paranoid Android
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from sm6150-common
$(call inherit-product, device/xiaomi/sm6150-common/sm6150.mk)

# Inherit GSI keys
$(call inherit-product, $(SRC_TARGET_DIR)/product/gsi_keys.mk)

# Inherit the proprietary setup
$(call inherit-product, vendor/xiaomi/surya/surya-vendor.mk)

# Audio
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/configs/audio/,$(TARGET_COPY_OUT_VENDOR)/etc)

# Camera
PRODUCT_PACKAGES += \
    libmedia_jni_shim

# Device Settings
PRODUCT_PACKAGES += \
    XiaomiParts

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/parts/privapp-permissions-parts.xml:$(TARGET_COPY_OUT_PRODUCT)/etc/permissions/privapp-permissions-parts.xml

# Display
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/configs/display/,$(TARGET_COPY_OUT_VENDOR)/etc)

# Fastbootd
PRODUCT_PACKAGES += \
    fastbootd

# Fingerprint
PRODUCT_PACKAGES += \
    android.hardware.biometrics.fingerprint@2.1-service.xiaomi_sm6150

# Fstab
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/etc/fstab.qcom:$(TARGET_COPY_OUT_VENDOR)/etc/fstab.qcom \
    $(LOCAL_PATH)/rootdir/etc/fstab.qcom:$(TARGET_COPY_OUT_RAMDISK)/fstab.qcom

# IDC
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/configs/idc/,$(TARGET_COPY_OUT_SYSTEM)/usr/idc)

# Init scripts
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/etc/init.surya.rc:$(TARGET_COPY_OUT_VENDOR)/etc/init/init.surya.rc

# IR
PRODUCT_PACKAGES += \
    android.hardware.ir@1.0-impl \
    android.hardware.ir@1.0-service

PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.consumerir.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.consumerir.xml

# Keylayout
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/configs/keylayout/,$(TARGET_COPY_OUT_SYSTEM)/usr/keylayout)

# NFC
PRODUCT_PACKAGES += \
    com.android.nfc_extras \
    NfcNci \
    SecureElement \
    Tag

PRODUCT_PACKAGES += \
    android.hardware.nfc@1.2-service

PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/configs/nfc/,$(TARGET_COPY_OUT_VENDOR)/etc)

PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.nfc.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/android.hardware.nfc.xml \
    frameworks/native/data/etc/android.hardware.nfc.ese.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/android.hardware.nfc.ese.xml \
    frameworks/native/data/etc/android.hardware.nfc.hce.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/android.hardware.nfc.hce.xml \
    frameworks/native/data/etc/android.hardware.nfc.hcef.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/android.hardware.nfc.hcef.xml \
    frameworks/native/data/etc/android.hardware.nfc.uicc.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/android.hardware.nfc.uicc.xml \
    frameworks/native/data/etc/com.android.nfc_extras.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/com.android.nfc_extras.xml \
    frameworks/native/data/etc/com.nxp.mifare.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/sku_surya/com.nxp.mifare.xml

# Overlay
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    $(LOCAL_PATH)/overlay-baikalos

PRODUCT_PACKAGES += \
    KarnaFrameworks \
    NoCutoutOverlay \
    NotchBarKiller \
    NotchHideOverlay \
    NotchHideOverlaySystemUI
    
# Partitions
PRODUCT_BUILD_SUPER_PARTITION := false
PRODUCT_USE_DYNAMIC_PARTITIONS := true

# Sensors
PRODUCT_PACKAGES += \
    android.hardware.sensors@2.0-service.multihal \
    vendor.qti.hardware.display.mapper@1.1.vendor

# Shipping API level
PRODUCT_SHIPPING_API_LEVEL := 29

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH) \
    kernel/xiaomi/surya


# Iorap
PRODUCT_PACKAGES += \
    iorap-app-startup-runner

# Lights
PRODUCT_PACKAGES += \
    android.hardware.light@2.0-service.surya


# LiveDisplay
PRODUCT_PACKAGES += \
    vendor.lineage.livedisplay@2.0-service-sdm


# Init
PRODUCT_PACKAGES += \
    init.device.rc \
    init.custom.rc \
    init.profiles.rc \
    init.performance.rc \
    init.thermal.rc


# BaikalService 
PRODUCT_PRODUCT_PROPERTIES += \
    baikal.eng.perf=1 \
    baikal.eng.therm=1 \
    baikal.eng.perf.edit=1

# Baikal Effects
PRODUCT_PRODUCT_PROPERTIES += \
    persist.baikal.ae.disable=0 \
    persist.baikal.qcae.disable=0 \
    persist.baikal.dolby.enable=0 \
    persist.baikal.fps.default=4 \
    sys.baikal.dolby.avail=0 \
    sys.baikal.qcae.avail=1  \
    sys.baikal.reader=1 \
    sys.baikal.var_fps=1

# Baikal Maintainer
PRODUCT_PRODUCT_PROPERTIES += \
    ro.baikalos.maintainer=svasiliev22

PRODUCT_PRODUCT_PROPERTIES += \
    baikal.last.a2dp_codec=NONE \
    baikal.last.a2dp_bitrate=0

#Fully optimize on install
PRODUCT_PRODUCT_PROPERTIES += \
    pm.dexopt.install=speed-profile \
    pm.dexopt.bg-dexopt=speed-profile \
    pm.dexopt.boot=verify \
    pm.dexopt.first-boot=speed-profile \
    pm.dexopt.ab-ota=speed-profile \
    dalvik.vm.dex2oat-filter=speed-profile \
    dalvik.vm.image-dex2oat-filter=speed-profile \
    ro.vendor.qti.am.reschedule_service=true \
    ro.sys.fw.dex2oat_thread_count=8 \
    dalvik.vm.boot-dex2oat-threads=8 \
    dalvik.vm.dex2oat-threads=8 \
    dalvik.vm.image-dex2oat-threads=8 \
    dalvik.vm.dex2oat-cpu-set=0-8


PRODUCT_PRODUCT_PROPERTIES += \
    ro.control_privapp_permissions=log

#Vendor props
PRODUCT_PRODUCT_PROPERTIES += \
    ro.vendor.smart_dfps.enable=0 \
    persist.vendor.power.dfps.level=0

# IORap
PRODUCT_PROPERTY_OVERRIDES += \
    iorapd.perfetto.enable=true \
    iorapd.readahead.enable=true

# Perf
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.perf.scroll_opt=true

# Zygote
PRODUCT_PROPERTY_OVERRIDES += \
     persist.device_config.runtime_native.usap_pool_enabled=true


# Disable smart fps switching
PRODUCT_PRODUCT_PROPERTIES += \
    ro.vendor.smart_dfps.enable=false

# Enable inband ringing
PRODUCT_PRODUCT_PROPERTIES += \
    persist.bluetooth.disableinbandringing=false

# Enable blurs
PRODUCT_PRODUCT_PROPERTIES += \
    ro.surface_flinger.supports_background_blur=1 \
    ro.sf.blurs_are_expensive=1 \
    debug.sf.latch_unsignaled=1 \
    debug.sf.disable_backpressure=1

#    debug.hwui.renderer=skiavk \

# Graphics
PRODUCT_PRODUCT_PROPERTIES += \
    ro.surface_flinger.max_frame_buffer_acquired_buffers=3 \
    ro.surface_flinger.force_hwc_copy_for_virtual_displays=true \
    vendor.display.disable_rotator_downscale=1 \
    debug.sf.disable_backpressure=1 \
    debug.sf.latch_unsignaled=1 \
    debug.sf.show_predicted_vsync=1 \
    debug.sf.vsync_reactor=1 \
    debug.sf.use_frame_rate_priority=1 \
    debug.sf.use_frame_rate_api=1 \
    debug.sf.use_content_detection_v2=1 \
    persist.sys.sf.color_saturation=1.0 \
    ro.surface_flinger.set_display_power_timer_ms=5000 \
    ro.surface_flinger.set_idle_timer_ms=2000 \
    ro.surface_flinger.set_touch_timer_ms=1000 \
    ro.surface_flinger.use_content_detection_for_refresh_rate=true \
    vendor.display.disable_idle_time_hdr=1 \
    vendor.display.disable_idle_time_video=1 \
    vendor.display.idle_time=1100 \


# Camera
PRODUCT_PRODUCT_PROPERTIES += \
    persist.camera.privapp.list=org.codeaurora.snapcam,org.lineageos.snap,com.android.camera \
    persist.vendor.camera.privapp.list=org.codeaurora.snapcam,org.lineageos.snap,com.android.camera \
    vendor.camera.aux.packagelist=org.codeaurora.snapcam,org.lineageos.snap,com.android.camera \
    vendor.camera.aux.packagelist.ext=org.codeaurora.snapcam,org.lineageos.snap,com.android.camera \

