package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.DragEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_test2.*

@SuppressLint("SetTextI18n")
class Test2 : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnDragListener {

    private var gestureDetectorCompat: GestureDetectorCompat? = null

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        tvGesture.text = "onDoubleTap"
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        tvGesture.text = "onDoubleTapEvent"
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        tvGesture.text = "onSingleTapConfirmed"
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        tvGesture.text = "onShowPress"
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        tvGesture.text = "onSingleTapUp"
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        tvGesture.text = "onDown"
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        tvGesture.text = "onFling"
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {

        tvGesture.text = "onScroll"
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        tvGesture.text = "onLongPress"
        val builder = View.DragShadowBuilder(imageDrag)
        @Suppress("DEPRECATION")
        imageDrag.startDrag(null, builder, null, 0)
        builder.view.setOnDragListener(this)
    }

    override fun onDrag(p0: View?, dragEvent: DragEvent?): Boolean {
        when (dragEvent!!.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                tvGesture.text = "drag started"
                return true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                tvGesture.text = "drag entered"
                return true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                tvGesture.text = "onDrag: current point: ( " + dragEvent.x + " Y-Axis : " + dragEvent.y + " )"
                return true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                tvGesture.text = "exited"
                return true
            }

            DragEvent.ACTION_DROP -> {
                tvGesture.text = "dropped"
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                tvGesture.text = "ended"
                return true
            }

            // An unknown action type was received.
            else -> tvGesture.text = "Unknown action type received by OnStartDragListener"
        }

        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        gestureDetectorCompat = GestureDetectorCompat(this, this)
       gestureDetectorCompat!!.setOnDoubleTapListener(this)
        imageDrag.setOnTouchListener { v, event ->
            val x1= event.getX()
            val y1=event.getY()
            if(x1<50 && y1<50)
            {
                Toast.makeText(this,"The image first Part "+x1+" postion "+y1,Toast.LENGTH_SHORT).show()
            }
            if(x1>100 && y1<150)
            {
                Toast.makeText(this,"The image Second Part "+x1+" postion "+y1,Toast.LENGTH_SHORT).show()
            }
            if(x1>50 && x1<150 && y1<50 && y1>150)
            {
                Toast.makeText(this,"The image Third Part "+x1+" postion "+y1,Toast.LENGTH_SHORT).show()
            }
            if(x1<50 && y1>150)
            {
                Toast.makeText(this,"The image fourth Part "+x1+" postion "+y1,Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"The image was at "+x1+" postion "+y1,Toast.LENGTH_SHORT).show()
            }

            !gestureDetectorCompat!!.onTouchEvent(event)
        }
        tvGesture.setOnTouchListener { v, event ->
            val x1= event.getX()
            val y1=event.getY()
            Toast.makeText(this, "The Text was " +x1  +y1, Toast.LENGTH_SHORT).show()
            !gestureDetectorCompat!!.onTouchEvent(event)
        }
        }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        gestureDetectorCompat!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}
