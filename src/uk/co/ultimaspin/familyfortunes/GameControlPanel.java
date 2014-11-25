package uk.co.ultimaspin.familyfortunes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uk.co.ultimaspin.familyfortunes.data.Question;
import uk.co.ultimaspin.familyfortunes.data.Quiz;
import uk.co.ultimaspin.familyfortunes.data.QuizFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by william on 02/11/2014.
 */
public class GameControlPanel {

    private final BoardWidget boardWidget;
    private final WrongAnswerBarWidget leftBarWidget;
    private final WrongAnswerBarWidget rightBarWidget;

    private final VBox answerButtonBox;
    private final Label questionLabel;
    private final Button leftWrongButton;
    private final Button rightWrongButton;
    private final Button nextQuestionButton;
    private Quiz quiz;


    public GameControlPanel(BoardWidget boardWidget, final WrongAnswerBarWidget leftBarWidget, final WrongAnswerBarWidget rightBarWidget) {

        this.boardWidget = boardWidget;
        this.leftBarWidget = leftBarWidget;
        this.rightBarWidget = rightBarWidget;

        this.answerButtonBox = VBoxBuilder.create()
                .spacing(8)
                .minWidth(200)
                .minHeight(300)
                .alignment(Pos.TOP_CENTER)
                .build();
        try {
            this.quiz = new QuizFileReader("sample_quiz.csv").getQuiz();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.questionLabel = LabelBuilder.create()
                .text("Click on \"Next Question\" to begin")
                .build();

        this.leftWrongButton = ButtonBuilder.create()
                .text("X - LEFT")
                .build();

        leftWrongButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                leftBarWidget.wrongAnswer();
                if (leftBarWidget.allWrong()) {
                    leftWrongButton.setDisable(true);
                }
            }
        });


        this.rightWrongButton = ButtonBuilder.create()
                .text("X - RIGHT")
                .build();

        rightWrongButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rightBarWidget.wrongAnswer();
                if (rightBarWidget.allWrong()) {
                    rightWrongButton.setDisable(true);
                }
            }
        });

        HBox hBox = HBoxBuilder.create()
                .children(leftWrongButton, answerButtonBox, rightWrongButton)
                .spacing(10)
                .styleClass("ff-board")
                .build();

        this.nextQuestionButton = ButtonBuilder.create()
                .text("Next Question")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        nextQuestion();
                    }
                })
                .build();

        VBox allControls = VBoxBuilder.create()
                .children(nextQuestionButton, questionLabel, hBox)
                .padding(new Insets(20, 20, 20, 20))
                .spacing(10)
                .build();

        FlowPane flowPaneContainer = FlowPaneBuilder.create()
                .children(allControls)
                .alignment(Pos.TOP_CENTER)
                .build();

        Stage adminStage = new Stage();
        adminStage.setTitle("Quiz Master Control Panel");
        Scene scene = new Scene(flowPaneContainer, 600, 480);
        scene.getStylesheets().add("cp.css");
        adminStage.setScene(scene);
        adminStage.sizeToScene();
        adminStage.show();

    }

    private void populateControlAnswerButtons() {
        List<BoardWidget.AnswerWidget> answerWidgets = this.boardWidget.getAvailableAnswerWidgets();

        this.answerButtonBox.getChildren().clear();

        for (final BoardWidget.AnswerWidget widget : answerWidgets) {

            final Button actionButton = ButtonBuilder.create()
                    .text(widget.getAnswerText().toUpperCase())
                    .prefWidth(200)
                    .build();

            actionButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    widget.revealAnswer();
                    actionButton.setDisable(true);
                }
            });

            answerButtonBox.getChildren().add(actionButton);

        }
    }

    private void nextQuestion() {
        if (!quiz.isFinished()) {
            leftBarWidget.reset();
            rightBarWidget.reset();

            leftWrongButton.setDisable(false);
            rightWrongButton.setDisable(false);

            Question question = quiz.nextQuestion();
            boardWidget.setQuestion(question);
            questionLabel.setText(question.getQuestion());
            populateControlAnswerButtons();

            if (quiz.isFinished()) {
                this.nextQuestionButton.setDisable(true);
            }

        }


    }

}
