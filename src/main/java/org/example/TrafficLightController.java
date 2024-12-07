package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficLightController {
    TrafficLight northSouthLight;
    TrafficLight eastWestLight;
    private final Lock lock = new ReentrantLock();

    public TrafficLightController() {

        northSouthLight = new TrafficLight(TrafficLight.State.RED, 7000, 2000, 5000);
        eastWestLight = new TrafficLight(TrafficLight.State.GREEN, 7000, 2000, 5000);
    }

    public void startSimulation() {
        Thread northSouthThread = new Thread(() -> runTrafficLight(northSouthLight, eastWestLight));
        Thread eastWestThread = new Thread(() -> runTrafficLight(eastWestLight, northSouthLight));

        northSouthThread.start();
        eastWestThread.start();
    }

    private void runTrafficLight(TrafficLight light, TrafficLight conflictingLight) {
        while (true) {
            lock.lock(); // Synchronize access to avoid conflicting green states
            try {

                // Si le feu actuel est VERT, forcer l'autre feu à rester en RED
                if (light.getCurrentState() == TrafficLight.State.GREEN) {
                    conflictingLight.setCurrentState(TrafficLight.State.RED);
                }
            } finally {
                lock.unlock();
            }

            light.runLight();


        }
    }

}
