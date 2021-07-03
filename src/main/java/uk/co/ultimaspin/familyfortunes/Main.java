package uk.co.ultimaspin.familyfortunes;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String GAME_TITLE = "Family Fortunes";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;


    @Override
    public void start(Stage primaryStage) {

        final VBox answerBox = new VBox();
        answerBox.setSpacing(10);
        answerBox.setPadding(new Insets(40, 0, 0, 0));

        final Label questionLabel = new Label();
        questionLabel.setText("Ready to start");
        questionLabel.setTextFill(Color.WHITE);
        questionLabel.setFont(Font.font(null, 20));

        final VBox questionContainer = new VBox(questionLabel);
        questionContainer.setAlignment(Pos.CENTER);
        questionContainer.setPadding(new Insets(0, 0, 20, 0));

        final BorderPane borderPane = new BorderPane();
        borderPane.setCenter(answerBox);
        borderPane.setBottom(questionContainer);

        VBox leftBar = createBar();
        VBox rightBar = createBar();

        borderPane.setLeft(leftBar);
        borderPane.setRight(rightBar);

        borderPane.setId("ff-main");

        WrongAnswerBarWidget leftBarWidget = new WrongAnswerBarWidget(leftBar);
        WrongAnswerBarWidget rightBarWidget = new WrongAnswerBarWidget(rightBar);
        BoardWidget boardWidget = new BoardWidget(answerBox, questionLabel);

        primaryStage.setTitle(GAME_TITLE);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        scene.getStylesheets().add("cp.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        new GameControlPanel(boardWidget, leftBarWidget, rightBarWidget);
    }

    private VBox createBar() {
        VBox container = new VBox();
        container.setSpacing(10);
        container.setPadding(new Insets(40, 20, 10, 20));
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
