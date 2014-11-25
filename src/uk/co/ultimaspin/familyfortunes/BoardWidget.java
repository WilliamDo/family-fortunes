package uk.co.ultimaspin.familyfortunes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uk.co.ultimaspin.familyfortunes.data.Answer;
import uk.co.ultimaspin.familyfortunes.data.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 01/11/2014.
 */
public class BoardWidget {

    private static final double ANSWER_HEIGHT = 60;
    private static final double ARC_RADIUS = 40;

    private final VBox container;

    private final Label questionLabel;

    private Question currentQuestion;

    private List<AnswerWidget> answerWidgets;

    public BoardWidget(VBox container, Label questionLabel) {
        this.container = container;
        this.questionLabel = questionLabel;
        this.answerWidgets = new ArrayList<AnswerWidget>();
    }

    public void setQuestion(Question question) {
        this.currentQuestion = question;

        // This line looks funny but it is to clear the board
        container.getChildren().removeAll(container.getChildren());
        answerWidgets.clear();

        for (int i = 0; i < question.getAnswers().size(); i++) {
            Answer answer = question.getAnswers().get(i);
            AnswerWidget answerWidget = new AnswerWidget(i + 1, answer.getAnswer(), answer.score());
            container.getChildren().add(answerWidget.getContainer());
            answerWidgets.add(answerWidget);
        }

        questionLabel.setText("... " + question.getQuestion().toLowerCase());
    }

    public List<AnswerWidget> getAvailableAnswerWidgets() {
        // TODO Filter these
        return answerWidgets;
    }

    class AnswerWidget {

        final String text;
        final int points;
        final Label label;
        final Label pointsLabel;
        final HBox container;

        private boolean revealed;

        AnswerWidget(int number, String data, int points) {

            Rectangle backDrop = RectangleBuilder.create()
                    .height(ANSWER_HEIGHT)
                    .width(480)
                    .arcHeight(ARC_RADIUS)
                    .arcWidth(ARC_RADIUS)
                    .fill(Color.BLUE)
                    .styleClass("ff-answer")
                    .build();

            this.label = LabelBuilder.create()
                    .font(Font.font("null", FontWeight.BOLD, 20))
                    .textFill(Color.WHITE)
                    .build();

            StackPane stackPane = StackPaneBuilder.create()
                    .children(backDrop, label)
                    .build();

            Rectangle positionBackDrop = RectangleBuilder.create()
                    .height(ANSWER_HEIGHT)
                    .width(60)
                    .arcHeight(ARC_RADIUS)
                    .arcWidth(ARC_RADIUS)
                    .fill(Color.DARKBLUE)
                    .styleClass("ff-answer-number")
                    .build();

            Label positionLabel = LabelBuilder.create()
                    .text(number + "")
                    .font(Font.font("null", FontWeight.BOLD, 30))
                    .textFill(Color.WHITE)
                    .build();

            StackPane position = StackPaneBuilder.create()
                    .children(positionBackDrop, positionLabel)
                    .build();

            this.pointsLabel = LabelBuilder.create()
                    .font(Font.font("null", FontWeight.BOLD, 20))
                    .textFill(Color.WHITE)
                    .build();

            Rectangle pointsBackDrop = RectangleBuilder.create()
                    .height(ANSWER_HEIGHT)
                    .width(60)
                    .arcHeight(ARC_RADIUS)
                    .arcWidth(ARC_RADIUS)
                    .fill(Color.BLUE)
                    .styleClass("ff-answer-score")
                    .build();

            StackPane pointsPane = StackPaneBuilder.create()
                    .children(pointsBackDrop, pointsLabel)
                    .build();

            this.container = HBoxBuilder.create()
                    .spacing(10)
                    .children(position, stackPane, pointsPane)
                    .alignment(Pos.CENTER)
                    .build();

            this.text = data;
            this.points = points;
            this.revealed = false;
        }

        public void revealAnswer() {
            if (!isRevealed()) {
                SoundEffects.playAnswerSound();
                label.setText(text.toUpperCase());
                pointsLabel.setText(points + "");
                this.revealed = true;
            }
        }

        public HBox getContainer() {
            return this.container;
        }

        private boolean isRevealed() {
            return this.revealed;
        }

        public String getAnswerText() {
            return this.text;
        }

    }


}
