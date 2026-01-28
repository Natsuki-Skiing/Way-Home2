
import interfaces.*;
import items.templates.WeaponTemplate;
import creatures.*;
import enums.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import Combat.CombatEncounter;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.util.regex.Pattern;
import java.math.BigDecimal;

import items.Instances.WeaponInstance;
import items.ItemManager.ItemController;
public class Game {
    private Player player;
    public Screen screen;
    public mainGameWindow mainWindow;
    public MultiWindowTextGUI textGUI;
    private boolean running = true;
    private InputManager inputManager;
    private boolean renderWindow;
    private Terminal terminal;
    private Clock clock;
    private ItemController itemController;
    public Game() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.terminal = terminal;
            this.screen = new TerminalScreen(terminal);
            this.screen.startScreen();
            this.screen.setCursorPosition(null);
            this.textGUI = new MultiWindowTextGUI(this.screen);
            this.inputManager = new InputManager(terminal);
            this.renderWindow = false;
            this.itemController = new ItemController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void main() {
        //CharacterMaker characterMaker = new CharacterMaker(this.screen,this.textGUI);
        //this.player = characterMaker.getPlayer();
        
        this.player = new Player("Hero",  10, 10, 10, 10, 10, 10,raceEnum.NORD,150);
        
       
        this.player.addItemToInventory(this.itemController.getItem(enums.itemTypeEnum.WEAPON), 3);
        
        
        this.player.equipItem(this.player.getInventory().getItemsByType(enums.itemTypeEnum.WEAPON).get(0).getItem());
        ///combatEncounter();
        this.clock = new Clock(0, 2500, 200);
        this.mainWindow = new mainGameWindow(this.screen,this.textGUI,this.player);
        this.textGUI.addWindow(this.mainWindow.getWindow());
       
    
        movePlayer(0, 0);
        this.clock.startClock();
        while(this.running){
            
            if(this.renderWindow){
                try {
                    this.mainWindow.updateInfo(this.clock.getTimeString());
                    this.textGUI.updateScreen();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                this.renderWindow = false;
            }
            KeyStroke input;
            try{
                input = this.terminal.readInput();
            }catch(Exception e){
                continue;
            }
            
            switch (this.inputManager.getInput(input)) {
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
                case INVENTORY:
                    this.clock.updateTime();
                    InventoryInterface invScreen = new InventoryInterface(player, screen, textGUI);
                    //invScreen.mainLoop();
                    invScreen.show();
                case STATS:
                    break;
                default:
                    this.mainWindow.getWindow().handleInput(input);
                    this.renderWindow = true;
                    break;
            }
        }
    }

    private void movePlayer(int deltaX, int deltaY) {
        this.player.setX(this.player.getX() + deltaX);
        this.player.setY(this.player.getY() + deltaY);  
        this.renderWindow = true;
        
        
        
    }


    private void combatEncounter(){
        Entity enemy = new Entity("Goblin",5,5,5,5,5,5,50);
        CombatEncounter combat = new CombatEncounter(player, enemy, textGUI);
    }
}
