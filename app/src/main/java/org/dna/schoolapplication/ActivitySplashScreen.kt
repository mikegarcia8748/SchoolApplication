package org.dna.schoolapplication

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton

class ActivitySplashScreen : AppCompatActivity() {

    /**
    private val poPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permission = it.key
                val isGrandted = it.value

                if(isGrandted){
                    Toast.makeText(
                        this,
                        "Permission granted!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if(permission == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(
                            this,
                            "Permission denied!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val btnRequest = findViewById<MaterialButton>(R.id.btnRequestPermission)

        btnRequest.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationalDialog("School App", "School App needs to access your external storage.")
        } else {
            poPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }
    }

    private fun showRationalDialog(title: String, message: String){
        val loBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        loBuilder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){
                dialog, _ ->
                dialog.dismiss()
            }
        loBuilder.create().show()
    }
}