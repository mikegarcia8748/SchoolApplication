package org.dna.quizzesmaster.constants

import org.dna.quizzesmaster.pojo.Question

object Constants {

    fun getQuestions() : ArrayList<Question> {
        val loQuestions = ArrayList<Question>()

        var loQuestion = Question(
            "MX0001",
            "What country does this flag belongs to?",
            "",
            "Philippines",
            "Japan",
            "Singapore",
            "Thailand",
            1)

        loQuestions.add(loQuestion)

        loQuestion = Question(
            "MX0002",
            "What country does this flag belongs to?",
            "",
            "Japan",
            "Philippines",
            "Thailand",
            "Singapore",
            3
        )

        loQuestion = Question(
            "MX0003",
            "What is Object Oriented Programming?",
            "",
            "Japan",
            "Philippines",
            "Thailand",
            "Singapore",
            3
        )


        loQuestion = Question(
            "MX0004",
            "What is android development?",
            "",
            "Japan",
            "Philippines",
            "Thailand",
            "Singapore",
            3
        )


        loQuestions.add(loQuestion)

        return loQuestions
    }
}