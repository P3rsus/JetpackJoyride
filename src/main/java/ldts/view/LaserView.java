package ldts.view;

import com.googlecode.lanterna.TextColor;
import ldts.model.Position;

import java.io.IOException;

public class LaserView extends View {
    private final TextColor laserColor;
    private final char character;

    public LaserView(String laserColor, char character) {
        this.laserColor = stringToColor(laserColor);
        this.character = character;
    }

    public void draw(Position pos1, Position pos2) throws IOException {
        graphics.setBackgroundColor(laserColor);
        graphics.drawLine(pos1.getX(), numberRows - pos1.getY(), pos2.getX(), numberRows - pos2.getY(), character);
    }
}
