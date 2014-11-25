package uk.co.ultimaspin.familyfortunes.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by william on 15/11/2014.
 */
public class Quiz {

    private List<Question> questionDeck;

    private Iterator<Question> iterator;

    private Question currentQuestion;

    private static int x = 0;

    public Quiz(List<Question> questions) {
        this.questionDeck = randomizeQuestions(questions);
        iterator = questionDeck.iterator();
    }

    public Question nextQuestion() {
        if (iterator.hasNext()) {
            Question question = iterator.next();
            this.currentQuestion = question;
        }
        return this.currentQuestion;
    }

    public boolean isFinished() {
        return !iterator.hasNext();
    }

    private List<Question> randomizeQuestions(List<Question> questions) {
        List<Question> result = new ArrayList<Question>();

        while (!questions.isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(questions.size());
            result.add(questions.remove(i));
        }

        return result;
    }

}
