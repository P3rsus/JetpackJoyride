package ldts.control;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import ldts.model.*;
import ldts.view.*;


import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;



public class Controller {
    private boolean gameOver = false;
    private Player player;
    private PlayerView playerView;
    private BackgroundView backgroundView;
    private GameOverView gameOverView;
    private ArrayList<Obstacle> obstacles;
    private RocketView rocketView;
    private LaserView laserView;
    private static final int LOWER_LIMIT = 1;
    private static Controller singleton = null;
    private DistanceCounterView distanceCounterView;
    private int timePerFrame = 1000 / 15;

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }

    public void setBackgroundView(BackgroundView backgroundView) {
        this.backgroundView = backgroundView;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public boolean checkCollisions(ArrayList<Obstacle> obstacles, Player player)
    {
        boolean collision = false;
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isLaser()) {
                // Laser Collision
                if (obstacle.getPosition().getX() <= player.getPosition().getX() && player.getPosition().getX() <= obstacle.getLastPosition().getX()) {
                    int m = 0;
                    if (obstacle.getPosition().getX() == obstacle.getLastPosition().getX() && obstacle.getLastPosition().getY() <= player.getPosition().getY() && player.getPosition().getY() <= obstacle.getPosition().getY())
                        collision = true;
                    else {
                        if (obstacle.getPosition().getY() > obstacle.getLastPosition().getY()) m = -1;
                        else if (obstacle.getPosition().getY() < obstacle.getLastPosition().getY()) m = 1;

                        int b = obstacle.getPosition().getY() - m * obstacle.getPosition().getX();

                        if (player.getPosition().getX() * m + b == player.getPosition().getY()) collision = true;
                    }

                }
            } else {
                // Rocket Collision
                Position temp = new Position(obstacle.getX() + 1, obstacle.getPosition().getY());
                if (obstacle.getPosition().equals(player.getPosition()) || temp.equals(player.getPosition()))
                    collision = true;
            }
        }
        return collision;
    }

    public Controller() throws IOException, URISyntaxException, FontFormatException {
        player = new Player();
        playerView = new PlayerView("#000C66", "#FFFF33", "!");
        backgroundView = new BackgroundView("#808080", "#000C66", ' ', ' ', LOWER_LIMIT);
        gameOverView = new GameOverView();
        rocketView = new RocketView("#000C66", "#FF1F1F", "$%");
        laserView = new LaserView("#336699", ' ');
        obstacles = new ArrayList<>();
        distanceCounterView = new DistanceCounterView("#808080", "#000000");
    }

    public static Controller getInstance() throws IOException, URISyntaxException, FontFormatException {
        if (singleton == null)
            singleton = new Controller();
        return singleton;
    }

    public void generateObstacles(int i){
        if (i % 15 == 0) {
            int random = (int) (Math.random() * (5 - 1)) + 1;
            if (random < 4) obstacles.add(new Laser());
            else obstacles.add(new Rocket());
        }
    }

    public void drawElements(int xMin) throws IOException {
        View.getScreen().clear();
        backgroundView.draw(new Position(0, LOWER_LIMIT), xMin);
        playerView.draw(player.getPosition());
        for (Obstacle obstacle : obstacles) {
            obstacle.move(-1, 0);
            if (obstacle.isLaser()) laserView.draw(obstacle.getPosition(), obstacle.getLastPosition());
            else rocketView.draw(obstacle.getPosition());
        }
        distanceCounterView.draw(xMin);
        View.getScreen().refresh();
    }

    public void run() throws IOException, InterruptedException, URISyntaxException, FontFormatException {
        boolean replay;
        View.initScreen();
        do {
            replay = false;
            Command command = new Command(View.getScreen());
            command.start();
            int xMin = 0, i = 0;
            while (!gameOver) {
                long startTime = System.currentTimeMillis();
                Character keyPressed = command.useKey();
                if (keyPressed == ' ') {
                    if (player.getPosition().getY() < View.getScreen().getTerminalSize().getRows())
                        player.goHigher();
                } else if (keyPressed == 'q') {
                    gameOver = true;
                } else if (player.getPosition().getY() > LOWER_LIMIT + 1) {
                    player.goLower();
                }
                generateObstacles(i);
                drawElements(xMin);
                if (checkCollisions(obstacles, player)) {
                    gameOver = true;
                }
                xMin++;
                i++;
                long finalTime = System.currentTimeMillis();
                Thread.sleep(timePerFrame-(finalTime - startTime));
            }
            command.interrupt();
            while (gameOver) {
                gameOverView.draw(null);
                KeyStroke x = View.getScreen().readInput();
                if (x.getKeyType() == KeyType.ArrowUp) {
                    gameOverView.moveSelected(-1);
                } else if (x.getKeyType() == KeyType.ArrowDown) {
                    gameOverView.moveSelected(1);
                } else if (x.getKeyType() == KeyType.Enter) {
                    if (gameOverView.getSelected() == 1)
                        System.exit(0);
                    replay = true;
                    gameOver = false;
                    break;
                }
            }
            resetElements();
        }while(replay);
    }

    private void resetElements() throws IOException, URISyntaxException, FontFormatException {
        player = new Player();
        obstacles = new ArrayList<>();
    }
}
