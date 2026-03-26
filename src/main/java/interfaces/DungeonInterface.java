package interfaces;

import Dungeon.Dungeon;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import creatures.Player;
import enums.dungeonTileDirEnum;

import java.util.concurrent.atomic.AtomicBoolean;

public final class DungeonInterface {
    private final Player player;
    private final Dungeon dungeon;
    private final WindowBasedTextGUI textGUI;

    private final Window window;

    private final DungeonView viewer;
    private final DungeonMiniMap miniMap;
    private final DungeonCompass compass;
    private final DungeonInfoPanel info;

    public DungeonInterface(Player player, Dungeon dungeon, WindowBasedTextGUI textGUI) {
        this.player = player;
        this.dungeon = dungeon;
        this.textGUI = textGUI;

        this.window = new BasicWindow("Dungeon");

        this.viewer = new DungeonView(80, 30);
        this.miniMap = new DungeonMiniMap(dungeon);
        this.compass = new DungeonCompass(dungeon);
        this.info = new DungeonInfoPanel(player);

        this.window.setComponent(buildLayout());
        this.window.addWindowListener(buildInputHandler());

        refresh();
    }

    public void showDungeonWindow() {
        textGUI.addWindowAndWait(window);
    }

    private Panel buildLayout() {
        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel topRow = new Panel(new LinearLayout(Direction.HORIZONTAL));

        Panel viewPanel = new Panel();
        viewPanel.addComponent(viewer);
        topRow.addComponent(viewPanel.withBorder(Borders.doubleLine(player.getName() + " POV")));

        Panel mapPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mapPanel.addComponent(compass.withBorder(Borders.singleLine("Compass")));
        mapPanel.addComponent(miniMap);
        topRow.addComponent(mapPanel.withBorder(Borders.singleLine("Map")));

        Panel infoPanel = new Panel();
        infoPanel.addComponent(info);

        root.addComponent(topRow);
        root.addComponent(infoPanel.withBorder(Borders.singleLine("Status")));
        return root;
    }

    private WindowListenerAdapter buildInputHandler() {
        return new WindowListenerAdapter() {
            @Override
            public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
                KeyType type = keyStroke.getKeyType();
                if (type == KeyType.ArrowUp) {
                    tryMoveForward();
                    deliverEvent.set(false);
                    return;
                }
                if (type == KeyType.ArrowDown) {
                    tryMoveBackward();
                    deliverEvent.set(false);
                    return;
                }
                if (type == KeyType.ArrowLeft) {
                    // Only allowed at crossroads (3+ connections).
                    if (dungeon.getCurrentTileConnectionCount() >= 3) {
                        tryMoveStrafe(turnLeft(dungeon.getPlayerFacing()));
                    }
                    deliverEvent.set(false);
                    return;
                }
                if (type == KeyType.ArrowRight) {
                    if (dungeon.getCurrentTileConnectionCount() >= 3) {
                        tryMoveStrafe(turnRight(dungeon.getPlayerFacing()));
                    }
                    deliverEvent.set(false);
                }
            }
        };
    }

    /**
     * Move forward in the current facing direction.
     * Facing is not changed here — autoRotateAfterMove handles corners.
     */
    private void tryMoveForward() {
        dungeonTileDirEnum facing = dungeon.getPlayerFacing();
        if (!dungeon.canMove(facing)) {
            return;
        }
        dungeon.movePlayer(facing);
        autoRotateAfterMove(facing);
        refresh();
    }

    /**
     * Move backward (opposite of facing), then face that direction so the
     * view shows what is ahead on the new tile.
     * autoRotateAfterMove handles corners after the facing flip.
     */
    private void tryMoveBackward() {
        dungeonTileDirEnum backDir = opposite(dungeon.getPlayerFacing());
        if (!dungeon.canMove(backDir)) {
            return;
        }
        dungeon.movePlayer(backDir);
        dungeon.setPlayerFacing(backDir);
        autoRotateAfterMove(backDir);
        refresh();
    }

    /**
     * Strafe: move sideways at a junction. The player faces the direction they
     * moved so they are oriented correctly on the new tile. autoRotateAfterMove
     * then handles any corner on the new tile.
     */
    private void tryMoveStrafe(dungeonTileDirEnum strafeDir) {
        if (!dungeon.canMove(strafeDir)) {
            return;
        }
        dungeon.movePlayer(strafeDir);
        // Face the direction we just moved into — we chose this path deliberately.
        dungeon.setPlayerFacing(strafeDir);
        autoRotateAfterMove(strafeDir);
        refresh();
    }

    /**
     * After entering a tile with exactly 2 connections (one way in, one way out),
     * automatically rotate the player to face the only exit.
     * arrivedFrom is the absolute direction the player just moved (not the tile
     * direction — it is used to derive which connection is the "way back").
     */
    private void autoRotateAfterMove(dungeonTileDirEnum arrivedFrom) {
        if (dungeon.getCurrentTileConnectionCount() != 2) {
            return;
        }
        // The "way back" in the new tile's reference frame is opposite to how we arrived.
        dungeonTileDirEnum wayBack = opposite(arrivedFrom);
        dungeonTileDirEnum exit = dungeon.getSingleExitExcluding(wayBack);
        if (exit != null) {
            dungeon.setPlayerFacing(exit);
        }
    }

    private void refresh() {
        viewer.setImage(dungeon.getCurrentTileImage());
        viewer.invalidate();

        info.update(dungeon.getClockString(), dungeon.getPlayerX(), dungeon.getPlayerY(), dungeon.getPlayerFacing());
        info.invalidate();

        miniMap.invalidate();
        compass.invalidate();
    }

    private static dungeonTileDirEnum opposite(dungeonTileDirEnum dir) {
        switch (dir) {
            case NORTH: return dungeonTileDirEnum.SOUTH;
            case SOUTH: return dungeonTileDirEnum.NORTH;
            case EAST:  return dungeonTileDirEnum.WEST;
            case WEST:  return dungeonTileDirEnum.EAST;
            default:    return dir;
        }
    }

    private static dungeonTileDirEnum turnLeft(dungeonTileDirEnum facing) {
        switch (facing) {
            case NORTH:
                return dungeonTileDirEnum.WEST;
            case WEST:
                return dungeonTileDirEnum.SOUTH;
            case SOUTH:
                return dungeonTileDirEnum.EAST;
            case EAST:
                return dungeonTileDirEnum.NORTH;
            default:
                return facing;
        }
    }

    private static dungeonTileDirEnum turnRight(dungeonTileDirEnum facing) {
        switch (facing) {
            case NORTH:
                return dungeonTileDirEnum.EAST;
            case EAST:
                return dungeonTileDirEnum.SOUTH;
            case SOUTH:
                return dungeonTileDirEnum.WEST;
            case WEST:
                return dungeonTileDirEnum.NORTH;
            default:
                return facing;
        }
    }
}