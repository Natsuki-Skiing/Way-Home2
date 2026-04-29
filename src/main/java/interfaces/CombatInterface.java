package interfaces;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Vector;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.table.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import Combat.*;
import creatures.*;
import enums.combatInfoEnum;
import enums.effectCounterType;
import items.Instances.WeaponInstance;

public class CombatInterface {
    private HealthBar playerHealthBar;
    private HealthBar enemyHealthBar;
    private Player player;
    private Opp enemy;
    private WindowBasedTextGUI textGUI;
    private Window window;
    private Panel rootPanel;
    private Panel logPanel;
    private Panel actionPanel;
    private Panel healthBarPanel; 
    private TextBox logTextBox;
    private Boolean running = false;
    private ConditionBar weaponConditionBar;
    private WeaponInstance playerWeapon;
    private Button attackButton;
    private Button fleeButton;
    private Button useItemButton;
    private Button endTurnButton;
    private Button changeWeaponButton;
    private CombatEncounter combatEngine;
    private Boolean playerTurn;
    private Random randGen = new Random();
    public CombatInterface(CombatEncounter combatEngine,WindowBasedTextGUI textGUI) {
        this.combatEngine = combatEngine;
        this.player = combatEngine.getPlayer();
        this.enemy = combatEngine.getOpp();
        this.playerWeapon = player.getEquippedWeapon();
        this.playerTurn = true;
        this.textGUI = textGUI;
       
        this.window = new BasicWindow("Combat");

        this.rootPanel = new Panel();
        this.logPanel = new Panel();
        this.actionPanel = new Panel();
        this.healthBarPanel = new Panel();
        
        this.playerHealthBar = new HealthBar(player);
        this.enemyHealthBar = new HealthBar(enemy);

        this.weaponConditionBar = new ConditionBar(this.playerWeapon);

        this.attackButton = new Button("Attack", new Runnable() {
            @Override
            public void run() {
                if(playerTurn){
                    addLogMessage(player.getName() + " attacks " + enemy.getName() + " with " + playerWeapon.getDisplayName());
                    attack();
                    playerTurn = false;
                    oppTurn();
                }
                
            }
        });
        this.fleeButton = new Button("Flee", new Runnable() {
            @Override
            public void run() {
                if(playerTurn){
                    addLogMessage(player.getName() + " attempts to flee!");
                    if(combatEngine.flee()){
                        addLogMessage( randomString(new String[]{"You Flee!","You manage to escape the "+enemy.getName(),"You run away","You are a coward, but a lucky one"}));
                        try {
                            // 2000 milliseconds = 2 seconds
                            Thread.sleep(2000); 
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        running = false;
                        window.close();
                    }else{
                        addLogMessage("");
                        playerTurn = false;
                        oppTurn();
                    }
                }
                
            }
        });
        this.useItemButton = new Button("Use Item", new Runnable() {
            @Override
            public void run() {
                if(playerTurn){
                    addLogMessage(player.getName() + " uses an item!");
                    playerTurn = false;
                    oppTurn();
                // Use item logic would go here
                }
                
            }
        });
        this.endTurnButton = new Button("End Turn", new Runnable() {
            @Override
            public void run() {
                if(playerTurn){
                    addLogMessage(player.getName() + " ends their turn.");
                    playerTurn = false;
                    oppTurn();
                }
                
                // End turn logic would go here
            }
        });
        this.changeWeaponButton = new Button("Change Weapon", new Runnable() {
            @Override
            public void run() {
                if(playerTurn){
                    addLogMessage(player.getName() + " changes weapon.");
                    playerTurn = false;
                    oppTurn();
                    // Change weapon logic would go here
                }
                
            }
        }); 

        this.actionPanel.addComponent(this.attackButton);
        this.actionPanel.addComponent(this.fleeButton);
        this.actionPanel.addComponent(this.useItemButton);
        this.actionPanel.addComponent(this.endTurnButton);
        this.actionPanel.addComponent(this.changeWeaponButton);

        updateHealthBars();
        updateWeaponConditionBar();
        Panel leftContainerPanel = new Panel();
        leftContainerPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        this.rootPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        this.window.setComponent(this.rootPanel);
        this.rootPanel.addComponent(leftContainerPanel);



        //Health Bars
        this.healthBarPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        this.healthBarPanel.addComponent(this.enemyHealthBar.withBorder(Borders.singleLine(this.enemy.getName())));
        this.healthBarPanel.addComponent(new EmptySpace(new TerminalSize(0,1)));
        this.healthBarPanel.addComponent(this.playerHealthBar.withBorder(Borders.singleLine(this.player.getName())));
        leftContainerPanel.addComponent(this.healthBarPanel.withBorder(Borders.doubleLine("Health Bars")));
        leftContainerPanel.addComponent(new EmptySpace(new TerminalSize(0,1)));

        //weapon condition
        Panel weaponConditionPanel = new Panel();
        weaponConditionPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        weaponConditionPanel.addComponent(this.weaponConditionBar.withBorder(Borders.singleLine(this.playerWeapon.getDisplayName())));
        leftContainerPanel.addComponent(weaponConditionPanel.withBorder(Borders.doubleLine("Weapon Condition")));
        //Log Panel
        this.logPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        this.logTextBox = new TextBox(new TerminalSize(50,20),TextBox.Style.MULTI_LINE);
        this.logTextBox.setReadOnly(true);
        this.logPanel.addComponent(this.logTextBox.withBorder(Borders.singleLine("Combat Log")));
        this.rootPanel.addComponent(this.logPanel.withBorder(Borders.doubleLine()));
        
        //Action Panel
        this.actionPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        leftContainerPanel.addComponent(this.actionPanel.withBorder(Borders.doubleLine("Combat Actions")));
    }

    public void endComabt(){
        this.running = false;
        
        this.window.close();
        DeathInterface endScreen = new DeathInterface();
        endScreen.showDeathScreen(player, textGUI);

    }

    public void mainLoop(){
        this.running = true;
        this.textGUI.addWindowAndWait(this.window);
    }
    private void addLogMessage(String message){
        //TODO MAke the scrolling work 
        this.logTextBox.setReadOnly(false);
        this.logTextBox.addLine(">" + message);
        
        this.logTextBox.setCaretPosition(this.logTextBox.getLineCount() - 1, 0); // jump to last line
        this.logTextBox.setReadOnly(true);
        this.logTextBox.invalidate();
    }
    private void attack(){
        CombatInfo info = this.combatEngine.attack();

        if(info.info == combatInfoEnum.HIT || info.info == combatInfoEnum.WIN){
            double damage = info.damage;
            this.addLogMessage(randomString(new String[]{"Your attack lands!","You land a devestating blow!","That's going to leave a scar",
                "You hit your mark", "The "+enemy.getName()+" failed to doge your attack","Your swift strike wounds "+enemy.getName()
            }));

            this.addLogMessage("for "+damage+" !");

            updateHealthBars();
            updateWeaponConditionBar();

            if(info.info == combatInfoEnum.WIN){
                this.addLogMessage("You slay the "+this.enemy.getName()+" !");
                int xpGained = this.enemy.getDeathXp();
                this.addLogMessage("You gain "+xpGained+" XP !");
                BigDecimal goldGained = new BigDecimal(0.0);
                int playerLuck = this.player.getLuck();
                double goldChance = 0.20 + (0.50*(playerLuck/(playerLuck+0.40)));

                //Shown on pop up message 
                String popUpMessage = "You slay the " + this.enemy.getName() + "!\n" +
                          "You gain " + xpGained + " XP!";

                if (this.randGen.nextDouble() < goldChance) {
                    BigDecimal goldAmount = this.enemy.getDeathGold();

                    double luckMultiplier = 1.0 + (playerLuck / 7.5);

                    goldAmount = goldAmount.multiply(BigDecimal.valueOf(luckMultiplier)).setScale(2,RoundingMode.HALF_DOWN);

                    this.player.addGold(goldAmount);

                    popUpMessage += "\n Picked up "+ goldAmount.toString()+" gold.";

                }

                

                this.player.addXp(xpGained);

                new MessageDialogBuilder()
                    .setTitle("Victory")
                    .setText(popUpMessage)
                    .addButton(MessageDialogButton.OK)
                    .build()
                    .showDialog(this.textGUI);
                
                this.playerTurn = true;
                this.window.close();
                
                
            }


        }else if(info.info == combatInfoEnum.NO_DAMAGE){
            this.addLogMessage(randomString(new String[] {"Your attack fails to hurt the "+this.enemy.getName()+" !",
                "You do no damage! ", "The attack was too weak to damage the "+ enemy.getName(),"You do no damage :["
            }));
        }else if(info.info == combatInfoEnum.DOGE){
            this.addLogMessage(randomString(new String[] {"That was some poor aim !",enemy.getName()+" is too swift for you","The attack finds nothing but air",enemy.getName()+" doged the attack","to kill something you need to hit it"}));
            this.addLogMessage("No damage");
        }else if(info.info == combatInfoEnum.DEATH){
            this.addLogMessage(randomString(new String[]{"You are victorious!",enemy.getName()+" is crushed under the weight of the blow","Sorcery, "+enemy.getName()+" is now a dead "+ enemy.getName(),enemy.getName()+ " is slain",
                "They didn't catch you this time Prince","You won", "You killed the "+enemy.getName()
            }));
        }

        
        
    }

    private void enchantmentChecks(creatures.Character character,effectCounterType eventType){
        Vector<String> expiredEffects = character.applyEnchantEvent(eventType);
        if(expiredEffects != null){
            for(String effectName : expiredEffects){
                String message = effectName +" expired.";
                this.addLogMessage(message);
            }
        }
       
    }

    private void oppTurn(){
    
        if(!this.playerTurn){
            //decremtning enemy tempory effects 
            enchantmentChecks(this.enemy, effectCounterType.TURN);

            String name = this.enemy.getName();
            CombatInfo info = this.combatEngine.oppTurn();
            double damage = info.damage;
            switch (info.info) {
                case DEATH:
                    DeathInterface deathUi = new DeathInterface();
                    deathUi.showDeathScreen(player, textGUI);

                    break;
                case HIT:
                    this.addLogMessage(name +" hit's you for "+ damage +" !");
                    this.addLogMessage(randomString(new String[]{"Ouch","It leaves a deep wound","You'll carry that scar for life","The attack cuts deep","You can't take much more of them",
                        this.player.getName()+" screams in pain"," you don't falter","Pain throbs from the wound","You're being carless","Be faster next time"
                    }));
                    break;
                case DOGE:
                    this.addLogMessage("Doge message here");
                    break;
                case BLOCK:
                    this.addLogMessage("Block message here");
                    break;
                case NO_DAMAGE:
                    this.addLogMessage("No Damage message here");
                    break;
                default:
                    break;
            }

            updateHealthBars();
            if(info.info == combatInfoEnum.DEATH){
                gameOver();
            }else{
                this.playerTurn = true;
                // Decementing player tempory effect timers 
                enchantmentChecks(this.player, effectCounterType.TURN);
            }
            
            


        }
    }

    private void gameOver(){
        addLogMessage("GAMEOVER");
    }
    private String randomString(String[] messageOptions){
        String message = "";
        int size = messageOptions.length;

        if(size > 1){
            message = messageOptions[this.randGen.nextInt(size)];
        }else{
            message = messageOptions[0];
        }
        
        return(message);
    }

    private void addMessageBuffer(){
        Vector<String> buffer = this.combatEngine.getMessageBuffer();

        for(String message : buffer){
            addLogMessage(message);
        }

        this.combatEngine.clearMessageBuffer();
    }
    
    private void updateHealthBars(){
        this.playerHealthBar.update();
        this.enemyHealthBar.update();
    }

    

    private void updateWeaponConditionBar(){
        this.weaponConditionBar.update();
    }

    
}
