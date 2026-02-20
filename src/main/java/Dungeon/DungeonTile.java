package Dungeon;

import java.util.Vector;

import enums.dungeonTileDirEnum;
import enums.dungeonTileTypeEnum;
public class DungeonTile {
    private Vector<dungeonTileDirEnum> connections;
    private dungeonTileTypeEnum type;
   
    public DungeonTile(dungeonTileTypeEnum tileType, Vector<dungeonTileDirEnum> connections){
        this.type = tileType;
        this.connections = connections;
    }

    public boolean hasConnection(dungeonTileDirEnum dir){
        return this.connections.contains(dir);
    }

    public dungeonTileTypeEnum getType(){
        return this.type;
    }
}
