package uk.co.ultimaspin.familyfortunes.data;

/**
 * Created by william on 01/11/2014.
 */
public class Answer {

    private final String answer;
    private final int score;

    public Answer(String answer, int score) {
        this.answer = answer;
        this.score = score;
    }

    public String getAnswer() {
        return this.answer;
    }

    public int score() {
        return this.score;
    }


}


