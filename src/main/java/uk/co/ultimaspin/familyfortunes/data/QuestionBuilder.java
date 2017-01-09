package uk.co.ultimaspin.familyfortunes.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 01/11/2014.
 */
public class QuestionBuilder {

    private String question;
    private List<Answer> answers;

    private QuestionBuilder(String question) {
        this.question = question;
        this.answers = new ArrayList<Answer>();

    }

    public static QuestionBuilder newQuestion(String question) {
        return new QuestionBuilder(question);
    }

    public QuestionBuilder answer(String answer, int score) {
        answers.add(new Answer(answer, score));
        return this;
    }

    public Question build() {
        return new Question(question, answers);
    }

}


