package uk.co.ultimaspin.familyfortunes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by william on 15/11/2014.
 */
public class WrongAnswerBarWidget {



    Node[] circles;


    public WrongAnswerBarWidget(VBox container) {
        circles = new Node[] {createCircle(), createCircle(), createCircle()};
        container.getChildren().setAll(circles);

    }


    private Node createCircle() {
        Circle circle = new Circle();
        circle.setRadius(60);
        circle.setStyle("ff-wrong");

        Label label = new Label();
                label.setText("X");
                label.setTextFill(Color.WHITE);
                label.setFont(Font.font(null, FontWeight.BOLD, 60));

        StackPane node = new StackPane(circle, label);
        node.setVisible(false);

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
