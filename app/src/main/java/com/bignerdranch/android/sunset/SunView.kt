package com.bignerdranch.android.sunset

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.lang.Math.cos
import java.lang.Math.sin

class SunView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }
    private val radius = 100f // the radius of the sun
    private val rayLength = 150f // the length of the rays
    private val rayCount = 16 // the number of rays
    private val rayAngle = 360 / rayCount // the angle between rays

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        // draw the sun
        canvas.drawCircle(cx, cy, radius, paint)

        // draw the rays
        for (i in 0 until rayCount) {
            val angle = i * rayAngle
            val startX = cx + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val startY = cy + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            val stopX = cx + (radius + rayLength) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val stopY = cy + (radius + rayLength) * sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawLine(startX, startY, stopX, stopY, paint)
        }

    }
}