package com.example.customview

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.view.doOnLayout
import android.view.*


class DoodleView : View, View.OnTouchListener {

    private val mPaint = Paint()
    private val mTextPaint = TextPaint()
    private var mPath = Path()
    private var selectedPath = Path()

    private val paths = mutableListOf<Path>()
    private val undonePaths = mutableListOf<Path>()
    private var mX = 0f
    private var mY = 0f
    var drawMode: Boolean = true
        set(value) {
            field = value
            if (drawMode) {
                mPaint.color = Color.RED
                selectedPath = Path()
                invalidate()
            }
        }

    private lateinit var mGestureDetector: GestureDetector

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    init {
        setBackgroundColor(context.getColor(R.color.white))

        setOnTouchListener(this)
        isFocusable = true
        isFocusableInTouchMode = true
        doOnLayout {
            mGestureDetector = GestureDetector(context, mGestureListener)
        }
        initPaint()
        initTextPaint()
    }

    companion object {
        val TAG = DoodleView::class.simpleName
    }

    private fun initPaint() {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 10f
    }

    private fun initTextPaint() {
        mTextPaint.color = Color.BLACK
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.strokeJoin = Paint.Join.ROUND
        mTextPaint.strokeCap = Paint.Cap.ROUND
        mTextPaint.strokeWidth = 1f
        mTextPaint.textSize = 100f
    }

    override fun onDraw(canvas: Canvas) {
        for (p in paths) {
            if (p == selectedPath) {
                mPaint.color = Color.GREEN
            } else {
                mPaint.color = Color.RED
            }
            canvas.drawPath(p, mPaint)
        }
        canvas.drawPath(mPath, mPaint)
//        canvas.drawMultilineText1("hehe", mTextPaint, 500, 2f,600f)
        super.onDraw(canvas)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null)
            return false

        if (!drawMode) {
            mGestureDetector.onTouchEvent(event)
            return true
        }

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onDown(x, y)
            MotionEvent.ACTION_MOVE -> onMove(x, y)
            MotionEvent.ACTION_UP -> onUp()
        }
        return true
    }

    private fun onDown(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mX = x
        mY = y
        invalidate()
    }

    private fun onMove(x: Float, y: Float) {
        mPath.lineTo(x, y)
        mX = x
        mY = y
        invalidate()
    }

    private fun onUp() {
        mPath.lineTo(mX, mY)
        paths.add(mPath)
        mPath = Path()
    }

    private val mGestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                getSelectedPath(e?.x!!, e.y)
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                onPathMove(-distanceX, -distanceY)
                return true
            }

        }

    private fun onPathMove(x: Float, y: Float) {
        val matrix = Matrix()
        matrix.setTranslate(x, y)
        selectedPath.transform(matrix)
        invalidate()
    }

    private fun getSelectedPath(x: Float, y: Float) {
        paths.forEach { path ->
            val bdr = RectF()
            path.computeBounds(bdr, true)
            if (bdr.contains(x, y)) {
                selectedPath = path
                return@forEach
            }
        }
        invalidate()
    }

    fun undo() {
        if (paths.isEmpty()) {
            return
        }
        val lastPath = paths.removeLast()
        undonePaths.add(lastPath)
        invalidate()
    }

    fun redo() {
        if (undonePaths.isEmpty()) {
            return
        }
        val lastPath = undonePaths.removeLast()
        paths.add(lastPath)
        invalidate()
    }

    fun clearDraw() {
        paths.clear()
        undonePaths.clear()
        mPath.rewind()
        invalidate()
    }

}