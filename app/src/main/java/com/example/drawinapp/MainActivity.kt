package com.example.drawinapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null


    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageBackground: ImageView = findViewById(R.id.iv_imageBackground)
                imageBackground.setImageURI(result.data?.data)
            }
        }

    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()

                    val pickIntent: Intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                    openGalleryLauncher.launch(pickIntent)

                } else {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        val linearLayoutColors = findViewById<LinearLayout>(R.id.ll_paint_colors)
        mImageButtonCurrentPaint = linearLayoutColors[1] as ImageButton

        // Brush button
        val ib_brush: ImageButton = findViewById(R.id.ib_brush_size)
        ib_brush.setOnClickListener {
            showBrushChooserDialog()
        }

        // Gallery Button
        val ib_gallery: ImageButton = findViewById(R.id.ib_gallery)
        ib_gallery.setOnClickListener {
            requestStoragePermission()
        }

        // Undo Button
        val ib_undo: ImageButton = findViewById(R.id.ib_undo)
        ib_undo.setOnClickListener {
            drawingView?.onClickUndo()
        }


        drawingView?.setSizeForBrush(20.toFloat())

    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            showRationaleDialog("Drawing Application", "Needs to access external storage")
        } else {
            requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private fun showBrushChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.layout_brush_size)
        brushDialog.setTitle("Choose brush title")
        val smallButton = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        val mediumButton = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        val largeButton = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)
        smallButton.setOnClickListener {
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


    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }


    fun paintClicked(view: View) {
        Toast.makeText(this, "Paint clicked", Toast.LENGTH_SHORT).show()
        if (view != mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.palette_selected
                )
            )

            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.palette_normal
                )
            )
            mImageButtonCurrentPaint = view

        }


    }
}