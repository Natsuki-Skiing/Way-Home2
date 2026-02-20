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
import java.util.Random;
import java.util.Vector;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.table.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import creatures.*;
import Dungeon.Dungeon;

public class DungeonInterface {
    private Player player;
    private WindowBasedTextGUI textGUI;
    private Window window;
    private Dungeon dungeon;
    private Panel rootPanel;

    
    public DungeonInterface(Player player,Dungeon dungeon, WindowBasedTextGUI textGUI) {
        this.player = player;
        this.textGUI = textGUI;
        this.dungeon = dungeon;
        this.window = new BasicWindow("Dungeon");





    }


}
