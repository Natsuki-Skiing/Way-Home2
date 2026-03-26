package interfaces;

import Dungeon.Dungeon;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

import java.util.Set;

/**
 * Minimap that renders only explored tiles as '#'. Player is '@'.
 * The map is centred on the player.
 */
public final class DungeonMiniMap extends AbstractComponent<DungeonMiniMap> {
    private static final int MAP_WIDTH = 21;
    private static final int MAP_HEIGHT = 11;

    private final Dungeon dungeon;

    public DungeonMiniMap(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    protected ComponentRenderer<DungeonMiniMap> createDefaultRenderer() {
        return new ComponentRenderer<DungeonMiniMap>() {
            @Override
            public TerminalSize getPreferredSize(DungeonMiniMap component) {
                return new TerminalSize(MAP_WIDTH, MAP_HEIGHT);
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, DungeonMiniMap component) {
                graphics.fill(' ');

                Set<String> visited = dungeon.getVisitedTiles();
                int px = dungeon.getPlayerX();
                int py = dungeon.getPlayerY();

                int halfW = MAP_WIDTH / 2;
                int halfH = MAP_HEIGHT / 2;

                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                for (String key : visited) {
                    int comma = key.indexOf(',');
                    if (comma <= 0 || comma >= key.length() - 1) {
                        continue;
                    }

                    int tx;
                    int ty;
                    try {
                        tx = Integer.parseInt(key.substring(0, comma));
                        ty = Integer.parseInt(key.substring(comma + 1));
                    } catch (NumberFormatException ignored) {
                        continue;
                    }

                    int sx = halfW + (tx - px);
                    int sy = halfH + (ty - py);
                    if (sx < 0 || sx >= MAP_WIDTH || sy < 0 || sy >= MAP_HEIGHT) {
                        continue;
                    }

                    graphics.putString(sx, sy, "#");
                }

                graphics.setForegroundColor(TextColor.ANSI.YELLOW);
                graphics.putString(halfW, halfH, "@");
            }
        };
    }
}
