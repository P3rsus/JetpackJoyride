package ldts.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RocketTest {

    @Test
    public void moveRocket()  {
        Rocket rocket = new Rocket();
        int x = rocket.getX() - 1;
        int y = rocket.getY();
        rocket.move(-1, 0);
        Assertions.assertEquals(x, rocket.getX());
        Assertions.assertEquals(y, rocket.getY());
    }

    @Test
    public void type()  {
        Rocket rocket = new Rocket();
        Assertions.assertFalse(rocket.isLaser());
        Assertions.assertFalse(rocket.isCoin());
        Assertions.assertTrue(rocket.isRocket());
    }

    @Test
    public void getPosition() {
        int x = 60;
        int y = (int) (Math.random() * (18 - 2)) + 2;
        Rocket rocket = new Rocket(x, y);
        Position position = new Position(x, y);
        Assertions.assertEquals(position, rocket.getPosition());
    }

    @Test
    public void constructor()  {
        Rocket rocket = new Rocket();
        Assertions.assertTrue(rocket.getY() >= 2 && rocket.getY() <= 18);
        Assertions.assertEquals(51, rocket.getX());
    }

    @Test
    public void sets() {
        Rocket rocket = new Rocket();
        int x = 10;
        int y = 10;
        rocket.setX(x);
        rocket.setY(y);
        Assertions.assertEquals(x, rocket.getPosition().getX());
        Assertions.assertEquals(y, rocket.getPosition().getY());
    }

    @Test
    public void gets() {
        Rocket rocket = new Rocket(10, 10);
        Assertions.assertEquals(10, rocket.getX());
        Assertions.assertEquals(10, rocket.getY());
    }
}
