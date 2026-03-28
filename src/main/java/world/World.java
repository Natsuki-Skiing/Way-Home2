package world;
import java.util.HashMap;
import enums.worldRegionEnum;
public class World {
    //I need to load already made maps here
    private HashMap<String,Map> mapsHash;
    private int mapWidth;
    private int mapHeight;


    public World(int mapWidth, int mapHeight,String existingMapsJsonPath){
        //TODO load edisting maps 

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }


    

    private void genMap(int x,int y){
        worldRegionEnum regionEnum = worldRegionEnum.NORMAL;
        if(y > 10){
            regionEnum = worldRegionEnum.SNOW;
        }else if(y <-10){
            regionEnum = worldRegionEnum.DESERT;
        }else if(x < -10 && y <10 && y >-10 ){
            regionEnum = worldRegionEnum.BOG;
        }else if(x > 10  && y <10 && y >-10){
            regionEnum = worldRegionEnum.MOUNTAINS;
        }

        String mapKey = Integer.toString(x) + Integer.toString(y);

        Map newMap = Map(this.mapWidth,this.mapHeight,regionEnum);

        //Might do more stuff here 

        this.mapsHash.put(mapKey,newMap);

    
    }
    public Map getMap(int worldX, int worldY){
        String mapKey = Integer.toString(worldX) + Integer.toString(worldY);

        if(!this.mapsHash.containsKey(mapKey)){
            //generate a map
        }

        return(this.mapsHash.get(mapKey));
    }

}
