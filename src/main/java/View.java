

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class View {
    protected static final int COLUMNS = 60;
    protected static final int ROWS = 18;
    static protected Screen screen;
    static protected TextGraphics graphics;

    public void initScreen() throws IOException, FontFormatException, URISyntaxException {


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setForceAWTOverSwing(true);
        factory.setInitialTerminalSize(new TerminalSize(COLUMNS, ROWS));
        Terminal terminal = factory.createTerminal();
        ((AWTTerminalFrame)terminal).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });

        screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);   // we don't need a cursor
        screen.startScreen();             // screens must be started
        screen.doResizeIfNecessary();     // resize screen if necessary
        graphics = screen.newTextGraphics();
    }

    protected void refresh() throws IOException {
        screen.refresh();
    }
    public abstract void draw(Position pos) throws IOException;

    public Screen getScreen() {
        return screen;
    }
}