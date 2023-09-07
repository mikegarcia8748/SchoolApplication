package org.dna.quizzesmaster.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.dna.quizzesmaster.R
import org.dna.quizzesmaster.constants.Constants.USER_NAME

class ActivityQuizIntroduction : AppCompatActivity() {

    private var btnStart : MaterialButton? = null
    private var btnClose : MaterialButton? = null

    private var txtUseName : TextInputEditText? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_introduction)
        initWidgets()
        initButtonActions()
    }

    private fun initWidgets(){
        btnStart = findViewById(R.id.btnStart)
        btnClose = findViewById(R.id.btnClose)

        txtUseName = findViewById(R.id.txt_username)
    }

    private fun initButtonActions(){
        btnStart?.setOnClickListener {
            if(txtUseName?.text?.isNullOrEmpty() == true){
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loIntent = Intent(this, ActivityQuizQuestions::class.java)
            loIntent.putExtra(USER_NAME, txtUseName?.text.toString())
            startActivity(loIntent)
            finish()
        }

        btnClose?.setOnClickListener {
            this.finish()
        }
    }
}