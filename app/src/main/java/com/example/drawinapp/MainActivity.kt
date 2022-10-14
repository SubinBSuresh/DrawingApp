package com.example.drawinapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)

        val ib_brush :ImageButton = findViewById(R.id.ib_brush_size)
        ib_brush.setOnClickListener {
            showBrushChooserDialog()
        }

        drawingView?.setSizeForBrush(20.toFloat())

    }

    private fun showBrushChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.layout_brush_size)
        brushDialog.setTitle("Choose brush title")
        val smallButton = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        val mediumButton = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        val largeButton = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)
        smallButton.setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        mediumButton.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }
        largeButton.setOnClickListener {
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
    }
}