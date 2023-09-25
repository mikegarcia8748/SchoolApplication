package org.dna.draw

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
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

class ActivityDrawing : AppCompatActivity(), OnSelectBrushSizeListener {

    private var drawingView: DrawingView? = null
    private var poImageBtnCurrentPaint : ImageButton? = null
    private var imgBackground: ShapeableImageView? = null

    private var poLoadDialog: DialogProgress? = null

    private val poImagePermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val lsPermission = it.key
                val isGranted = it.value

                if(!isGranted){
                    Toast.makeText(
                        this@ActivityDrawing,
                        "Permission denied!",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@forEach
                }

                val loIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                poGallery.launch(loIntent)
            }
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
            poLoadDialog = DialogProgress(this@ActivityDrawing, "Executing coroutines...")
            poLoadDialog?.showDialog()
            lifecycleScope.launch{
                execute("Coroutine executing....")
            }
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
                        poImagePermission.launch(arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES
                        ))
                    } else {
                        poImagePermission.launch(arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ))
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
                poImagePermission.launch(arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                ))
            } else {
                poImagePermission.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            }
        }
    }

    private suspend fun execute(result: String){
        withContext(Dispatchers.IO){
            for(i in 1..10){
                Log.d("Coroutine", "")
                Thread.sleep(1000)
            }
        }
        runOnUiThread {
            poLoadDialog?.dismiss()
            Toast.makeText(
                this@ActivityDrawing,
                result,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}