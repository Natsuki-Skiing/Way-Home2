package interfaces;

import Dungeon.Dungeon;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import enums.dungeonTileDirEnum;

/**
 * Small compass widget showing the player's facing direction.
 */
public final class DungeonCompass extends AbstractComponent<DungeonCompass> {
    private static final int WIDTH = 9;
    private static final int HEIGHT = 5;

    private final Dungeon dungeon;

    public DungeonCompass(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    protected ComponentRenderer<DungeonCompass> createDefaultRenderer() {
        return new ComponentRenderer<DungeonCompass>() {
            @Override
            public TerminalSize getPreferredSize(DungeonCompass component) {
                return new TerminalSize(WIDTH, HEIGHT);
            }

            @Override
            public void drawComponent(TextGUIGraphics g, DungeonCompass component) {
                g.fill(' ');

                g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                g.putString(4, 0, "N");
                g.putString(0, 2, "W");
                g.putString(8, 2, "E");
                g.putString(4, 4, "S");

                g.setForegroundColor(TextColor.ANSI.YELLOW);
                g.putString(4, 2, String.valueOf(arrowFor(dungeon.getPlayerFacing())));
            }
        };
    }

    private static char arrowFor(dungeonTileDirEnum dir) {
        switch (dir) {
            case NORTH:
                return '^';
            case EAST:
                return '>';
            case SOUTH:
                return 'v';
            case WEST:
                return '<';
            default:
                return '?';
        }
    }
}
