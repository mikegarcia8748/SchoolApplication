package org.dna.quizzesmaster.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import org.dna.quizzesmaster.R
import org.dna.quizzesmaster.constants.Constants
import org.dna.quizzesmaster.constants.Constants.USER_NAME
import org.dna.quizzesmaster.pojo.Question

class ActivityQuizQuestions : AppCompatActivity(), View.OnClickListener {
    private val TAG = "ActivityQuizQuestions"

    private var psUsername : String? = null

    private var pnCrrPosition : Int = 1
    private var poQuestions : ArrayList<Question>? = null
    private var pnSltOption : Int = 0
    private var pnCorrectAns: Int = 0
    private var pnTotalQuiz : Int? = null

    private var imgPreview : ShapeableImageView? = null
    private var lblQuestion : MaterialTextView? = null
    private var progressBar : LinearProgressIndicator? = null
    private var lblProgress : MaterialTextView? = null
    private var lblOption1 : MaterialTextView? = null
    private var lblOption2 : MaterialTextView? = null
    private var lblOption3 : MaterialTextView? = null
    private var lblOption4 : MaterialTextView? = null

    private var btnContinue : MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        psUsername = intent.getStringExtra(USER_NAME)
        Log.d(TAG, "Username has been initialized!. Value: $psUsername")

        initWidgets()
        initQuestions()
        bindQuestionsToUI()
    }

    private fun initQuestions(){
        defaultOptionsView()
        poQuestions = Constants.getQuestions()

        if(pnCrrPosition == poQuestions!!.size){
            btnContinue?.text = R.string.btnSubmit.toString();
            return
        }
    }

    private fun defaultOptionsView(){
        val loOptions = ArrayList<MaterialTextView>()
        lblOption1?.let {
            loOptions.add(0, it)
        }
        lblOption2?.let {
            loOptions.add(1, it)
        }
        lblOption3?.let {
            loOptions.add(2, it)
        }
        lblOption4?.let {
            loOptions.add(3, it)
        }

        val loColor = com.google.android.material.R.attr.colorOnPrimary
        val loTypeVal = TypedValue()
        theme.resolveAttribute(loColor, loTypeVal, true)
        val lnColorRes = loTypeVal.data

        for (option in loOptions){
            option.setTextColor(lnColorRes)
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_options_default
            )
        }
    }

    private fun setSelectedOptionView(foTextView: MaterialTextView, fnOptionIndex: Int){
        defaultOptionsView()
        pnSltOption = fnOptionIndex
        foTextView.setTypeface(foTextView.typeface, Typeface.BOLD)
        foTextView.background = ContextCompat.getDrawable(
            this,
            R.drawable.bg_selected_option
        )
    }

    private fun bindQuestionsToUI() {
        val loQuestion: Question = poQuestions!![pnCrrPosition - 1]
        progressBar?.progress = pnCrrPosition
        progressBar?.max = poQuestions!!.size
        lblProgress?.text = "$pnCrrPosition/${progressBar?.max}"
        lblQuestion?.text = loQuestion.sQuestnx
        lblOption1?.text = loQuestion.sOption1
        lblOption2?.text = loQuestion.sOption2
        lblOption3?.text = loQuestion.sOption3
        lblOption4?.text = loQuestion.sOption4
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnOption1 -> {
                lblOption1?.let {
                    setSelectedOptionView(it, 1)
                }
            }

            R.id.btnOption2 -> {
                lblOption2?.let {
                    setSelectedOptionView(it, 2)
                }
            }

            R.id.btnOption3 -> {
                lblOption3?.let {
                    setSelectedOptionView(it, 3)
                }
            }

            R.id.btnOption4 -> {
                lblOption4?.let {
                    setSelectedOptionView(it, 4)
                }
            }

            R.id.btnCheckAnswer -> {
                if(pnSltOption == 0){
                    pnCrrPosition++

                    when {
                        pnCrrPosition <= poQuestions!!.size -> {
                            initQuestions()
                            bindQuestionsToUI()
                        } else ->{
                            val loIntent = Intent(this, ActivityQuizResult::class.java)
                            loIntent.putExtra(Constants.USER_NAME, psUsername)
                            loIntent.putExtra(Constants.CORRECT_SCORE, pnCorrectAns)
                            loIntent.putExtra(Constants.TOTAL_QUIZ, poQuestions?.size)
                            startActivity(loIntent)
                        }
                    }
                } else {
                    val loQuestion = poQuestions?.get(pnCrrPosition - 1)
                    if(loQuestion!!.nAnswerx != pnSltOption){
                        checkAnswer(pnSltOption, R.drawable.bg_wrong_answer)
                        Log.d(TAG, "Answer is wrong!, No points added. Your points: $pnCorrectAns")
                    } else {
                        pnCorrectAns++
                        Log.d(TAG, "Answer is correct!, 1 point added. Your points: $pnCorrectAns")
                    }
                    checkAnswer(loQuestion.nAnswerx, R.drawable.bg_correct_answer)
                }

                if(pnCrrPosition == poQuestions!!.size){
                    btnContinue?.text = "Finish"
                } else {
                    btnContinue?.text = "Proceed"
                }

                pnSltOption = 0
            }
        }
    }

    private fun initWidgets(){
        imgPreview = findViewById(R.id.img_imagePreview);
        lblQuestion = findViewById(R.id.lblQuestion);
        progressBar = findViewById(R.id.progressBar);
        lblProgress = findViewById(R.id.lblProgressIndicator);
        lblOption1 = findViewById(R.id.btnOption1);
        lblOption2 = findViewById(R.id.btnOption2);
        lblOption3 = findViewById(R.id.btnOption3);
        lblOption4 = findViewById(R.id.btnOption4);

        btnContinue = findViewById(R.id.btnCheckAnswer)

        lblOption1?.setOnClickListener(this)
        lblOption2?.setOnClickListener(this)
        lblOption3?.setOnClickListener(this)
        lblOption4?.setOnClickListener(this)
        btnContinue?.setOnClickListener(this)
    }

    private fun checkAnswer(fnAnswer: Int, foView: Int){
        when(fnAnswer){
            1 -> {
                lblOption1?.background = ContextCompat.getDrawable(
                    this,
                    foView
                )
            }
            2 -> {
                lblOption2?.background = ContextCompat.getDrawable(
                    this,
                    foView
                )
            }
            3 -> {
                lblOption3?.background = ContextCompat.getDrawable(
                    this,
                    foView
                )
            }
            4 -> {
                lblOption4?.background = ContextCompat.getDrawable(
                    this,
                    foView
                )
            }
        }
    }
}