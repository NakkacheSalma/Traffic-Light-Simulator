package org.example;

/*
TrafficLight:
    Représente un feu de circulation individuel.
Contient :
    L’état actuel du feu (Rouge, Jaune, ou Vert).
    La logique pour passer à l’état suivant.
 */

import javafx.application.Platform;

public class TrafficLight {
    enum State { RED, YELLOW, GREEN }

    private State currentState;
    private int redDuration, yellowDuration, greenDuration;
    private Runnable uiUpdater;

    public TrafficLight(State initialState, int redDuration, int yellowDuration, int greenDuration) {
        this.currentState = initialState;
        this.redDuration = redDuration;
        this.yellowDuration = yellowDuration;
        this.greenDuration = greenDuration;
    }

    public synchronized State getCurrentState() {
        return currentState;
    }

    public synchronized void setCurrentState(State newState) {
        this.currentState = newState;
        if (uiUpdater != null) {
            Platform.runLater(uiUpdater); // Update UI
        }
    }

    public void setUiUpdater(Runnable uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    public void runLight() {
        try {
            switch (currentState) {
                case RED :
                    Thread.sleep(redDuration);
                    nextState();
                case GREEN :
                    Thread.sleep(greenDuration);
                    nextState();
                case YELLOW :
                    Thread.sleep(yellowDuration);
                    nextState();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void nextState() {
        switch (currentState) {
            case RED -> {
                setCurrentState(State.GREEN);
            }
            case GREEN -> {
                setCurrentState(State.YELLOW);
            }
            case YELLOW -> {
                setCurrentState(State.RED);
            }
        }
    }

}
