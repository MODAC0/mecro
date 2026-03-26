package com.mecro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.facebook.react.bridge.*

class MecroPermissionModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "MecroPermission"

    @ReactMethod
    fun requestBatteryOptimizationExemption() {
        val packageName = reactApplicationContext.packageName
        val pm = reactApplicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactApplicationContext.startActivity(intent)
        }
    }

    @ReactMethod
    fun checkAccessibilityPermission(promise: Promise) {
        promise.resolve(MecroAccessibilityService.instance != null)
    }

    @ReactMethod
    fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    @ReactMethod
    fun requestMediaProjection() {
        val currentActivity = currentActivity
        if (currentActivity is MainActivity) {
            currentActivity.requestMediaProjectionPermission()
        }
    }

    @ReactMethod
    fun performClick(x: Float, y: Float, promise: Promise) {
        if (MecroAccessibilityService.instance != null) {
            MecroAccessibilityService.instance?.performClick(x, y)
            promise.resolve(true)
        } else {
            promise.reject("ACCESSIBILITY_NOT_ENABLED", "Accessibility service is not running. Please enable it in settings.")
        }
    }
}
