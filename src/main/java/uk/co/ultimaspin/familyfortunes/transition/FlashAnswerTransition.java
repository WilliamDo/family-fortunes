package uk.co.ultimaspin.familyfortunes.transition;

import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Created by william on 06/12/2014.
 */
public class FlashAnswerTransition extends Transition {

    final Rectangle rectangle;
    final String styleClassSet;
    final String styleClassFlash;


    public FlashAnswerTransition(Rectangle rectangle, String styleClassSet, String styleClassFlash, Duration duration) {
        this.rectangle = rectangle;
        this.styleClassSet = styleClassSet;
        this.styleClassFlash = styleClassFlash;
        setCycleDuration(duration);

    }

    @Override
    protected void interpolate(double v) {
        if (v > 0.5) {
            rectangle.getStyleClass().clear();
            rectangle.getStyleClass().add(styleClassSet);
        } else {
            rectangle.getStyleClass().clear();
            rectangle.getStyleClass().add(styleClassFlash);
        }
    }
}
