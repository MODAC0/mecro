package com.mecro

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import com.facebook.react.ReactApplication
import com.facebook.react.ReactRootView

class FloatingOverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var reactRootView: ReactRootView? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        reactRootView = ReactRootView(this)

        val reactInstanceManager = (application as ReactApplication).reactHost.getReactInstanceManager()
        reactRootView?.startReactApplication(reactInstanceManager, "FloatingOverlay", null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        windowManager?.addView(reactRootView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (reactRootView != null) {
            windowManager?.removeView(reactRootView)
            reactRootView?.unmountReactApplication()
            reactRootView = null
        }
    }
}
