package uk.co.ultimaspin.familyfortunes.data;

import java.util.*;

/**
 * Created by william on 15/11/2014.
 */
public class Quiz {


    private final List<Question> questionDeck;

    private List<Question> currentDeck;

    public Quiz(List<Question> questions) {
        this.questionDeck = Collections.unmodifiableList(questions);
        this.currentDeck = new ArrayList<Question>(questionDeck);
    }

    public Question nextQuestion() {
        if (!isFinished()) {
            Random random = new Random();


            int index = random.nextInt(currentDeck.size());
            Question nextQuestion = currentDeck.get(index);
            currentDeck.remove(index);

            return nextQuestion;
        }
        return null;

    }

    public boolean isFinished() {
        return currentDeck.isEmpty();
    }

    public List<Question> getRemainingQuestions() {
        return Collections.unmodifiableList(this.currentDeck);
    }

    public void remove(Question question) {
        this.currentDeck.remove(question);
    }


}
