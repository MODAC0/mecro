package com.mecro

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule

class MecroAccessibilityService : AccessibilityService() {

    companion object {
        var instance: MecroAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        sendStatusToReactNative(true)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        sendStatusToReactNative(false)
        return super.onUnbind(intent)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {
        sendStatusToReactNative(false)
    }

    private fun sendStatusToReactNative(isEnabled: Boolean) {
        val reactContext = MainApplication.getReactContext()
        if (reactContext != null && reactContext.hasActiveCatalystInstance()) {
            val params = Arguments.createMap()
            params.putBoolean("isEnabled", isEnabled)
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onAccessibilityStatusChanged", params)
        }
    }

    fun performClick(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        val builder = GestureDescription.Builder()
        // 100ms 동안 터치 유지 (일반적인 클릭)
        val gesture = builder.addStroke(GestureDescription.StrokeDescription(path, 0, 100)).build()
        dispatchGesture(gesture, null, null)
    }
}
