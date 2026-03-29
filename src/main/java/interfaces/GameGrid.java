package interfaces;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import creatures.Player;
import world.*;
public class GameGrid extends AbstractComponent<GameGrid> {
    private Map currentMap;
    
    private final int width;
    private final int height;
    private Player player;
    public GameGrid(int width, int height, Player player,Map startMap) {
        this.width = width;
        this.height = height;
        this.player = player;
        this.currentMap = startMap;
    }


    public void setCurrentMap(Map newMap){
        this.currentMap = newMap;
    }
    public Map getCurrentMap(){
        return(this.currentMap);
    }
    public boolean setTile(int x , int y, GameTile tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        this.currentMap.setTile(x, y, tile);
        return true;
    }

    public GameTile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return this.currentMap.getMapTile(x, y);
    }

    @Override
    protected ComponentRenderer<GameGrid> createDefaultRenderer() {
        return new ComponentRenderer<GameGrid>() {
            @Override
            public TerminalSize getPreferredSize(GameGrid component) {
                return new TerminalSize(width, height);
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, GameGrid component) {
                // Iterate over the grid and draw every character
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        GameTile tile = currentMap.getMapTile(x, y);
                        graphics.setForegroundColor(tile.getForegroundColor());
                        graphics.setBackgroundColor(tile.getBackgroundColor());
                        graphics.setCharacter(x, y, tile.getDisplayChar());
                    }
                }

                graphics.setForegroundColor(player.getPlayerTile().getForegroundColor());
                graphics.setBackgroundColor(player.getPlayerTile().getBackgroundColor());
                graphics.setCharacter(player.getX(),player.getY(), player.getPlayerTile().getDisplayChar());
            }
        };
    } 
    
}
