
import interfaces.*;
import creatures.*;
import enums.*;
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
    private Player player;
    public Screen screen;
    public mainGameWindow mainWindow;
    public MultiWindowTextGUI textGUI;
    private boolean running = true;
    private InputManager inputManager;
    private boolean renderWindow;
    public Game() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(terminal);
            this.screen.startScreen();
            this.screen.setCursorPosition(null);
            this.textGUI = new MultiWindowTextGUI(this.screen);
            this.inputManager = new InputManager(terminal);
            this.renderWindow = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void main() {
        CharacterMaker characterMaker = new CharacterMaker(this.screen,this.textGUI);
        
        this.player = characterMaker.getPlayer();
        //this.player = new Player("Hero",  10, 10, 10, 10, 10, 10,raceEnum.NORD,150);
           
        this.mainWindow = new mainGameWindow(this.screen,this.textGUI,this.player);
        this.textGUI.addWindow(this.mainWindow.getWindow());
       
    
        movePlayer(0, 0);

        while(this.running){
            if(this.renderWindow){
                try {
                    this.textGUI.updateScreen();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                this.renderWindow = false;
            }
            switch (this.inputManager.getInput()) {
                case UP:
                    this.movePlayer(0, -1);
                    break;
                case DOWN:
                    this.movePlayer(0, 1);
                    break;
                case LEFT:
                    this.movePlayer(-1, 0);
                    break;
                case RIGHT:
                    this.movePlayer(1, 0);
                    break;

                default:
                    break;
            }
        }
    }

    private GameTile movePlayer(int deltaX, int deltaY) {
        int newX = this.player.getX() + deltaX;
        int newY = this.player.getY() + deltaY;
        GameTile targetTile = this.mainWindow.getTile(newX, newY);
        if (targetTile != null) {
            if(this.player.getPreviousTile()!= null){
                this.mainWindow.setTile(this.player.getX(), this.player.getY(), this.player.getPreviousTile());
            }
            
            this.player.setPreviousTile(this.mainWindow.getTile(this.player.getX(), this.player.getY()));
            this.player.setX(newX);
            this.player.setY(newY);
            this.mainWindow.setTile(newX, newY, this.player.getPlayerTile());
            this.renderWindow = true;
            return targetTile;
        }
        return null;
    }
}
