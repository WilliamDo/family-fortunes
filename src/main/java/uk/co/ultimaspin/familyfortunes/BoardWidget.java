package uk.co.ultimaspin.familyfortunes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import uk.co.ultimaspin.familyfortunes.data.Answer;
import uk.co.ultimaspin.familyfortunes.data.PrizeAnswerUtil;
import uk.co.ultimaspin.familyfortunes.data.Question;
import uk.co.ultimaspin.familyfortunes.transition.FlashAnswerTransition;

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

    private TotalScoreWidget scoreWidget;

    public BoardWidget(VBox container, Label questionLabel) {
        this.container = container;
        this.questionLabel = questionLabel;
        this.answerWidgets = new ArrayList<>();
        this.scoreWidget = new TotalScoreWidget();
        
    }

    public void setQuestion(Question question) {
        this.currentQuestion = question;

        // This line looks funny but it is to clear the board
        container.getChildren().removeAll(container.getChildren());
        answerWidgets.clear();
        scoreWidget.clear();

        for (int i = 0; i < question.getAnswers().size(); i++) {
            Answer answer = question.getAnswers().get(i);
            AnswerWidget answerWidget = new AnswerWidget(i + 1, answer.getAnswer(), answer.score(), scoreWidget);
            container.getChildren().add(answerWidget.getContainer());
            answerWidgets.add(answerWidget);
        }
        
        container.getChildren().add(scoreWidget.container);

        questionLabel.setText("... " + question.getQuestion().toLowerCase());
    }

    public List<AnswerWidget> getAvailableAnswerWidgets() {
        // TODO Filter these
        return answerWidgets;
    }

    class AnswerWidget {

        final String STYLE_ANSWER_TEXT = "ff-answer";
        final String STYLE_ANSWER_FLASH = "ff-answer-flash";

        final String text;
        final int points;
        final Label label;
        final Label pointsLabel;
        final HBox container;
        final Rectangle backDrop;

        final TotalScoreWidget scoreWidget;

        private boolean revealed;

        AnswerWidget(int number, String data, int points, TotalScoreWidget scoreWidget) {
            this.scoreWidget = scoreWidget;

            this.backDrop = new Rectangle();
            this.backDrop.setHeight(ANSWER_HEIGHT);
            this.backDrop.setWidth(480);
            this.backDrop.setArcHeight(ARC_RADIUS);
            this.backDrop.setArcWidth(ARC_RADIUS);
            this.backDrop.setFill(Color.BLUE);
            this.backDrop.setStyle(STYLE_ANSWER_TEXT);

            this.label = new Label();
            this.label.setFont(Font.font("null", FontWeight.BOLD, 20));
            this.label.setTextFill(Color.WHITE);

            StackPane stackPane = new StackPane(backDrop, label);

            Rectangle positionBackDrop = new Rectangle();
            positionBackDrop.setHeight(ANSWER_HEIGHT);
            positionBackDrop.setWidth(60);
            positionBackDrop.setArcHeight(ARC_RADIUS);
            positionBackDrop.setArcWidth(ARC_RADIUS);
            positionBackDrop.setFill(Color.DARKBLUE);
            positionBackDrop.setStyle("ff-answer-number");

            Label positionLabel = new Label();
            positionLabel.setText(number + "");
            positionLabel.setFont(Font.font("null", FontWeight.BOLD, 30));
            positionLabel.setTextFill(Color.WHITE);

            StackPane position = new StackPane(positionBackDrop, positionLabel);

            this.pointsLabel = new Label();
            this.pointsLabel.setFont(Font.font("null", FontWeight.BOLD, 22));
            this.pointsLabel.setTextFill(Color.WHITE);

            Rectangle pointsBackDrop = new Rectangle();
            pointsBackDrop.setHeight(ANSWER_HEIGHT);
            pointsBackDrop.setWidth(60);
            pointsBackDrop.setArcHeight(ARC_RADIUS);
            pointsBackDrop.setArcWidth(ARC_RADIUS);
            pointsBackDrop.setFill(Color.BLUE);
            pointsBackDrop.setStyle("ff-answer-score");

            StackPane pointsPane = new StackPane(pointsBackDrop, pointsLabel);

            this.container = new HBox(position, stackPane, pointsPane);
            this.container.setSpacing(10);
            this.container.setAlignment(Pos.CENTER);

            this.text = data;
            this.points = points;
            this.revealed = false;
        }

        public void revealAnswer() {
            if (!isRevealed()) {

                if (PrizeAnswerUtil.getInstance().isPrizeAnswerTime()) {
                    FlashAnswerTransition flashAnswerTransition = new FlashAnswerTransition(backDrop, STYLE_ANSWER_TEXT, STYLE_ANSWER_FLASH, Duration.millis(180));
                    flashAnswerTransition.setAutoReverse(true);
                    flashAnswerTransition.setCycleCount(9);
                    flashAnswerTransition.play();
                    SoundEffects.playPrizeAnswerSound();
                } else {
                    SoundEffects.playAnswerSound();
                }



                label.setText(text.toUpperCase());
                pointsLabel.setText(points + "");
                this.revealed = true;
                scoreWidget.addPoints(this.points);
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
    
    class TotalScoreWidget {

        private final HBox container;
        private int currentPoints;
        private final Label pointsLabel;

        TotalScoreWidget() {

            this.currentPoints = 0;

            Rectangle backDrop = new Rectangle();
            backDrop.setHeight(ANSWER_HEIGHT);
            backDrop.setWidth(480);
            backDrop.setArcHeight(ARC_RADIUS);
            backDrop.setArcWidth(ARC_RADIUS);
            backDrop.setFill(Color.ORANGERED);
            backDrop.setStyle("ff-total-score");

            Label label = new Label();
            label.setFont(Font.font("null", FontWeight.BOLD, 20));
            label.setTextFill(Color.WHITE);
            label.setText("TOTAL");

            StackPane stackPane = new StackPane(backDrop, label);

            Rectangle positionBackDrop = new Rectangle();
            positionBackDrop.setHeight(ANSWER_HEIGHT);
            positionBackDrop.setWidth(60);
            positionBackDrop.setVisible(false);

            this.pointsLabel = new Label();
            this.pointsLabel.setFont(Font.font("null", FontWeight.BOLD, 22));
            this.pointsLabel.setText("0");
            this.pointsLabel.setTextFill(Color.WHITE);

            Rectangle pointsBackDrop = new Rectangle();
            pointsBackDrop.setHeight(ANSWER_HEIGHT);
            pointsBackDrop.setWidth(60);
            pointsBackDrop.setArcHeight(ARC_RADIUS);
            pointsBackDrop.setArcWidth(ARC_RADIUS);
            pointsBackDrop.setFill(Color.ORANGERED);
            pointsBackDrop.setStyle("ff-total-score");

            StackPane pointsPane = new StackPane(pointsBackDrop, pointsLabel);

            this.container = new HBox(positionBackDrop, stackPane, pointsPane);
            this.container.setSpacing(10);
            this.container.setAlignment(Pos.CENTER);

        }

        void addPoints(int points) {
            this.currentPoints += points;
            this.pointsLabel.setText(currentPoints + "");
        }

        void clear() {
            this.currentPoints = 0;
            this.pointsLabel.setText(currentPoints + "");
        }
        
    }


}
