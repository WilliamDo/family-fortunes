package uk.co.ultimaspin.familyfortunes;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String GAME_TITLE = "Family Fortunes";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;


    @Override
    public void start(Stage primaryStage) throws Exception{

        final VBox answerBox = VBoxBuilder.create()
                .spacing(10)
                .padding(new Insets(40, 0, 0, 0))
                .build();

        final Label questionLabel = LabelBuilder.create()
                .text("Ready to start")
                .textFill(Color.WHITE)
                .font(Font.font(null, 20))
                .build();

        final Node questionContainer = VBoxBuilder.create()
                .children(questionLabel)
                .alignment(Pos.CENTER)
                .padding(new Insets(0, 0, 20, 0))
                .build();

        final BorderPane borderPane = BorderPaneBuilder.create().build();
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
        VBox container = VBoxBuilder.create()
                .spacing(10)
                .padding(new Insets(40, 20, 10, 20))
                .build();

        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
