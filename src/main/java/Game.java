
import interfaces.*;
import creatures.*;
import enums.raceEnum;
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
    public mainGameWindow mainWindow;
    public MultiWindowTextGUI textGUI;
    public Game() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(terminal);
            this.screen.startScreen();
            this.screen.setCursorPosition(null);
            this.textGUI = new MultiWindowTextGUI(this.screen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void main() {
        //CharacterMaker characterMaker = new CharacterMaker(this.screen,this.textGUI);
        
        //this.player = characterMaker.getPlayer();
        this.player = new Player("Hero",  10, 10, 10, 10, 10, 10,raceEnum.NORD,150);
        System.out.println("Character Maker closed. Player created: " + (this.player != null));     
        this.mainWindow = new mainGameWindow(this.screen,this.textGUI,this.player);
        

    }
}
