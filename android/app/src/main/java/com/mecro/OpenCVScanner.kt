package com.mecro

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class OpenCVScanner {
    fun findImageCenter(screen: Bitmap, template: Bitmap, threshold: Double = 0.8): Point? {
        val imgScene = Mat()
        val imgTemplate = Mat()
        
        Utils.bitmapToMat(screen, imgScene)
        Utils.bitmapToMat(template, imgTemplate)

        // 그레이스케일로 변환
        Imgproc.cvtColor(imgScene, imgScene, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(imgTemplate, imgTemplate, Imgproc.COLOR_BGR2GRAY)

        val result = Mat()
        val resultCols = imgScene.cols() - imgTemplate.cols() + 1
        val resultRows = imgScene.rows() - imgTemplate.rows() + 1
        
        if (resultCols <= 0 || resultRows <= 0) return null

        result.create(resultRows, resultCols, CvType.CV_32FC1)

        // 템플릿 매칭
        Imgproc.matchTemplate(imgScene, imgTemplate, result, Imgproc.TM_CCOEFF_NORMED)

        val mmres = Core.minMaxLoc(result)
        val matchLoc: Point = mmres.maxLoc
        val maxVal = mmres.maxVal

        if (maxVal < threshold) {
            return null
        }

        val centerX = matchLoc.x + (imgTemplate.cols() / 2)
        val centerY = matchLoc.y + (imgTemplate.rows() / 2)

        imgScene.release()
        imgTemplate.release()
        result.release()

        return Point(centerX, centerY)
    }
}
