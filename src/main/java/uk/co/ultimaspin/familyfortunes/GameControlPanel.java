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

        this.answerButtonBox = new VBox();
        this.answerButtonBox.setSpacing(8);
        this.answerButtonBox.setMinWidth(420);
        this.answerButtonBox.setMinHeight(200);
        this.answerButtonBox.setAlignment(Pos.TOP_CENTER);

        try {
            this.quiz = new QuizFileReader("sample_quiz.csv").getQuiz();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        this.questionLabel = new Label();
        this.questionLabel.setText("Select a question to begin");
        this.questionLabel.setWrapText(true);

        this.leftWrongButton = new Button("X - LEFT");

        leftWrongButton.setOnAction(actionEvent -> {
            leftBarWidget.wrongAnswer();
            if (leftBarWidget.allWrong()) {
                leftWrongButton.setDisable(true);
            }
        });


        this.rightWrongButton = new Button("X - RIGHT");

        rightWrongButton.setOnAction(actionEvent -> {
            rightBarWidget.wrongAnswer();
            if (rightBarWidget.allWrong()) {
                rightWrongButton.setDisable(true);
            }
        });

        Button resetLeftButton = new Button("RESET");
        resetLeftButton.setOnAction(actionEvent -> {
            leftBarWidget.reset();
            leftWrongButton.setDisable(false);
        });

        Button resetRightButton = new Button("RESET");

        resetRightButton.setOnAction(actionEvent -> {
            rightBarWidget.reset();
            rightWrongButton.setDisable(false);
        });

        HBox hBox = new HBox(new VBox(8, leftWrongButton, resetLeftButton),
                answerButtonBox,
                new VBox(8, rightWrongButton, resetRightButton));
        hBox.setSpacing(10);
        hBox.setStyle("ff-board");

        this.nextQuestionButton = new Button("Next Random Question");
        this.nextQuestionButton.setOnAction(actionEvent -> nextQuestion());

        this.questionComboBox = new ComboBox<>();

        this.selectQuestionButton = new Button("Select Question");
        this.selectQuestionButton.setOnAction(actionEvent -> selectQuestion());

        HBox selectQuestionBox = new HBox(this.questionComboBox, this.selectQuestionButton);
        selectQuestionBox.setSpacing(10);

        updateRemainingQuestions();

        VBox allControls = new VBox(nextQuestionButton,
                selectQuestionBox,
                new ScrollPane(questionLabel),
                hBox,
                prizeAnswerControl(),
                prizeTimeControl(),
                soundTestControl());
        allControls.setPadding(new Insets(20, 20, 20, 20));
        allControls.setSpacing(10);

        FlowPane flowPaneContainer = new FlowPane(allControls);
        flowPaneContainer.setAlignment(Pos.TOP_CENTER);

        Stage adminStage = new Stage();
        adminStage.setTitle("Quiz Master Control Panel");
        Scene scene = new Scene(flowPaneContainer, 800, 600);
        scene.getStylesheets().add("cp.css");
        adminStage.setScene(scene);
        adminStage.sizeToScene();
        adminStage.show();

    }

    private HBox prizeAnswerControl() {
        final ToggleGroup group = new ToggleGroup();

        RadioButton rbOn = new RadioButton();
        rbOn.setToggleGroup(group);
        rbOn.setText("On");
        rbOn.setUserData(Boolean.TRUE);

        RadioButton rbOff = new RadioButton();
        rbOff.setToggleGroup(group);
        rbOff.setSelected(true);
        rbOff.setText("Off");
        rbOff.setUserData(Boolean.FALSE);

        group.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (group.getSelectedToggle() != null) {
                Boolean enabled = (Boolean) group.getSelectedToggle().getUserData();
                PrizeAnswerUtil.getInstance().setEnabled(enabled);
                GameControlPanel.this.slider.setDisable(!enabled);
            }
        });

        return new HBox(new Label("Prize Answer Effect: "), rbOn, rbOff);
    }

    private HBox prizeTimeControl() {

        this.slider = new Slider();
        this.slider.setMin(0);
        this.slider.setMax(60);
        this.slider.setValue(30);
        this.slider.setShowTickLabels(true);
        this.slider.setMajorTickUnit(10);
        this.slider.setMinorTickCount(9);
        this.slider.setSnapToTicks(true);
        this.slider.setMinWidth(350);
        this.slider.setDisable(true);

        final Label timeIntervalLabel = new Label(String.format("%d minute(s)", Math.round(slider.getValue())));

        slider.valueProperty().addListener((observableValue, number, number2) -> {
            timeIntervalLabel.setText(String.format("%d minute(s)", number2.intValue()));
            PrizeAnswerUtil.getInstance().setTimerInterval(number2.longValue());
        });

        return new HBox(new Label("Prize Answer Interval: "), slider, timeIntervalLabel);

    }

    private HBox soundTestControl() {

        Button wrongAnswer = new Button();
        wrongAnswer.setText("Wrong Answer");
        wrongAnswer.setPrefWidth(150);
        wrongAnswer.setOnAction(actionEvent -> SoundEffects.playWrongAnswerSound());

        Button correctAnswer = new Button();
        correctAnswer.setText("Correct Answer");
        correctAnswer.setPrefWidth(150);
        correctAnswer.setOnAction(actionEvent -> SoundEffects.playAnswerSound());

        Button prizeAnswer = new Button();
        prizeAnswer.setText("Prize Answer");
        prizeAnswer.setPrefWidth(150);
        prizeAnswer.setOnAction(actionEvent -> SoundEffects.playPrizeAnswerSound());

        HBox container = new HBox(new Label("Sound Test: "), wrongAnswer, correctAnswer, prizeAnswer);
        container.setSpacing(10);
        return container;
    }

    private void populateControlAnswerButtons() {
        List<BoardWidget.AnswerWidget> answerWidgets = this.boardWidget.getAvailableAnswerWidgets();

        this.answerButtonBox.getChildren().clear();

        for (final BoardWidget.AnswerWidget widget : answerWidgets) {

            final Button actionButton = new Button();
            actionButton.setText(widget.getAnswerText().toUpperCase());
            actionButton.setPrefWidth(240);

            actionButton.setOnAction(actionEvent -> {
                widget.revealAnswer();
                actionButton.setDisable(true);
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
