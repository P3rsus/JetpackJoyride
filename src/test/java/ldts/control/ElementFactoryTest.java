package ldts.control;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import ldts.view.View;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ElementFactoryTest {

    @Test
    public void generateElements() {
        Screen screen = Mockito.mock(Screen.class);
        View.setScreen(screen);
        Mockito.when(screen.getTerminalSize()).thenReturn(new TerminalSize(51, 18));

        ElementFactory creator = new ElementFactory(5);

        for (int i = 0; i <= 10; i++) {
            if (!(i%5==0)) {
                Assertions.assertNull(creator.generateElements(i));
            }
            else {
                Assertions.assertNotNull(creator.generateElements(i));
            }
        }
    }
}
