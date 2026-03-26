package com.mecro

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class FloatingOverlayModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "FloatingOverlay"

    @ReactMethod
    fun startOverlay() {
        if (!Settings.canDrawOverlays(reactApplicationContext)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactApplicationContext.packageName))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactApplicationContext.startActivity(intent)
        } else {
            val intent = Intent(reactApplicationContext, FloatingOverlayService::class.java)
            reactApplicationContext.startService(intent)
        }
    }

    @ReactMethod
    fun stopOverlay() {
        val intent = Intent(reactApplicationContext, FloatingOverlayService::class.java)
        reactApplicationContext.stopService(intent)
    }
}
