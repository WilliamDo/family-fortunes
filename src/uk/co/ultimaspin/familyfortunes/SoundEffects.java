package uk.co.ultimaspin.familyfortunes;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uk.co.ultimaspin.familyfortunes.data.PrizeAnswerUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by william on 02/11/2014.
 */
public class SoundEffects {

    public static void main(String[] args) throws URISyntaxException {
        URL resource = SoundEffects.class.getClassLoader().getResource("answer.mp3");

        if (new File(resource.toURI().getPath()).exists()) {
            System.out.println("Found the music");
        } else {
            System.out.println("No music!");
        }

        Media media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playAnswerSound() {
        URL resource = SoundEffects.class.getClassLoader().getResource("ff-bell.m4a");
        Media media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void playPrizeAnswerSound() {
        URL resource = SoundEffects.class.getClassLoader().getResource("answer.mp3");
        Media media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }


    public static void playWrongAnswerSound() {
        URL resource = SoundEffects.class.getClassLoader().getResource("wrong-actual.mp3");
        Media media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

}
