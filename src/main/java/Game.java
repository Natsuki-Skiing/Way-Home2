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
import interfaces.*;
import java.io.Serializable;
public class Game implements Serializable {
    private Player player = null;
    public Screen screen;
    public mainGameWindow mainWindow;
    public MultiWindowTextGUI textGUI;
    private boolean running = true;
    private InputManager inputManager;
    private boolean renderWindow;
    private Terminal terminal;
    private Clock clock = null;
    private ItemController itemController;
    private CreatureController creatureController;
    private TileHolderClass tileHolder;
    private World world = null;
    private Boolean newMap = false;
    private int mapWidth = 80;
    private int mapHeight = 30;
    Random randomGen = new Random();
    public Game() {
        try {

            //Doesn't like the window's terminal so making a swing emulator if on windows
            //Only want this to happen if on windows tho
            
            DefaultTerminalFactory termFactory = new DefaultTerminalFactory();

            // if(osName.contains("win")){
            //     //We're on windows 
            //     termFactory.setForceAWTOverSwing(true);
            // }
            this.terminal = termFactory.createTerminal();
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

    


    public Game(Player player, World world, Clock clock) {
        this(); 
        this.player = player;
        this.world = world;
        this.clock = clock;
    }

    public saveData prepareSave() {
        saveData data = new saveData();
        
        // The entire player (including character/entity fields) is packed here
        data.savedPlayer = this.player;
        data.savedWorld = this.world;
        
        // Pack clock values
        data.clockSeconds = this.clock.getSecondsTime();
        data.clockMultiplier = this.clock.getMultiplier();
        data.clockSleepTime = this.clock.getSleepTime();
        
        return data;
    }

    public void loadSave(saveData data) {
        // Simply replace the current objects with the loaded ones
        this.player = data.savedPlayer;
        this.world = data.savedWorld;
        this.world.reinitialize("src/jsons/maps/maps.json", "src/jsons/world/regionTiles/tiles.json");
        this.world.restoreAllTileColors();
        // Re-initialize the clock since the background thread cannot be saved
        this.clock = new Clock(data.clockSeconds, data.clockSleepTime, data.clockMultiplier);
        this.clock.startClock();
        
        // Update the UI to show the loaded position
        this.mainWindow.setCurrentMap(this.world.getMap(this.player.getWorldX(), this.player.getWorldY()));
        this.renderWindow = true;
    }

    public  void main() {
        this.world = new World(this.mapWidth, this.mapHeight,"src/jsons/maps/maps.json");
        
       
        this.tileHolder = new TileHolderClass();
        if(this.player == null){
            //Assuming no game has be loaded using the other constructor 

             // CharacterMaker characterMaker = new CharacterMaker(this.screen,this.textGUI);
            // this.player = characterMaker.getPlayer();
            this.player = new Player("Hero",  10, 10, 10, 10, 10, 10,raceEnum.NORD,150);
            this.player.addItemToInventory(this.itemController.getItem(enums.itemTypeEnum.WEAPON), 3);
            this.player.equipItem(this.player.getInventory().getItemsByType(enums.itemTypeEnum.WEAPON).get(0).getItem());

            this.clock = new Clock(0, 2500, 200);
            this.clock.startClock();
        }
        
        
        this.creatureController = new CreatureController("src/jsons/creatures/opps.json");
       
        
        

        ///combatEncounter();
        /// 
        /// 
        ///
        //combatEncounter();
       

        // Dungeon dungeon = new Dungeon(player, creatureController, clock, this.tileHolder, 0);
        // DungeonInterface dungeonInterface = new DungeonInterface(player, dungeon, textGUI);
        // dungeonInterface.showDungeonWindow();

        this.mainWindow = new mainGameWindow(this.screen,this.textGUI,this.player,this.world.getMap(0, 0));
        this.textGUI.addWindow(this.mainWindow.getWindow());
       
    
        movePlayer(0, 0);
        
        while(this.running){
            
            if(this.renderWindow){
                try {
                    this.mainWindow.updateInfo(this.clock.getTimeString(),this.newMap);
                    this.textGUI.updateScreen();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                this.renderWindow = false;
                this.newMap = false;
            }
            KeyStroke input;
            try{
                input = this.terminal.readInput();
            }catch(Exception e){
                continue;
            }
            inputActionEnum action = this.inputManager.getInput(input);
            if(action != null){
                switch (action) {
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
                    break;
                case STATS:
                    PlayerStatusScreen statusScreen = new PlayerStatusScreen();
                    statusScreen.showStatusScreen(player, textGUI);
                    break;
                case PAUSE:
                    this.clock.pause();
                    PauseMenu pauseMenu = new PauseMenu(this, new SaverLoader());
                    pauseMenu.show(textGUI);
                    this.clock.start();
                    break;
                default:
                    this.mainWindow.getWindow().handleInput(input);
                    this.renderWindow = true;
                    break;
                }
            }else{
                this.mainWindow.getWindow().handleInput(input);
                this.renderWindow = true;
            }
            
        }
    }

    private void movePlayer(int deltaX, int deltaY) {
        
        int newX = this.player.getX() + deltaX;
        int newY = this.player.getY() + deltaY;
        this.renderWindow = true;
        this.newMap = true;
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
            if(this.randomGen.nextInt(100) < combatChance && !(deltaX ==0 && deltaY == 0)){
                combatEncounter();
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
