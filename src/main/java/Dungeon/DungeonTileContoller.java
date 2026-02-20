package Dungeon;

import enums.dungeonTileDirEnum;
import java.util.Vector;
import java.util.Arrays;
public class DungeonTileContoller {
    private final DungeonTile hallTile = new DungeonTile("src/dungeonTiles/forwardHallway.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.FORWARD, dungeonTileDirEnum.BACKWARD)));
    private final  DungeonTile leftTurnTile = new DungeonTile("src/dungeonTiles/leftTurn.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.LEFT)));
    private final  DungeonTile rightTurnTile = new DungeonTile("src/dungeonTiles/rightTurn.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.RIGHT)));
    private final  DungeonTile fourWayTile = new DungeonTile("src/dungeonTiles/crossSection.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.FORWARD, dungeonTileDirEnum.BACKWARD, dungeonTileDirEnum.LEFT, dungeonTileDirEnum.RIGHT)));
    private final  DungeonTile deadEndTile = new DungeonTile("src/dungeonTiles/deadEnd.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.BACKWARD)));
    private final DungeonTile exitTile = new DungeonTile("src/dungeonTiles/doorEnd.txt", new Vector<dungeonTileDirEnum>(Arrays.asList(dungeonTileDirEnum.FORWARD)));


    public DungeonTile getHallTile(){
        return this.hallTile;
    }
    public DungeonTile getLeftTurnTile(){
        return this.leftTurnTile;
    }
    public DungeonTile getRightTurnTile(){
        return this.rightTurnTile;
    }
    public DungeonTile getFourWayTile(){
        return this.fourWayTile;
    }
    public DungeonTile getDeadEndTile(){
        return this.deadEndTile;
    }
    public DungeonTile getExitTile(){
        return this.exitTile;
    }
}
