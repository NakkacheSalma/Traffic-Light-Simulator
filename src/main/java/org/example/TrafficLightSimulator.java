package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TrafficLightSimulator extends Application {

    private TrafficLightController controller;


    private Circle northRed, northYellow, northGreen;
    private Circle southRed, southYellow, southGreen;
    private Circle eastRed, eastYellow, eastGreen;
    private Circle westRed, westYellow, westGreen;

    @Override
    public void start(Stage primaryStage) {

        controller = new TrafficLightController();


        GridPane grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(50);


        VBox northUI = createTrafficLightUI();
        VBox southUI = createTrafficLightUI();
        VBox eastUI = createTrafficLightUI();
        VBox westUI = createTrafficLightUI();


        grid.add(northUI, 1, 0); // top center (1, 0)
        grid.add(southUI, 1, 2); // bottom center (1, 2)
        grid.add(eastUI, 0, 1);  // left center (0, 1)
        grid.add(westUI, 2, 1);  // right center (2, 1)


        bindTrafficLightToUI(controller);


        new Thread(() -> controller.startSimulation()).start();


        Scene scene = new Scene(grid, 300, 600);
        primaryStage.setTitle("Traffic Light Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTrafficLightUI() {

        Circle red = new Circle(20, Color.GRAY);
        Circle yellow = new Circle(20, Color.GRAY);
        Circle green = new Circle(20, Color.GRAY);

        VBox lightBox = new VBox(10);
        lightBox.getChildren().addAll(red, yellow, green);


        if (northRed == null) {
            northRed = red;
            northYellow = yellow;
            northGreen = green;
        } else if (southRed == null) {
            southRed = red;
            southYellow = yellow;
            southGreen = green;
        } else if (eastRed == null) {
            eastRed = red;
            eastYellow = yellow;
            eastGreen = green;
        } else {
            westRed = red;
            westYellow = yellow;
            westGreen = green;
        }

        return lightBox;
    }

    private void bindTrafficLightToUI(TrafficLightController controller) {
        // Thread for updating the North light
        new Thread(() -> {
            while (true) {
                TrafficLight.State state = controller.northSouthLight.getCurrentState();
                Platform.runLater(() -> updateLightUI(northRed, northYellow, northGreen, state));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Thread for updating the South light
        new Thread(() -> {
            while (true) {
                TrafficLight.State state = controller.northSouthLight.getCurrentState();
                Platform.runLater(() -> updateLightUI(southRed, southYellow, southGreen, state));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Thread for updating the East light
        new Thread(() -> {
            while (true) {
                TrafficLight.State state = controller.eastWestLight.getCurrentState();
                Platform.runLater(() -> updateLightUI(eastRed, eastYellow, eastGreen, state));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Thread for updating the West light
        new Thread(() -> {
            while (true) {
                TrafficLight.State state = controller.eastWestLight.getCurrentState();
                Platform.runLater(() -> updateLightUI(westRed, westYellow, westGreen, state));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateLightUI(Circle red, Circle yellow, Circle green, TrafficLight.State state) {
        red.setFill(state == TrafficLight.State.RED ? Color.RED : Color.GRAY);
        yellow.setFill(state == TrafficLight.State.YELLOW ? Color.YELLOW : Color.GRAY);
        green.setFill(state == TrafficLight.State.GREEN ? Color.GREEN : Color.GRAY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
