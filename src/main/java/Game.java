
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
import Dungeon.*;
import Combat.CombatEncounter;
import clock.*;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.util.Random;
import items.Instances.WeaponInstance;
import items.ItemManager.ItemController;
import world.*;
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
    private CreatureController creatureController;
    private TileHolderClass tileHolder;
    private World world;
    private int mapWidth = 80;
    private int mapHeight = 30;
    Random randomGen = new Random();
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
        this.world = new World(this.mapWidth, this.mapHeight,"src/jsons/maps/maps.json");
        
        // CharacterMaker characterMaker = new CharacterMaker(this.screen,this.textGUI);
        // this.player = characterMaker.getPlayer();
        this.tileHolder = new TileHolderClass();
        
        this.player = new Player("Hero",  10, 10, 10, 10, 10, 10,raceEnum.NORD,150);
        
        this.creatureController = new CreatureController("src/jsons/creatures/opps.json");
        this.player.addItemToInventory(this.itemController.getItem(enums.itemTypeEnum.WEAPON), 3);
        
        
        this.player.equipItem(this.player.getInventory().getItemsByType(enums.itemTypeEnum.WEAPON).get(0).getItem());
        ///combatEncounter();
        /// 
        /// 
        ///
        //combatEncounter();
        this.clock = new Clock(0, 2500, 200);

        // Dungeon dungeon = new Dungeon(player, creatureController, clock, this.tileHolder, 0);
        // DungeonInterface dungeonInterface = new DungeonInterface(player, dungeon, textGUI);
        // dungeonInterface.showDungeonWindow();

        this.mainWindow = new mainGameWindow(this.screen,this.textGUI,this.player,this.world.getMap(0, 0));
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
        
        int newX = this.player.getX() + deltaX;
        int newY = this.player.getY() + deltaY;
        this.renderWindow = true;

        if(newX < 0 || newX >= this.mapWidth || newY < 0 || newY >= this.mapHeight) {
            // Move to new map
            int worldX = this.player.getWorldX();
            int worldY = this.player.getWorldY();
            this.player.setX(newX);
            this.player.setY(newY);
            if(this.player.getX() < 0){
                worldX -= 1;
                this.player.setX(this.mapWidth - 1);
            }else if(this.player.getX() >= this.mapWidth){
                worldX += 1;
                this.player.setX(0);
            }
            if(this.player.getY() < 0){
                worldY -= 1;
                this.player.setY(this.mapHeight - 1);
            }else if(this.player.getY() >= this.mapHeight){
                worldY += 1;
                this.player.setY(0);
            }
            this.player.setWorldX(worldX);
            this.player.setWorldY(worldY);
            this.mainWindow.setCurrentMap(this.world.getMap(player.getWorldX(), player.getWorldY()));
        }else if(this.world.currentMapTileWalkable(this.player.getX() + deltaX, this.player.getY() + deltaY)){ //check if tile is walkable
            this.player.setX(newX);
            this.player.setY(newY);
            int combatChance = 10;
            if(this.clock.isNight()){
                combatChance += 5;
            }
            if(this.randomGen.nextInt(100) < combatChance){
                //combatEncounter();
            }
        }
        


        
        
        
        
        
    }


    private void combatEncounter(){
        // Opp enemy = new Opp("Goblin",4,5,5,5,9,6,50,null,1.2,oppInfoEnum.HUMANOID,10);
        Opp enemy = this.creatureController.getOpp(false,1.5);
        enemy.setWeapon(new WeaponInstance(new WeaponTemplate("Goblin Scythe","Wop wop wop",1.0,20,1,itemTypeEnum.WEAPON_LARGE,1000,1)));
        CombatEncounter combat = new CombatEncounter(player, enemy, textGUI);
        combat.showInterface();
        
    }
}
