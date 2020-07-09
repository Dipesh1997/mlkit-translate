package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.DragEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.activity_test2.*
import kotlinx.android.synthetic.main.content_text_recognition.*

class TextRecognitionActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnDragListener {

    private val imageView by lazy { findViewById<ImageView>(R.id.text_recognition_image_view)!! }

    private val bottomSheetButton by lazy { findViewById<FrameLayout>(R.id.bottom_sheet_button)!! }
    private val bottomSheetRecyclerView by lazy { findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view)!! }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(findViewById(R.id.bottom_sheet)!!) }

    private val textRecognitionModels = ArrayList<TextRecognitionModel>()
    private var gestureDetectorCompat: GestureDetectorCompat? = null
    var topValue =0
    var leftValue =0
    var rightValue =0
    var bottomValue =0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)
        gestureDetectorCompat = GestureDetectorCompat(this, this)
        gestureDetectorCompat!!.setOnDoubleTapListener(this)
        bottomSheetButton.setOnClickListener {
            CropImage.activity().start(this)
        }

        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomSheetRecyclerView.adapter = TextRecognitionAdapter(this, textRecognitionModels)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val imageUri = result.uri
                @Suppress("DEPRECATION")
                this.analyzeImage(getBitmap(this.contentResolver, imageUri))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "There was some error : ${result.error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun analyzeImage(image: Bitmap?) {
        if (image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        imageView.setImageBitmap(null)
        textRecognitionModels.clear()
        bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showProgress()

        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)

                recognizeText(it, mutableImage)

                imageView.setImageBitmap(mutableImage)
                hideProgress()
                bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            .addOnFailureListener {
                Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
                hideProgress()
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?) {
        if (result == null || image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 2F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F
        var index = 0
        for (block in result.textBlocks) {

            val dataSaveHelper = DataSaveHelper(this)
            imageView.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Confirm")
                    .setMessage("Are you sure to save it?")
                    .setCancelable(true)
                    .setPositiveButton("No"){dialog,which->
                    }
                    .setNegativeButton("Yes"){dialog,which->
                        val time = 12
                        val name: String = block.text
                        dataSaveHelper.addNotes(Notes(time,name))
                    }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

            }

            for (line in block.lines) {
                canvas.drawRect(line.boundingBox!!, rectPaint)
                canvas.drawText(
                    index.toString(),
                    line.cornerPoints!![2].x.toFloat(),
                    line.cornerPoints!![2].y.toFloat(),
                    textPaint
                )
                val top = line.boundingBox!!.top
                val left = line.boundingBox!!.left
                val right = line.boundingBox!!.right
                val bottom = line.boundingBox!!.bottom
                val cornerX=line.cornerPoints!![0].x.toFloat()
                val cornerY=line.cornerPoints!![0].y.toFloat()
                topValue=top
                leftValue=left
                rightValue=right
                bottomValue=bottom

                //textRecognitionModels.add(TextRecognitionModel(index++, line.text))
                /*imageView.setOnTouchListener { v, event ->
                    val x1 = event.getX()
                    val y1 = event.getY()

                    if (x1 in 130.0..540.0 && y1 in 60.0..180.0) {
                        Toast.makeText(this, "SAMANK", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this, "The image was at " + x1 + " postion " + y1, Toast.LENGTH_SHORT).show()
                    }

                    !gestureDetectorCompat!!.onTouchEvent(event)
                }*/


                textRecognitionModels.add(TextRecognitionModel(index++, line.text))
            }
        }
    }


    private fun showProgress() {
        findViewById<View>(R.id.bottom_sheet_button_image).visibility = View.GONE
        findViewById<View>(R.id.bottom_sheet_button_progress).visibility = View.VISIBLE
    }

    private fun hideProgress() {
        findViewById<View>(R.id.bottom_sheet_button_image).visibility = View.VISIBLE
        findViewById<View>(R.id.bottom_sheet_button_progress).visibility = View.GONE
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        gestureDetectorCompat!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        tvGesture2.text = "onDoubleTap"
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        tvGesture2.text = "onDoubleTapEvent"
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        tvGesture2.text = "onSingleTapConfirmed"
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        tvGesture2.text = "onShowPress"
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        tvGesture2.text = "onSingleTapUp"
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        tvGesture2.text = "onDown"
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        tvGesture2.text = "onFling"
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {

        tvGesture2.text = "onScroll"
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        tvGesture2.text = "onLongPress"
        val builder = View.DragShadowBuilder(imageDrag)
        @Suppress("DEPRECATION")
        imageDrag.startDrag(null, builder, null, 0)
        builder.view.setOnDragListener(this)
    }

    override fun onDrag(p0: View?, dragEvent: DragEvent?): Boolean {
        when (dragEvent!!.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                tvGesture2.text = "drag started"
                return true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                tvGesture2.text = "drag entered"
                return true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                tvGesture2.text = "onDrag: current point: ( " + dragEvent.x + " Y-Axis : " + dragEvent.y + " )"
                return true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                tvGesture2.text = "exited"
                return true
            }

            DragEvent.ACTION_DROP -> {
                tvGesture2.text = "dropped"
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                tvGesture2.text = "ended"
                return true
            }

            // An unknown action type was received.
            else -> tvGesture2.text = "Unknown action type received by OnStartDragListener"
        }

        return false
    }
}