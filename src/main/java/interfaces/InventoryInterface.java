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
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.table.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.regex.Pattern;



import creatures.Player;
import items.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import enums.itemTypeEnum;
public class InventoryInterface {
    private Player player;
    private Panel rootPanel;
    private Panel listPanel;
    private Panel controlsPanel;
    private Label currentCatLabel;
    private Chest inventory;
    private Screen screen;
    private WindowBasedTextGUI textGUI;
    private Window window;
    private ArrayList<enums.itemTypeEnum> catList;
    private Terminal terminal;
    
    private int currentCatIndex = 0;
    //private Table<String> table = new Table<String>("Item Name","Quantity"," ");
    private ActionListBox table = new ActionListBox();
    public InventoryInterface(Player player,Screen screen, WindowBasedTextGUI textGUI,Terminal terminal){
       
        this.terminal = terminal;
        this.window = new BasicWindow();
        this.window.setHints(java.util.Arrays.asList(
               // Occupy the entire terminal area
        Window.Hint.NO_POST_RENDERING   // Prevents windows behind from drawing
        ));
        this.screen = screen;
        this.textGUI = textGUI;
        this.inventory = player.getInventory();
        this.player = player;
        this.rootPanel = new Panel();
        this.listPanel = new Panel();
        this.controlsPanel = new Panel();
        this.currentCatLabel = new Label(" ");
        this.listPanel.addComponent(currentCatLabel);
        this.listPanel.addComponent(this.table);
        this.rootPanel.addComponent(listPanel);
        this.catList = this.inventory.getTypesOfChest();
        
        this.controlsPanel.addComponent(new Label("Change Category: <- ->"));
        
        
    }
    private KeyStroke getKeyStroke(){
        try{
            return(this.terminal.readInput());
        }catch(Exception e){
            return(null);
        }
    }
    public void mainLoop(){
        
        this.window.setComponent(this.rootPanel);
        this.changeCat(0);
        boolean running = true;
        this.textGUI.addWindow(this.window);
        KeyType keyComp;
        while(running){
            try{this.textGUI.updateScreen();}catch(Exception e){}
            
            KeyStroke input = getKeyStroke();

            if(input == null){
                try{Thread.sleep(10);}catch(Exception e){}
                continue;
            }else{
                keyComp = input.getKeyType();
            }
            switch (keyComp) {
                case ArrowLeft:
                    changeCat(-1);
                    break;
                

                case ArrowRight:
                    changeCat(1);
                    break;

                case Escape: // Add an exit key
                this.window.close(); // Removes window from stack
                running = false;
                break;

                default:
                    this.window.handleInput(input);
            }
            
        }
        this.window.close();
        
    }
    private void populateTable(){
            itemTypeEnum type = getCurrentType();
            ArrayList<ChestItem> itemList = this.inventory.getItemsByType(type);
            this.table.clearItems();
            for(ChestItem item : itemList){
                String name = item.getName();
                String quantity =Integer.toString(item.getQuantity());
                String equipt = " ";
                if(this.player.checkIfEquipt(item.getItem())){
                    equipt = "E";
                }
                String label = name +' '+ quantity+' '+equipt;
                this.table.addItem(label,()-> hadleSelection(item));

            }
    }
    private void hadleSelection(ChestItem selectedItem){
        final AtomicBoolean equip = new AtomicBoolean(false);
        final AtomicBoolean hasEquip = new AtomicBoolean(false);
        String actionMsg = "Consume";
        
        if(selectedItem.getItem().getUseType() == enums.itemTypeEnum.EQUIPPABLE){
            equip.set(true);
            if(this.player.checkIfEquipt(selectedItem.getItem())){
                hasEquip.set(true);
                actionMsg = "Unequip";
            }else{
                actionMsg = "Equip";
            }
            
        }
        new ActionListDialogBuilder()
		.setTitle(selectedItem.getName())
		.setDescription("Choose item")
		.addAction(actionMsg, new Runnable() {
		    @Override
		    public void run() {
		        if(equip.get()){
                    if(hasEquip.get()){
                        player.unequipItem(selectedItem.getItem());
                    }else{
                        player.equipItem(selectedItem.getItem());
                    }
                }else{
                    //TODO CONSUME STUFF
                }
                populateTable();
		    }
		})
		.addAction("Drop", new Runnable() {
		    @Override
		    public void run() {
                //TODO Add like a slider to drop more than one at once 

		        if(player.checkIfEquipt(selectedItem.getItem()) && selectedItem.getQuantity() ==1){
                    player.unequipItem(selectedItem.getItem());
                    
                }
                player.getInventory().takeItem(selectedItem.getItem(), 1);
                populateTable();
		    }
		})
		.addAction("Back", new Runnable() {
		    @Override
		    public void run() {
		        
		    }
		})
		.build()
		.showDialog(textGUI);
    }
    private void changeCat(int modifier){
        this.currentCatIndex += modifier;
        if(this.currentCatIndex > this.catList.size()-1){
            this.currentCatIndex = 0;
        }else if(this.currentCatIndex < 0){
            this.currentCatIndex = this.catList.size() -1;
        } 
        this.currentCatLabel.setText(this.catList.get(this.currentCatIndex).name());
        this.populateTable();
    }

    private void nextCat(){
        changeCat(1);
    }

    private void prevCat(){
        changeCat(-1);
    }

    private enums.itemTypeEnum getCurrentType(){
        return(this.catList.get(this.currentCatIndex));
    }


}
