package uk.co.ultimaspin.familyfortunes.data;

import java.util.Collections;
import java.util.List;

/**
 * Created by william on 01/11/2014.
 */
public class Question {

    private final String question;

    private final List<Answer> answers;

    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = Collections.unmodifiableList(answers);

    }

    public String getQuestion() {
        return this.question;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
