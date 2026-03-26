package com.mecro

import android.graphics.BitmapFactory
import com.facebook.react.bridge.*
import org.opencv.android.OpenCVLoader
import java.io.File

class MecroImageModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    
    init {
        if (!OpenCVLoader.initDebug()) {
            println("OpenCV initialization failed.")
        }
    }

    override fun getName(): String = "MecroImage"

    @ReactMethod
    fun findTargetImage(templatePath: String, threshold: Double, promise: Promise) {
        try {
            val screenBitmap = MecroForegroundService.latestBitmap
            
            val file = File(templatePath)
            if (!file.exists()) {
                 promise.reject("ERROR", "Template image not found at path: $templatePath")
                 return
            }
            val templateBitmap = BitmapFactory.decodeFile(templatePath)

            if (screenBitmap == null || templateBitmap == null) {
                promise.reject("ERROR", "Screen or template image is null. Make sure MediaProjection is running.")
                return
            }

            val scanner = OpenCVScanner()
            val centerPoint = scanner.findImageCenter(screenBitmap, templateBitmap, threshold)

            if (centerPoint != null) {
                val result = Arguments.createMap().apply {
                    putDouble("x", centerPoint.x)
                    putDouble("y", centerPoint.y)
                    putBoolean("found", true)
                }
                promise.resolve(result)
            } else {
                promise.resolve(Arguments.createMap().apply {
                    putBoolean("found", false)
                })
            }
        } catch (e: Exception) {
            promise.reject("SCAN_FAILED", e.message)
        }
    }
}
