package ldts.control;


import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;


import java.io.IOException;

public class Command extends Thread {
    private final Screen screen;
    private Character key = '0';

    public Command(Screen screen) {
        this.screen = screen;
    }

    public synchronized Character getKey() {
        return key;
    }

    public synchronized Character useKey(){
        Character tmp = key;
        key='0';
        return tmp;
    }

    public synchronized void changeKey(Character c){key=c;}

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                inputReader(screen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void inputReader(Screen screen) throws IOException {
        KeyStroke keyStroke = screen.readInput();
        if (keyStroke.getKeyType() == KeyType.Character)
            changeKey(keyStroke.getCharacter());
        else
            changeKey('0');
    }
}
