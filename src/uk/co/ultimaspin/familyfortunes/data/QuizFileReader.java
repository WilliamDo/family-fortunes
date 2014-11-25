package uk.co.ultimaspin.familyfortunes.data;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 15/11/2014.
 */
public class QuizFileReader {

    public static void main(String[] args) throws IOException, URISyntaxException {
        new QuizFileReader("sample_quiz");
    }

    private final List<Question> questions;

    public QuizFileReader(String quizFileName) throws URISyntaxException, IOException {

        InputStream resource = QuizFileReader.class.getClassLoader().getResourceAsStream(quizFileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

        this.questions = new ArrayList<Question>();

        String questionText = null;
        List<Answer> answers = null;

        String line;
        reader.readLine(); // Skip first line
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");

            if (split[0] == null || split[0].length() == 0) {
                answers.add(new Answer(split[1], Integer.parseInt(split[2])));
            } else {
                if (questionText != null) {
                    questions.add(new Question(questionText, answers));
                }

                questionText = split[0];
                answers = new ArrayList<Answer>();
                answers.add(new Answer(split[1], Integer.parseInt(split[2])));
            }

        }

        // Add the last question
        questions.add(new Question(questionText, answers));

    }

    public Quiz getQuiz() {
        return new Quiz(questions);
    }

}
