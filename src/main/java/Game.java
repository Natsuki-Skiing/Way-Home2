
import interfaces.*;
import creatures.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.util.regex.Pattern;
public class Game {
    public Player player;
    public Screen screen;


    public Game() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(terminal);
            this.screen.startScreen();
            this.screen.setCursorPosition(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void main() {
        CharacterMaker characterMaker = new CharacterMaker(this.screen);
        
        this.player = characterMaker.getPlayer();
        
        

    }
}
