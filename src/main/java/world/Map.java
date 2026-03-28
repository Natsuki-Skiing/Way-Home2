package world;
import java.util.HashMap;
import interfaces.GameTile;
public class Map {
    private HashMap<String,GameTile> mapTiles;

    private String mapKey(int x , int y){
        return((Integer.toString(x)+Integer.toString(y)));
    }
    public GameTile getMapTile(int x, int y){
        return(this.mapTiles.get(mapKey(x, y)));
    }

    public boolean isTileWalkable(int x, int y){
        GameTile tile = getMapTile(x, y);
        return(tile.isWalkable());
    }

    public void setTile(int x, int y, GameTile newTile){
        this.mapTiles.put(mapKey(x, y),newTile);
    }
}
