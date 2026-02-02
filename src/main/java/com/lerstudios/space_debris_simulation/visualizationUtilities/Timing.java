package com.lerstudios.space_debris_simulation.visualizationUtilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class Timing {

    private final Label timeDisplay; // Top time display text
    private final Label timeTextDisplay; // Bottom time display text

    private static double elapsedSeconds;

    private static Timeline timer;
    private static boolean paused = true;

    private static int minSeconds = 0;
    private static int maxSeconds = 5 * 365 * 24 * 60 * 60;

    private static double minSpeed = -10000000;
    private static double maxSpeed = 10000000;
    private static final double updateSpeed = 0.05; // UI Updates / Second
    private static double speed = 1; // Time multiplier. 1 is regular speed.

    public Timing(Consumer<Double> updateScene, Label timeDisplay, Label timeTextDisplay, Button pausePlayButton, Button speedButton, Button slowButton,
                  Button startButton, Button endButton) {
        this.timeDisplay = timeDisplay;
        this.timeTextDisplay = timeTextDisplay;

        pausePlayButton.setOnAction(e -> pausePlayButtonEvent());
        speedButton.setOnAction(e -> increaseSpeed());
        slowButton.setOnAction(e -> decreaseSpeed());
        startButton.setOnAction(e -> goToStart());
        endButton.setOnAction(e -> goToEnd());

        timer = new Timeline(
                new KeyFrame(Duration.seconds(Timing.updateSpeed), e -> {
                    elapsedSeconds += Timing.speed * Timing.updateSpeed;

                    if(elapsedSeconds < minSeconds) {
                        elapsedSeconds = minSeconds;
                    } if(elapsedSeconds > maxSeconds) {
                        elapsedSeconds = maxSeconds;
                    }

                    this.updateDisplays();
                    updateScene.accept(elapsedSeconds);
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    // Does not consider leap-years :|
    public void updateDisplays() {

        double seconds = elapsedSeconds;

        int years = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;

        while(seconds >= 365 * 24 * 60 * 60) {
            years++;
            seconds -= 365 * 24 * 60 * 60;
        }

        while(seconds >= 24 * 60 * 60) {
            days++;
            seconds -= 24 * 60 * 60;
        }

        while(seconds >= 60 * 60) {
            hours++;
            seconds -= 60 * 60;
        }

        while(seconds >= 60) {
            minutes++;
            seconds -= 60;
        }

        DecimalFormat df = new DecimalFormat("0");
        DecimalFormat df2 = new DecimalFormat("00");
        DecimalFormat df3 = new DecimalFormat("000");

        String yearString = df3.format(years);
        String dayString = df3.format(days);
        String hourString = df2.format(hours);
        String minuteString = df2.format(minutes);
        String secondString = df2.format((int) seconds);

        String speedString = df.format(Timing.speed);

        this.timeDisplay.setText(yearString + ":" + dayString + ":" + hourString + ":" + minuteString + ":" + secondString);

        String timeString;

        if(years > 0) {
            timeString = yearString + " years, " + dayString + " days";
        } else {
            timeString = dayString + " days, " + hourString + " hours";
        }

        this.timeTextDisplay.setText(timeString + "  |  " + speedString + "x");
    }

    public static void pause() {
        timer.pause();
        paused = true;
    }

    public static void play() {
        timer.play();
        paused = false;
    }

    public static boolean getPaused() {
        return paused;
    }

    public void increaseSpeed() {
        if(Timing.speed == -1) {
            Timing.speed = 1;
            this.updateDisplays();
            return;
        }

        if(Timing.speed > 0) {
            if(Timing.speed < Timing.maxSpeed) {
                Timing.speed *= 10;
                this.updateDisplays();
            }
            return;
        }

        Timing.speed /= 10;
        this.updateDisplays();
    }

    public void decreaseSpeed() {
        if(Timing.speed == 1) {
            Timing.speed = -1;
            this.updateDisplays();
            return;
        }

        if(Timing.speed < 0) {
            if(Timing.speed > Timing.minSpeed) {
                Timing.speed *= 10;
                this.updateDisplays();
            }
            return;
        }

        Timing.speed /= 10;
        this.updateDisplays();
    }

    public void goToStart() {
        Timing.elapsedSeconds = minSeconds;
        this.updateDisplays();
    }

    public void goToEnd() {
        Timing.elapsedSeconds = maxSeconds;
        this.updateDisplays();
    }

    private void pausePlayButtonEvent() {
        if (Timing.getPaused()) {
            Timing.play();
            return;
        }

        Timing.pause();
    }

    public static void setMaxSeconds(int seconds) {
        Timing.maxSeconds = seconds;
    }
}