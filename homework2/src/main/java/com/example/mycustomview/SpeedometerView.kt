package com.example.mycustomview

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class SpeedometerView(context: Context, attrs: AttributeSet?): View(context, attrs) {

    companion object {
        const val STATE_DIGIT_COLOR = "digitColor"
        const val STATE_MAX_SPEED = "maxSpeed"
        const val STATE_CURRENT_SPEED = "currentSpeed"
        const val STATE_SEGMENTS_AMOUNT = "segmentsAmount"
        const val STATE_POINTER_COLOR = "pointerColor"
        const val STATE_SEGMENT_COLOR = "segmentColor"
        const val STATE_BACKGROUND_COLOR = "backgroundColor"
        const val STATE_SUPER = "superState"

        private const val DEFAULT_MAX_SPEED = 200F
        private const val DEFAULT_CURRENT_SPEED = 0F
        private const val DEFAULT_SEGMENT_COLOR = Color.CYAN
        private const val DEFAULT_DIGIT_COLOR = Color.BLUE
        private const val DEFAULT_POINTER_COLOR = Color.RED
        private const val DEFAULT_BACKGROUND_COLOR = Color.WHITE
        private const val DEFAULT_SEGMENT_AMOUNT = 5
        private const val DEFAULT_WIDTH = 250
        private const val DEFAULT_HEIGHT = 250
    }

    private var speedStep: Float
    var maxSpeed: Float = DEFAULT_MAX_SPEED
        private set(value) {
            field = (value / blockOfSegmentsAmount) * blockOfSegmentsAmount
        }
    var curSpeed: Float = DEFAULT_CURRENT_SPEED
        private set(value) {
            field = value.coerceAtMost(maxSpeed)
        }

    var background: Int = DEFAULT_BACKGROUND_COLOR
        private set
    var pointerColor: Int = DEFAULT_POINTER_COLOR
        private set
    private var segmentColor: Int
    private var digitColor: Int

    private var blockOfSegmentsAmount: Int
    private var segmentsPerBlockAmount = 9
    private val segmentsDegreeStep: Float
    private val bigSegmentLength = 18F
    private val smallSegmentLength = 10F
    private val boldStrokeWidth = 5F
    private val normalStrokeWidth = 3F

    private val defaultPaint = Paint()
    private val smallSegmentsPaint = Paint()
    private val bigSegmentsPaint = Paint()
    private val digitPaint = Paint()
    private val smallSegmentsPath = Path()
    private val bigSegmentsPath = Path()

    private val digitsCoordinates = HashMap<String, Pair<Float, Float>>()

    private var arcRadius: Float = (width / 2F)
    private val paddingFromArc = 15F
    private val arcRectF = RectF()

    init {
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.SpeedometerView)
        attrsArray.apply {
            blockOfSegmentsAmount = getInteger(R.styleable.SpeedometerView_segmentationAmount, DEFAULT_SEGMENT_AMOUNT)
            maxSpeed = getFloat(R.styleable.SpeedometerView_maxSpeed, DEFAULT_MAX_SPEED)
            segmentColor = getColor(R.styleable.SpeedometerView_segmentationColor, DEFAULT_SEGMENT_COLOR)
            digitColor = getColor(R.styleable.SpeedometerView_digitColor, DEFAULT_DIGIT_COLOR)
            curSpeed = getFloat(R.styleable.SpeedometerView_currentSpeed, DEFAULT_CURRENT_SPEED)
            pointerColor = getInteger(R.styleable.SpeedometerView_pointerColor, DEFAULT_POINTER_COLOR)
            background = getInteger(R.styleable.SpeedometerView_backgroundColor, DEFAULT_BACKGROUND_COLOR)
        }
        segmentsDegreeStep = 180F / (segmentsPerBlockAmount * blockOfSegmentsAmount)
        speedStep = maxSpeed / blockOfSegmentsAmount

        defaultPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = normalStrokeWidth
        }

        smallSegmentsPaint.apply {
            color = segmentColor
            style = Paint.Style.STROKE
            strokeWidth = normalStrokeWidth
        }

        bigSegmentsPaint.apply {
            color = digitColor
            style = Paint.Style.STROKE
            strokeWidth = boldStrokeWidth
        }

        digitPaint.apply {
            color = digitColor
            style = Paint.Style.STROKE
            strokeWidth = normalStrokeWidth
            textSize = 30F
            textAlign = Paint.Align.CENTER
        }

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measureDimension(DEFAULT_WIDTH, widthMeasureSpec)
        val height = measureDimension(DEFAULT_HEIGHT, widthMeasureSpec)

        val size = min(width, height)

        setMeasuredDimension(size, size / 2 + defaultPaint.strokeWidth.toInt())
    }

    private fun measureDimension(minSize: Int, measureSpec: Int): Int {
        val sizeSpec = MeasureSpec.getSize(measureSpec)
        val modeSpec = MeasureSpec.getMode(measureSpec)

        return when(modeSpec) {
            MeasureSpec.EXACTLY -> sizeSpec
            MeasureSpec.AT_MOST -> minSize.coerceAtMost(sizeSpec)
            else -> minSize
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        arcRectF.set(
                defaultPaint.strokeWidth,
                defaultPaint.strokeWidth,
                width.toFloat() - defaultPaint.strokeWidth,
                width.toFloat())

        arcRadius = width / 2F

        var speedCounter = 0F
        var currentAngle = 0F
        for (blockIndex in 0..blockOfSegmentsAmount) {
            val (speed, coordinates) = calcSpeedDigitsXY(currentAngle, speedCounter.toInt())
            digitsCoordinates[speed] = coordinates
            var segmentCounter = 0
            do {
                if (segmentCounter == 0) {
                    calcSegmentAndAddToPath(bigSegmentsPath, currentAngle, bigSegmentLength)
                }
                else {
                    calcSegmentAndAddToPath(smallSegmentsPath, currentAngle, smallSegmentLength)
                }

                currentAngle += segmentsDegreeStep
                segmentCounter++
            } while (blockIndex != blockOfSegmentsAmount && segmentCounter < segmentsPerBlockAmount)

            speedCounter += speedStep
        }
    }

    private fun calcSpeedDigitsXY(angle: Float, speed: Int): Pair<String, Pair<Float, Float>> {
        val text = speed.toString()
        val (digitX, digitY) =
                getPointOnCircle(
                        radius = arcRadius - 3 * paddingFromArc - bigSegmentLength - defaultPaint.measureText(text) / 2,
                        angle = angle)

        return Pair(text, Pair(digitX, digitY))
    }

    private fun calcSegmentAndAddToPath(path: Path, angle: Float, segmentLength: Float) {
        val (fromX, fromY) =
                getPointOnCircle(
                        radius = arcRadius - paddingFromArc,
                        angle = angle)
        val (toX, toY) =
                getPointOnCircle(
                        radius = arcRadius - paddingFromArc - segmentLength,
                        angle = angle)

        path.moveTo(fromX, fromY)
        path.lineTo(toX, toY)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        defaultPaint.strokeWidth = normalStrokeWidth
        drawArc(canvas, Paint.Style.FILL, background)
        drawArc(canvas, Paint.Style.STROKE, segmentColor)

        canvas?.drawPath(smallSegmentsPath, smallSegmentsPaint)
        canvas?.drawPath(bigSegmentsPath, bigSegmentsPaint)

        digitsCoordinates.forEach {
            canvas?.drawText(it.key, it.value.first, it.value.second, digitPaint)
        }

        drawPointer(canvas)
    }

    private fun drawArc(canvas: Canvas?, paintStyle: Paint.Style, paintColor: Int) {
        defaultPaint.apply {
            style = paintStyle
            color = paintColor
        }
        canvas?.drawArc(arcRectF, 0F, -180F, false, defaultPaint)
    }

    private fun drawPointer(canvas: Canvas?) {
        defaultPaint.apply {
            color = pointerColor
            strokeWidth = boldStrokeWidth
        }
        val currentSpeedCircleAngle = curSpeed / (maxSpeed / 180F)

        val (toX, toY) =
                getPointOnCircle(
                        radius = arcRadius - paddingFromArc,
                        angle = currentSpeedCircleAngle)

        canvas?.drawLine(arcRadius, arcRadius, toX, toY, defaultPaint)
    }

    fun setCurrentSpeed(speed: Float) {
        curSpeed = speed
        invalidate()
    }

    fun setSpeedometerBackgroundColor(color: Int) {
        background = color
        invalidate()
    }

    fun setPointerColor(color: Int) {
        pointerColor = color
        invalidate()
    }

    private fun getPointOnCircle(radius: Float, angle: Float, centreX: Float = arcRadius, centreY: Float = arcRadius) =
            Pair(centreX + radius * -cos(Math.toRadians(angle.toDouble())).toFloat(),
                    centreY + radius * -sin(Math.toRadians(angle.toDouble())).toFloat())

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putFloat(STATE_MAX_SPEED, maxSpeed)
            putFloat(STATE_CURRENT_SPEED, curSpeed)
            putInt(STATE_SEGMENTS_AMOUNT, segmentsPerBlockAmount)
            putInt(STATE_DIGIT_COLOR, digitColor)
            putInt(STATE_POINTER_COLOR, pointerColor)
            putInt(STATE_SEGMENT_COLOR, segmentColor)
            putInt(STATE_BACKGROUND_COLOR, background)
            putParcelable(STATE_SUPER, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        state?.let{
            if (state is Bundle) {
                maxSpeed = state.getFloat(STATE_MAX_SPEED)
                curSpeed = state.getFloat(STATE_CURRENT_SPEED)
                segmentsPerBlockAmount = state.getInt(STATE_SEGMENTS_AMOUNT)
                digitColor = state.getInt(STATE_DIGIT_COLOR)
                pointerColor = state.getInt(STATE_POINTER_COLOR)
                segmentColor = state.getInt(STATE_SEGMENT_COLOR)
                background = state.getInt(STATE_BACKGROUND_COLOR)
                superState = state.getParcelable(STATE_SUPER)
            }
        }
        super.onRestoreInstanceState(superState)
    }
}