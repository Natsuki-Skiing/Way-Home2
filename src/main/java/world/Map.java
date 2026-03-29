package world;
import java.util.HashMap;
import java.util.Vector;

import interfaces.GameTile;
import enums.worldRegionEnum;
import enums.worldTileTypeEnum;
import java.util.Vector;
public class Map {
    private HashMap<String,GameTile> mapTiles;
    private worldRegionEnum mapRegionType;


    public Map(int width, int height,worldRegionEnum region, TileFactory tileFactory){
        this.mapTiles = new HashMap<>();
        this.mapRegionType = region;
        //Basic map gen
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                worldTileTypeEnum tileType = getTileType();
                GameTile tile = tileFactory.getTile(region, tileType);
                setTile(x, y, tile);
            }
        }

        //Additonal generation

        //Add water to the map
        int waterChance;
        switch (region) {
            case MOUNTAINS:
                waterChance = 12; 
                break;
            case SNOW:
                waterChance = 10; 
                break;
            case DESERT:
                waterChance = 5; 
                break;
            default:
                //Normal and bog have the same water chance
                waterChance = 20; // 20% chance for water tile
                break;
        } 

        if(Math.random()*100 < waterChance){
            if(region != worldRegionEnum.BOG || region != worldRegionEnum.NORMAL){
                region = worldRegionEnum.NORMAL;
            }

            generateWater(tileFactory,region,width,height);
        }


        

    }


    private void generateWater(TileFactory tileFactory, worldRegionEnum region,int mapWidth,int mapHeight){
        int waterRadius = (int)(Math.random()*7)+2; // Random water radius between 5 and 20
        int centerX = (int)(Math.random()*mapWidth);
        int centerY = (int)(Math.random()*mapHeight);

        Vector<String> waterCoords = new Vector<>();
        for (int x = centerX - waterRadius; x <= centerX + waterRadius; x++) {
            for (int y = centerY - waterRadius; y <= centerY + waterRadius; y++) {
                int dx = x - centerX;
                int dy = y - centerY;
                if (dx*dx + dy*dy <= waterRadius*waterRadius) {
                    waterCoords.add(mapKey(x, y));
                }
            }
        }

        for(String coord : waterCoords){
            this.mapTiles.put(coord, tileFactory.getTile(region, worldTileTypeEnum.WATER));
        }


        
    }

    private worldTileTypeEnum getTileType() {
        worldTileTypeEnum tileType;
        int rand = (int) (Math.random() * 100);
        if (rand < 45) {
            tileType = worldTileTypeEnum.BASE;
        } else if (rand < 75) {
            tileType = worldTileTypeEnum.TEXTURE;
        } else {
            tileType = worldTileTypeEnum.PLANTS;
        }
        return tileType;
    }
    private String mapKey(int x , int y){
        return((Integer.toString(x)+","+Integer.toString(y)));
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

    public worldRegionEnum getRegionType(){
        return(this.mapRegionType);
    }
}
