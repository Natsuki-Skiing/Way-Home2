package interfaces;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.Random;
import Combat.CombatEncounter;

import java.util.Vector;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.table.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import creatures.*;
import items.Instances.WeaponInstance;

public class CombatInterface {
    private HealthBar playerHealthBar;
    private HealthBar enemyHealthBar;
    private Player player;
    private Entity enemy;
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
    private Random randGen = new Random();
    public CombatInterface(CombatEncounter combatEngine,WindowBasedTextGUI textGUI) {
        this.player = combatEngine.getPlayer();
        this.enemy = combatEngine.getOpp();
        this.playerWeapon = player.getEquippedWeapon();
        
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
                addLogMessage(player.getName() + " attacks " + enemy.getName() + " with " + playerWeapon.getDisplayName());
                attack();
            }
        });
        this.fleeButton = new Button("Flee", new Runnable() {
            @Override
            public void run() {
                addLogMessage(player.getName() + " attempts to flee!");
                if(combatEngine.flee()){
                    addLogMessage( randomString(new String[]{"You Flee!","You manage to escape the "+enemy.getName(),"You run away","You are a coward, but a lucky one"}));
                }else{
                    addLogMessage("");
                }
            }
        });
        this.useItemButton = new Button("Use Item", new Runnable() {
            @Override
            public void run() {
                addLogMessage(player.getName() + " uses an item!");
                // Use item logic would go here
            }
        });
        this.endTurnButton = new Button("End Turn", new Runnable() {
            @Override
            public void run() {
                addLogMessage(player.getName() + " ends their turn.");
                // End turn logic would go here
            }
        });
        this.changeWeaponButton = new Button("Change Weapon", new Runnable() {
            @Override
            public void run() {
                addLogMessage(player.getName() + " changes weapon.");
                // Change weapon logic would go here
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

    public void mainLoop(){
        this.running = true;
        this.textGUI.addWindowAndWait(this.window);
    }
    private void addLogMessage(String message){
        this.logTextBox.addLine(message);
        this.logTextBox.setCaretPosition(this.logTextBox.getLineCount(), 0);
    }
    private void attack(){
        double damage = this.combatEngine.attack();

        if(damage > 0.0){
            this.addLogMessage(randomString(new String[]{"Your attack lands!","You land a devestating blow!","That's going to leave a scar",
                "You hit your mark", "The "+enemy.getName()+" failed to doge your attack","Your swift strike wounds "+enemy.getName()
            }));

            this.addLogMessage("for "+damage+" !");

            updateHealthBars();
            updateWeaponConditionBar();


        }else{
            this.addLogMessage(randomString(new String[] {"Your attack fails to hurt the "+this.enemy.getName()+" !",
                "You do no damage! ", "The attack was too weak to damage the "+ enemy.getName(),"You do no damage :["
            }));
        }
        
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
        Borders.singleLine(this.playerWeapon.getDisplayName());
        this.weaponConditionBar.update();
    }
}
