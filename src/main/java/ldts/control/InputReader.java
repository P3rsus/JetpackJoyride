package ldts.control;


import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class InputReader extends Thread {
    private final Screen screen;
    private final ArrayList<InputObserver> observers;
    private boolean stopInputs;

    public InputReader(Screen screen) {
        this.screen = screen;
        observers = new ArrayList<>();
        stopInputs = false;
    }

    public synchronized void addObserver(InputObserver obs) {
        observers.add(obs);
        stopInputs = false;
    }

    public synchronized void notify(KeyStroke c) throws IOException, URISyntaxException, InterruptedException, FontFormatException {
        for (InputObserver observer : observers)
            observer.input(c);
    }

    @Override
    public void run() {
        while (!stopInputs) {
            try {
                inputReader(screen);
            } catch (IOException | URISyntaxException | InterruptedException | FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void inputReader(Screen screen) throws IOException, URISyntaxException, InterruptedException, FontFormatException {
        KeyStroke keyStroke = screen.readInput();
        notify(keyStroke);
    }

    public void clear() throws AWTException {
        stopInputs = true;
        Robot robot = new Robot();
        robot.keyPress(48);
        observers.clear();
    }
}