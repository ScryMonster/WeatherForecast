package com.example.weatherforecast.utils.customView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.weatherforecast.R

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DoubleProgressView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttributeSet: Int = 0,
                                                   defStyleRes: Int = 0) : View(context, attrs, defStyleAttributeSet, defStyleRes) {

    init {
        init(attrs,defStyleRes)
    }

    private val INDETERMINANT_MIN_SWEEP= 15f

    private lateinit var mPaint: Paint
    private lateinit var mRect: RectF

    private var mSize: Int = 0
    private var mIndeterminateSweep: Float = 0f
    private var mStartAngle: Int = 0
    private var mIndeterminateRotateOffset: Float = 0f

    private lateinit var mPaintInner: Paint
    private lateinit var mPaintOuter: Paint
    private var mThickness = 0
    private var mInnerPadding = 55f
    private var mAnimDuration = 0
    private lateinit var mInnerSquare: RectF
    private lateinit var mOuterCircle: RectF
    @ColorInt
    private var mOuterCircleColor: Int = 0
    @ColorInt
    private var mInnerSquareColor: Int = 0
    private var mSteps = 0

    private var mIntermediateAnimator: AnimatorSet? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(mOuterCircle, mStartAngle + mIndeterminateRotateOffset, mIndeterminateSweep, false, mPaintOuter)
        canvas?.drawArc(mInnerSquare, mStartAngle + mIndeterminateRotateOffset + 180,mIndeterminateSweep,false,mPaintInner)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val xPad = paddingLeft + paddingRight
        val yPad = paddingTop + paddingBottom
        val width = measuredWidth - xPad
        val height = measuredHeight - yPad
        mSize = if (width < height) width else height
        setMeasuredDimension(mSize + xPad, mSize + yPad)
        updateRectAngleBounds()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSize = if (w < h) w else h
        updateRectAngleBounds()
    }

    private fun updateRectAngleBounds() {
        mOuterCircle.set(
            (paddingLeft + mThickness).toFloat(),
            (paddingTop + mThickness).toFloat(),
            (mSize - paddingLeft - mThickness).toFloat(),
            (mSize - paddingTop - mThickness).toFloat()
        )
        mInnerSquare.set(
            (paddingLeft + mThickness + mInnerPadding),
            (paddingTop + mThickness + mInnerPadding),
            (mSize - paddingLeft - mThickness - mInnerPadding),
            (mSize - paddingTop - mThickness - mInnerPadding)
        )
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun setVisibility(visibility: Int) {
        val currentVisibility = getVisibility()
        super.setVisibility(visibility)
        if (visibility != currentVisibility){
            if (visibility == VISIBLE){
                resetAnimation()
            }
            else if(visibility == GONE || visibility == INVISIBLE){
                stopAnimation()
            }
        }

    }


    private fun init(attrs: AttributeSet?, defStyleRes: Int?) {



        mPaintOuter = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintInner = Paint(Paint.ANTI_ALIAS_FLAG)

        mInnerSquare = RectF()
        mOuterCircle = RectF()

        val res = resources

        val array = context.obtainStyledAttributes(attrs, R.styleable.DoubleProgressView, defStyleRes!!, 0)
        mThickness = array.getDimensionPixelSize(R.styleable.DoubleProgressView_thickness, res.getDimensionPixelSize(R.dimen.default_thickness))
        mOuterCircleColor = array.getColor(R.styleable.DoubleProgressView_outerColor, ContextCompat.getColor(context,R.color.windows_blue))
        mInnerSquareColor = array.getColor(R.styleable.DoubleProgressView_innerColor, ContextCompat.getColor(context,R.color.soft_green))
        mAnimDuration = array.getInteger(R.styleable.DoubleProgressView_anim_duration, res.getInteger(R.integer.default_anim_duration))
        mSteps = res.getInteger(R.integer.default_anim_step)
        mStartAngle = res.getInteger(R.integer.default_start_angle)
        array.recycle()
        setPaint()
    }


    private fun setPaint() {
        mPaintOuter.apply {
            color = mOuterCircleColor
            style = Paint.Style.STROKE
            strokeWidth = mThickness.toFloat()
            strokeCap = Paint.Cap.BUTT

        }
        mPaintInner.apply {
            color = mInnerSquareColor
            style = Paint.Style.STROKE
            strokeWidth = mThickness.toFloat()
            strokeCap = Paint.Cap.BUTT
        }
    }



    private fun createIndeterminateAnimator(step: Float) : AnimatorSet{
        val maxSweep = 360f *(mSteps - 1) / mSteps + INDETERMINANT_MIN_SWEEP
        val start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP)

        val fronEnd = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP,maxSweep).apply {
            duration = mAnimDuration / mSteps / 2L
            interpolator = DecelerateInterpolator(1f)
            addUpdateListener { animation->
                mIndeterminateSweep = animation.animatedValue as Float
                invalidate()
            }
        }

        val rotateAnimatorOuter = ValueAnimator.ofFloat(step * 720f/mSteps,(step + .5f) * 720f /mSteps).apply {
            duration = mAnimDuration / mSteps / 2L
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                mIndeterminateRotateOffset = animation.animatedValue as Float
            }
        }


        val backEnd = ValueAnimator.ofFloat(start,start + maxSweep - INDETERMINANT_MIN_SWEEP).apply {
            duration = mAnimDuration / mSteps / 2L
            interpolator = DecelerateInterpolator(1f)
            addUpdateListener { animation->
                mStartAngle = (animation.animatedValue as Float).toInt()
                mIndeterminateSweep =  maxSweep - mStartAngle + start
                invalidate()
            }
        }

        val rotateAnimatorInner = ValueAnimator.ofFloat((step + .5f) * 720f / mSteps, (step + 1) * 720f / mSteps).apply {
            duration = mAnimDuration / mSteps / 2L
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                mIndeterminateRotateOffset = animation.animatedValue as Float
            }
        }

        return AnimatorSet().apply {
            play(fronEnd).with(rotateAnimatorOuter)
            play(backEnd).with(rotateAnimatorInner).after(rotateAnimatorOuter)
        }
    }

    private fun resetAnimation(){
        if (mIntermediateAnimator != null && mIntermediateAnimator?.isRunning!!){
            mIntermediateAnimator?.cancel()
        }
        mIndeterminateSweep = INDETERMINANT_MIN_SWEEP

        mIntermediateAnimator = AnimatorSet()
        var prevSet : AnimatorSet? = null
        var nextSet : AnimatorSet? = null
        var k = 0
        while (k<mSteps){
            nextSet = createIndeterminateAnimator(k.toFloat())
            val builder = mIntermediateAnimator?.play(nextSet)
            if (prevSet != null){
                builder?.after(prevSet)
            }
            prevSet = nextSet
            k++
        }
        mIntermediateAnimator?.addListener(object :AnimatorListenerAdapter(){
            var wasCanceled = false
            override fun onAnimationEnd(animation: Animator?) {
                if (!wasCanceled) resetAnimation()
            }

            override fun onAnimationCancel(animation: Animator?) {
                wasCanceled = true
            }
        })
        mIntermediateAnimator?.start()
    }

    private fun startAnimation(){
        resetAnimation()
    }

    private fun stopAnimation(){
        if (mIntermediateAnimator != null){
            mIntermediateAnimator?.cancel()
            mIntermediateAnimator = null
        }
    }
}