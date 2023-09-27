package org.dna.draw

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import org.dna.draw.dialog.DialogBrushSize
import org.dna.draw.dialog.OnSelectBrushSizeListener
import org.dna.utils.dialog.DialogMessage
import org.dna.utils.dialog.DialogProgress
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ActivityDrawing : AppCompatActivity(), OnSelectBrushSizeListener {

    private var drawingView: DrawingView? = null
    private var poImageBtnCurrentPaint : ImageButton? = null
    private var imgBackground: ShapeableImageView? = null

    private var poLoadDialog: DialogProgress? = null

    private val poWriteStorage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(!it){
                Toast.makeText(
                    this@ActivityDrawing,
                    "Permission denied!",
                    Toast.LENGTH_SHORT
                ).show()
                return@registerForActivityResult
            }

            saveCanvasImage()
        }

    private val poReadImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(!it){
                Toast.makeText(
                    this@ActivityDrawing,
                    "Permission denied!",
                    Toast.LENGTH_SHORT
                ).show()
                return@registerForActivityResult
            }

            openGallery()
        }

    private val poGallery: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK &&
                result.data != null){
                imgBackground?.setImageURI(result.data?.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
        initWidgets()
    }

    private fun initWidgets(){
        drawingView = findViewById(R.id.drawingView)
        imgBackground = findViewById(R.id.img_background)

        val btnSelectBrushSize: ImageButton = findViewById(R.id.btnSelectBrushSize)
        val btnSelectBackground: ImageButton = findViewById(R.id.btnSelectBackground)
        val btnUndoChanges: ImageButton = findViewById(R.id.btnUndoChanges)
        val btnRedoChanges: ImageButton = findViewById(R.id.btnRedoChanges)
        val btnSaveCanvas: ImageButton = findViewById(R.id.btnSave)

        drawingView?.setBrushSize(10.toFloat())

        val loBrushColors = findViewById<LinearLayout>(R.id.linearBrushColor)

        poImageBtnCurrentPaint = loBrushColors[0] as ImageButton
        poImageBtnCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallete_selected)
        )

        btnSelectBrushSize.setOnClickListener {
            showBrushSizeChooserDialog()
        }

        btnSelectBackground.setOnClickListener {
            requestMediaImagePermission()
        }

        btnUndoChanges.setOnClickListener {
            drawingView?.undoChanges()
        }

        btnRedoChanges.setOnClickListener {
            drawingView?.redoChanges()
        }

        btnSaveCanvas.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                saveCanvasImage()
                return@setOnClickListener
            }

            poWriteStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showBrushSizeChooserDialog(){
        var loDialog = DialogBrushSize(this, this)
        loDialog.showDialog()
    }

    override fun OnSelectBrushSize(nBrushSize: Float) {
        drawingView?.setBrushSize(nBrushSize)
    }

    fun paintClick(view: View){
        if(view !== poImageBtnCurrentPaint){
            val btnPaint = view as ImageButton
            val loColorTag = btnPaint.tag.toString()
            drawingView?.setColor(loColorTag)

            btnPaint.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallete_selected)
            )

            poImageBtnCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallete_normal)
            )

            poImageBtnCurrentPaint = view
        }
    }

    override fun onBackPressed() {
        val loMessage = DialogMessage(
            this,
            "School App",
            "Exit app?")
        loMessage.setPositiveButtonInterface("Okay", object: DialogMessage.OnDialogInterfaceClickCallback {
            override fun onClick(dialog: Dialog) {
                dialog.dismiss()
                finish()
            }
        })
        loMessage.setNegativeButtonInterface("Cancel", object: DialogMessage.OnDialogInterfaceClickCallback {
            override fun onClick(dialog: Dialog) {
                dialog.dismiss()
            }
        })
        loMessage.showDialog()
    }

    private fun openGallery(){
        val loIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        poGallery.launch(loIntent)
    }

    private fun requestMediaImagePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this@ActivityDrawing,
                Manifest.permission.READ_MEDIA_IMAGES
            )){
            val loMessage = DialogMessage(
                this@ActivityDrawing,
                "Gallery Permission",
                "Drawing canvas needs access to phone's media images.")
            loMessage.setPositiveButtonInterface("Continue", object: DialogMessage.OnDialogInterfaceClickCallback{
                override fun onClick(dialog: Dialog) {
                    dialog.dismiss()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        poReadImage.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        poReadImage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            })
            loMessage.setNegativeButtonInterface("Cancel", object: DialogMessage.OnDialogInterfaceClickCallback{
                override fun onClick(dialog: Dialog) {
                    dialog.dismiss()
                }
            })
            loMessage.showDialog()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                poReadImage.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                poReadImage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun saveCanvasImage(){
//        poLoadDialog = DialogProgress(this@ActivityDrawing, "Executing coroutines...")
//        poLoadDialog?.showDialog()
        lifecycleScope.launch{
            poLoadDialog = DialogProgress(
                this@ActivityDrawing,
                "Saving canvas image. Please wait...",
            )
            poLoadDialog?.showDialog()
            val frameLayout: FrameLayout = findViewById(R.id.frame_container)
            saveCanvasBitmap(getBitmapFromView(frameLayout))
        }
    }

    private fun getBitmapFromView(view: View) : Bitmap {
        val loBitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        val loCanvas = Canvas(loBitmap)
        val loBackground = view.background

        if(loBackground != null){
            loBackground.draw(loCanvas)
        } else {
            loCanvas.drawColor(Color.WHITE)
        }

        view.draw(loCanvas)

        return loBitmap
    }

    private suspend fun saveCanvasBitmap(bitmap: Bitmap): String{
        var lsResult: String
        withContext(Dispatchers.IO){
            try{
                val loBytes = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    90,
                    loBytes)

                val loFile = File(externalCacheDir?.absoluteFile.toString()
                        + File.separator + "Canvas_" + System.currentTimeMillis() / 1000 + ".png")

                val loOutput = FileOutputStream(loFile)
                loOutput.write(loBytes.toByteArray())
                loOutput.close()

                lsResult = loFile.absolutePath

                runOnUiThread{
                    poLoadDialog?.dismiss()
                    if(lsResult.isNotEmpty()){
                        Toast.makeText(
                            this@ActivityDrawing,
                            "File saved successfully :$lsResult",
                            Toast.LENGTH_SHORT
                        ).show()
                        shareImage(lsResult)
                    } else {
                        Toast.makeText(
                            this@ActivityDrawing,
                            "Something went wrong while saving the file.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception){
                lsResult = ""
                e.printStackTrace()
            }
        }

        return lsResult
    }

    private fun shareImage(result: String){
        MediaScannerConnection.scanFile(
            this@ActivityDrawing,
            arrayOf(result),
            null){
            path, uri ->

            val loIntent = Intent()
            loIntent.action = Intent.ACTION_SEND
            loIntent.putExtra(Intent.EXTRA_STREAM, uri)
            loIntent.type = "image/png"
            startActivity(Intent.createChooser(loIntent, "Share"))
        }
    }
}