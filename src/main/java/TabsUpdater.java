package main.java;

import main.java.windows.ControlWindow;

public class TabsUpdater implements Runnable{
    public volatile boolean running = true;

    ControlWindow controlWindow;

    public void start() {
        new Thread(this).start();
    }

    public TabsUpdater(ControlWindow controlWindow) {
        this.controlWindow = controlWindow;
    }

    @Override
    public void run() {
        while(running) {
            try {
                Thread.sleep(500);
                controlWindow.updateAirportTab();
                controlWindow.updatePlanesTab();
                controlWindow.updateShipsTab();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
