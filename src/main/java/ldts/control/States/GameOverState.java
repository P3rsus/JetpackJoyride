package ldts.control.States;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import ldts.control.Controller;
import ldts.control.InputObserver;
import ldts.control.States.State;
import ldts.view.GameOverView;
import ldts.view.View;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class GameOverState extends State implements InputObserver {
    private final GameOverView gameOverView;
    private boolean enterPressed = false;
    private boolean mainMenu = false;


    public GameOverState(GameOverView gameOverView) {
        this.gameOverView = gameOverView;
    }

    @SuppressWarnings("CatchAndPrintStackTrace")
    public void step() throws IOException, URISyntaxException, FontFormatException, InterruptedException {
        try {
            gameOverView.draw();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!enterPressed) {
            this.input(View.getScreen().readInput());
        }
        if (mainMenu) Controller.getInstance().runMenu();
        else Controller.getInstance().run();
    }

    public boolean isMainMenu() {
        return mainMenu;
    }

    @Override
    public void input(KeyStroke input) throws IOException, URISyntaxException, FontFormatException, InterruptedException {
        if (input.getKeyType() == KeyType.ArrowUp) {
            gameOverView.moveSelected(-1);
        } else if (input.getKeyType() == KeyType.ArrowDown) {
            gameOverView.moveSelected(1);
        }
        gameOverView.draw();
        if (input.getKeyType() == KeyType.Enter) {
            if (gameOverView.getSelected() == 1)
                mainMenu = true;
            enterPressed = true;
        }
    }


    public synchronized boolean isEnterPressed() {
        return enterPressed;
    }
}