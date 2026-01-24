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
import java.util.Vector;


import creatures.Player;
import items.*;
import items.ChestClasses.Chest;
import items.ChestClasses.ChestItem;
import items.Instances.ConditionInstance;
import items.Instances.ItemInstance;
import items.Instances.WeaponInstance;
import items.templates.ConditionTemplate;
import items.templates.ItemTemplate;
import items.templates.WeaponTemplate;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import items.ChestClasses.*;
import enums.itemTypeEnum;
public class InventoryInterface {
    private ChestContentsPanel chestPanel;
    private Screen screen;
    private WindowBasedTextGUI textGUI;
    private Window window;
    private Panel rootPanel;
    public InventoryInterface(Player player,Screen screen, WindowBasedTextGUI textGUI){
        this.chestPanel = new ChestContentsPanel(player, player.getInventory());

        this.window = new BasicWindow();
        this.window.setHints(java.util.Arrays.asList(
            Window.Hint.NO_POST_RENDERING   // Prevents windows behind from drawing
        ));

        this.screen = screen;
        this.textGUI = textGUI;

        this.rootPanel = new Panel();
        this.rootPanel.addComponent(chestPanel);

        this.window.setComponent(rootPanel);
      

    this.window.addWindowListener(new WindowListenerAdapter() {
    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        // 1. Handle Escape (Close)
        if (keyStroke.getKeyType() == KeyType.Escape) {
            window.close();
            deliverEvent.set(false);
        }
        // 2. Handle Left Arrow (Previous Category)
        else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            chestPanel.changeCat(-1);
            deliverEvent.set(false); // Stop the event from moving focus
        }
        // 3. Handle Right Arrow (Next Category)
        else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            chestPanel.changeCat(1);
            deliverEvent.set(false); // Stop the event from moving focus
        }
    }
});

       
    }

    public void show() {
        
        textGUI.addWindowAndWait(window);
    }


}