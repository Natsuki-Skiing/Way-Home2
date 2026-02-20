package Dungeon;
import java.util.Vector;

import enums.dungeonTileDirEnum;
import enums.dungeonTileTypeEnum;
public class TileHolderClass {
    private TileTemplate forwardHallway;
    private TileTemplate leftTurn;
    private TileTemplate rightTurn;
    private TileTemplate crossSection;
    private TileTemplate deadEnd;
    private TileTemplate doorEnd;
    private TileTemplate chest;
    public TileHolderClass() {
        
        this.forwardHallway = new TileTemplate("src/dungeonTiles/fowardHallway.txt");
        this.leftTurn = new TileTemplate("src/dungeonTiles/leftTurn.txt");
        this.rightTurn = new TileTemplate("src/dungeonTiles/rightTurn.txt");
        this.crossSection = new TileTemplate("src/dungeonTiles/crossSection.txt");
        this.deadEnd = new TileTemplate("src/dungeonTiles/deadEnd.txt");
        this.doorEnd = new TileTemplate("src/dungeonTiles/doorEnd.txt");
        this.chest = new TileTemplate("src/dungeonTiles/chestEnd.txt");
    }

    public Vector<String> getTileImage(DungeonTile tile, dungeonTileDirEnum playerFacing){
        if (tile.getType() == dungeonTileTypeEnum.EXIT) {
            return this.doorEnd.getImage();
        }else if (tile.getType() == dungeonTileTypeEnum.CHEST) {
            return this.chest.getImage();
        }
        boolean canGoForward = false;
        boolean canGoLeft = false;
        boolean canGoRight = false;
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
        }

        if ((canGoForward && canGoLeft) || (canGoForward && canGoRight) || (canGoLeft && canGoRight)) {
            return this.crossSection.getImage();
        } else if (canGoForward) {
            return this.forwardHallway.getImage();
        } else if (canGoLeft) {
            return this.leftTurn.getImage();
        } else if (canGoRight) {
            return this.rightTurn.getImage();
        } else {
            return this.deadEnd.getImage();
        }
    }
}
