package interfaces;

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

import java.lang.ProcessHandle.Info;
import java.util.regex.Pattern;


import creatures.*;
public class mainGameWindow {
    private Screen screen;
    private WindowBasedTextGUI textGUI;
    private Panel mapPanel;
    private GameGrid gameGrid;
    private InfoPanel infoPanel;
    private Player player;
    private Window window;
    private Panel rootPanel;
    public mainGameWindow(Screen screen, WindowBasedTextGUI textGUI, Player player) {
        this.screen = screen;
        this.textGUI = textGUI;
        this.player = player;
        this.window = new BasicWindow();
        this.rootPanel = new Panel();
        this.rootPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        window.setComponent(this.rootPanel.withBorder(Borders.doubleLineBevel("Way Home")));
        this.mapPanel = new Panel();
        this.gameGrid = new GameGrid(80, 30 ,this.player);
        // Adding panels 
        this.mapPanel.addComponent(this.gameGrid);
        this.infoPanel = new InfoPanel(this.player);
        this.rootPanel.addComponent(this.mapPanel);
        this.rootPanel.addComponent(this.infoPanel.withBorder(Borders.doubleLineBevel(this.player.getName())));
        
        
        
        
    }

    public void setTile(int x , int y, GameTile tile) {
        this.gameGrid.setTile(x, y, tile);
    }

    public GameTile getTile(int x, int y) {
        return this.gameGrid.getTile(x, y);
    }
    public Window getWindow(){
        return(this.window);
    }
    
    
}
