package org.dna.quizzesmaster.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView
import org.dna.quizzesmaster.R
import org.dna.quizzesmaster.constants.Constants

class ActivityQuizResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        var lsUserName: String? = intent.getStringExtra(Constants.USER_NAME)
        var lnCorrectx: Int? = intent.getIntExtra(Constants.CORRECT_SCORE, 0)
        var lnTotlQuiz: Int? = intent.getIntExtra(Constants.TOTAL_QUIZ, 0)

        val lblUserName : MaterialTextView = findViewById(R.id.lblUserName)
        val lblQuizScre : MaterialTextView = findViewById(R.id.lblQuizScore)

        lblUserName.text = lsUserName
        lblQuizScre.text = "Your score is $lnCorrectx out of $lnTotlQuiz"
    }
}