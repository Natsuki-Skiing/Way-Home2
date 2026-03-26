package Dungeon;

import enums.dungeonTileDirEnum;
import enums.dungeonTileTypeEnum;

import java.util.Vector;

public final class TileHolderClass {
    private final TileTemplate forwardHallway;
    private final TileTemplate leftTurn;
    private final TileTemplate rightTurn;
    private final TileTemplate crossSection;
    private final TileTemplate tJunction;
    private final TileTemplate deadEnd;
    private final TileTemplate doorEnd;
    private final TileTemplate chest;


    public TileHolderClass() {
        this.forwardHallway = new TileTemplate("src/dungeonTiles/fowardHallway.txt");
        this.leftTurn = new TileTemplate("src/dungeonTiles/leftTurn.txt");
        this.rightTurn = new TileTemplate("src/dungeonTiles/rightTurn.txt");
        this.crossSection = new TileTemplate("src/dungeonTiles/crossSection.txt");
        this.tJunction = new TileTemplate("src/dungeonTiles/tJunction.txt");
        this.deadEnd = new TileTemplate("src/dungeonTiles/deadEnd.txt");
        this.doorEnd = new TileTemplate("src/dungeonTiles/doorEnd.txt");
        this.chest = new TileTemplate("src/dungeonTiles/chestEnd.txt");

    }

    public Vector<String> getTileImage(DungeonTile tile, dungeonTileDirEnum playerFacing) {
        if (tile.getType() == dungeonTileTypeEnum.EXIT) {
            return this.doorEnd.getImage();
        }
        if (tile.getType() == dungeonTileTypeEnum.CHEST) {
            return this.chest.getImage();
        }

        boolean canGoForward;
        boolean canGoLeft;
        boolean canGoRight;

        switch (playerFacing) {
            case NORTH:
                canGoForward = tile.hasConnection(dungeonTileDirEnum.NORTH);
                canGoLeft = tile.hasConnection(dungeonTileDirEnum.WEST);
                canGoRight = tile.hasConnection(dungeonTileDirEnum.EAST);
                break;
            case SOUTH:
                canGoForward = tile.hasConnection(dungeonTileDirEnum.SOUTH);
                canGoLeft = tile.hasConnection(dungeonTileDirEnum.EAST);
                canGoRight = tile.hasConnection(dungeonTileDirEnum.WEST);
                break;
            case EAST:
                canGoForward = tile.hasConnection(dungeonTileDirEnum.EAST);
                canGoLeft = tile.hasConnection(dungeonTileDirEnum.NORTH);
                canGoRight = tile.hasConnection(dungeonTileDirEnum.SOUTH);
                break;
            case WEST:
                canGoForward = tile.hasConnection(dungeonTileDirEnum.WEST);
                canGoLeft = tile.hasConnection(dungeonTileDirEnum.SOUTH);
                canGoRight = tile.hasConnection(dungeonTileDirEnum.NORTH);
                break;
            default:
                canGoForward = false;
                canGoLeft = false;
                canGoRight = false;
        }

        int openCount = (canGoForward ? 1 : 0) + (canGoLeft ? 1 : 0) + (canGoRight ? 1 : 0);
        // All three exits open: true 4-way crossroad.
        if (openCount == 3) {
            return this.crossSection.getImage();
        }
        // Two exits open: T-junction.
        if (openCount == 2) {
            return this.tJunction.getImage();
        }

        // Single open path
        if (canGoForward) {
            return this.forwardHallway.getImage();
        }
        if (canGoLeft) {
            return this.leftTurn.getImage();
        }
        if (canGoRight) {
            return this.rightTurn.getImage();
        }
        return this.deadEnd.getImage();
    }
}