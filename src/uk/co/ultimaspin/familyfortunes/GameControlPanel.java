package uk.co.ultimaspin.familyfortunes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uk.co.ultimaspin.familyfortunes.data.PrizeAnswerUtil;
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
    private final Button selectQuestionButton;

    private Slider slider;

    private final ComboBox<Question> questionComboBox;

    private Quiz quiz;


    public GameControlPanel(BoardWidget boardWidget, final WrongAnswerBarWidget leftBarWidget, final WrongAnswerBarWidget rightBarWidget) {

        this.boardWidget = boardWidget;
        this.leftBarWidget = leftBarWidget;
        this.rightBarWidget = rightBarWidget;

        this.answerButtonBox = VBoxBuilder.create()
                .spacing(8)
                .minWidth(420)
                .minHeight(200)
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
                .text("Select a question to begin")
                .wrapText(true)
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
                .text("Next Random Question")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        nextQuestion();
                    }
                })
                .build();

        this.questionComboBox = new ComboBox<Question>();

        this.selectQuestionButton = ButtonBuilder.create()
                .text("Select Question")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        selectQuestion();
                    }
                })
                .build();

        HBox selectQuestionBox = HBoxBuilder.create()
                .spacing(10)
                .children(this.questionComboBox, this.selectQuestionButton)
                .build();

        updateRemainingQuestions();


        VBox allControls = VBoxBuilder.create()
                .children(nextQuestionButton, selectQuestionBox, ScrollPaneBuilder.create().content(questionLabel).build(), hBox, prizeAnswerControl(), prizeTimeControl(), soundTestControl())
                .padding(new Insets(20, 20, 20, 20))
                .spacing(10)
                .build();

        FlowPane flowPaneContainer = FlowPaneBuilder.create()
                .children(allControls)
                .alignment(Pos.TOP_CENTER)
                .build();

        Stage adminStage = new Stage();
        adminStage.setTitle("Quiz Master Control Panel");
        Scene scene = new Scene(flowPaneContainer, 800, 600);
        scene.getStylesheets().add("cp.css");
        adminStage.setScene(scene);
        adminStage.sizeToScene();
        adminStage.show();

    }

    private HBox prizeAnswerControl() {
        final ToggleGroup group = ToggleGroupBuilder.create()
                .build();

        RadioButton rbOn = RadioButtonBuilder.create()
                .toggleGroup(group)
                .text("On")
                .userData(Boolean.TRUE)
                .build();

        RadioButton rbOff = RadioButtonBuilder.create()
                .toggleGroup(group)
                .selected(true)
                .text("Off")
                .userData(Boolean.FALSE)
                .build();

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldToggle, Toggle newToggle) {
                if (group.getSelectedToggle() != null) {
                    Boolean enabled = (Boolean) group.getSelectedToggle().getUserData();
                    PrizeAnswerUtil.getInstance().setEnabled(enabled);
                    GameControlPanel.this.slider.setDisable(!enabled);
                }
            }
        });

        return HBoxBuilder.create().children(LabelBuilder.create().text("Prize Answer Effect: ").build(), rbOn, rbOff).build();
    }

    private HBox prizeTimeControl() {

        this.slider = SliderBuilder.create()
                .min(0)
                .max(60)
                .value(30)
                .showTickLabels(true)
                .majorTickUnit(10)
                .minorTickCount(9)
                .snapToTicks(true)
                .minWidth(350)
                .disable(true)
                .build();

        final Label timeIntervalLabel = LabelBuilder.create().text(String.format("%d minute(s)", Math.round(slider.getValue()))).build();

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                timeIntervalLabel.setText(String.format("%d minute(s)", number2.intValue()));
                PrizeAnswerUtil.getInstance().setTimerInterval(number2.longValue());
            }
        });

        return HBoxBuilder.create().children(LabelBuilder.create().text("Prize Answer Interval: ").build(), slider, timeIntervalLabel).build();

    }

    private HBox soundTestControl() {

        Button wrongAnswer = ButtonBuilder.create()
                .text("Wrong Answer")
                .prefWidth(150)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        SoundEffects.playWrongAnswerSound();
                    }
                })
                .build();

        Button correctAnswer = ButtonBuilder.create()
                .text("Correct Answer")
                .prefWidth(150)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        SoundEffects.playAnswerSound();
                    }
                })
                .build();

        Button prizeAnswer = ButtonBuilder.create()
                .text("Prize Answer")
                .prefWidth(150)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        SoundEffects.playPrizeAnswerSound();
                    }
                })
                .build();

        return HBoxBuilder.create().children(LabelBuilder.create().text("Sound Test: ").build(), wrongAnswer, correctAnswer, prizeAnswer).spacing(10).build();

    }

    private void populateControlAnswerButtons() {
        List<BoardWidget.AnswerWidget> answerWidgets = this.boardWidget.getAvailableAnswerWidgets();

        this.answerButtonBox.getChildren().clear();

        for (final BoardWidget.AnswerWidget widget : answerWidgets) {

            final Button actionButton = ButtonBuilder.create()
                    .text(widget.getAnswerText().toUpperCase())
                    .prefWidth(240)
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
            resetBoardAndControls();

            Question question = quiz.nextQuestion();
            boardWidget.setQuestion(question);
            questionLabel.setText(question.getQuestion());
            populateControlAnswerButtons();

            if (quiz.isFinished()) {
                this.nextQuestionButton.setDisable(true);
            }

            updateRemainingQuestions();

        }

    }

    private void resetBoardAndControls() {
        leftBarWidget.reset();
        rightBarWidget.reset();

        leftWrongButton.setDisable(false);
        rightWrongButton.setDisable(false);
    }

    private void selectQuestion() {
        if (!quiz.isFinished()) {
            resetBoardAndControls();

            Question selectedQuestion = questionComboBox.getValue();

            if (selectedQuestion != null) {
                quiz.remove(selectedQuestion);
                boardWidget.setQuestion(selectedQuestion);
                questionLabel.setText(selectedQuestion.getQuestion());
                populateControlAnswerButtons();

                if (quiz.isFinished()) {
                    this.nextQuestionButton.setDisable(true);
                }

                updateRemainingQuestions();
            }


        }
    }

    private void updateRemainingQuestions() {
        ObservableList<Question> questions = FXCollections.observableArrayList(quiz.getRemainingQuestions());
        questionComboBox.setItems(questions);
    }

}
