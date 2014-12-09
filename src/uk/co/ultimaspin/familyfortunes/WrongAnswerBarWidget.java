package uk.co.ultimaspin.familyfortunes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by william on 15/11/2014.
 */
public class WrongAnswerBarWidget {


    public static final int CIRCLE_RADIUS = 45;
    public static final int TEXT_SIZE = 45;
    Node[] circles;


    public WrongAnswerBarWidget(VBox container) {
        circles = new Node[] {createCircle(), createCircle(), createCircle()};
        container.getChildren().setAll(circles);

    }


    private Node createCircle() {
        Circle circle = CircleBuilder.create().radius(CIRCLE_RADIUS).styleClass("ff-wrong").build();

        Label label = LabelBuilder.create()
                .text("X")
                .textFill(Color.WHITE)
                .font(Font.font(null, FontWeight.BOLD, TEXT_SIZE))
                .build();

        StackPane node = StackPaneBuilder.create()
                .children(circle, label)
                .visible(false)
                .build();

        return node;

    }

    public void wrongAnswer() {
        for (Node n : circles) {
            if (!n.isVisible()) {
                n.setVisible(true);
                SoundEffects.playWrongAnswerSound();
                break;
            }
        }
    }

    public void reset() {
        for (Node n : circles) {
            n.setVisible(false);
        }
    }

    public boolean allWrong() {
        boolean result = true;
        for (Node n : circles) {
            result = result && n.isVisible();
        }
        return result;
    }

}
