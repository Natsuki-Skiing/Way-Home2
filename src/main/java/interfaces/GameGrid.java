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
public class GameGrid extends AbstractComponent<GameGrid> {
    private final GameTile[][] tiles;
    private final int width;
    private final int height;
    private Player player;
    public GameGrid(int width, int height, Player player) {
        this.width = width;
        this.height = height;
        this.player = player;
        this.tiles = new GameTile[this.width][this.height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new GameTile(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE, '.');
            }
        }

    }

    public boolean setTile(int x , int y, GameTile tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        tiles[x][y] = tile;
        return true;
    }

    public GameTile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[x][y];
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
                        GameTile tile = tiles[x][y];
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
