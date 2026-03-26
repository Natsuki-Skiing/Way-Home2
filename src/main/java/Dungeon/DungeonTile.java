package Dungeon;

import enums.dungeonTileDirEnum;
import enums.dungeonTileTypeEnum;

import java.util.Vector;

public class DungeonTile {
    private final Vector<dungeonTileDirEnum> connections;
    private dungeonTileTypeEnum type;

    public DungeonTile(dungeonTileTypeEnum tileType, Vector<dungeonTileDirEnum> connections) {
        this.type = tileType;
        this.connections = connections;
    }

    public boolean hasConnection(dungeonTileDirEnum dir) {
        return this.connections.contains(dir);
    }

    public dungeonTileTypeEnum getType() {
        return this.type;
    }

    public void setType(dungeonTileTypeEnum type) {
        this.type = type;
    }

    public Vector<dungeonTileDirEnum> getConnections() {
        return this.connections;
    }
}
