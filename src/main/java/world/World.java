package world;
import java.util.HashMap;
import enums.worldRegionEnum;
import util.FastNoiseLite;
import util.FastNoiseLite.NoiseType;
public class World {
    //I need to load already made maps here
    private HashMap<String,Map> mapsHash;
    private int mapWidth;
    private int mapHeight;
    private FastNoiseLite noiseMaker;

    public World(int mapWidth, int mapHeight,String existingMapsJsonPath){
        //TODO load edisting maps 

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        // Noise maker for the map type generation
        this.noiseMaker = new FastNoiseLite();
        this.noiseMaker.SetSeed(1337);
        this.noiseMaker.SetNoiseType(NoiseType.OpenSimplex2);

    }


    private String getMapKey(int x, int y){
        return((Integer.toString(x)+","+Integer.toString(y)));
    }

    private void genMap(int x,int y){
        float frequency = 0.05f;

        float noiseValue = this.noiseMaker.GetNoise(x*frequency, y*frequency);

        worldRegionEnum regionEnum;
        if (noiseValue < -0.4f) {
            regionEnum = worldRegionEnum.SNOW;
        } else if (noiseValue < -0.1f) {
            regionEnum = worldRegionEnum.MOUNTAINS;
        } else if (noiseValue < 0.2f) {
            regionEnum = worldRegionEnum.NORMAL;
        } else if (noiseValue < 0.5f) {
            regionEnum = worldRegionEnum.BOG;
        } else {
            regionEnum = worldRegionEnum.DESERT;
        }

        String mapKey = getMapKey(x, y);

        //Actually making the map
        Map newMap = new Map(this.mapWidth,this.mapHeight,regionEnum);

        //Might do more stuff here 

        this.mapsHash.put(mapKey,newMap);

    
    }
    public Map getMap(int worldX, int worldY){
        String mapKey = getMapKey(worldX, worldY);

        if(!this.mapsHash.containsKey(mapKey)){
            //generate a map
        }

        return(this.mapsHash.get(mapKey));
    }

}
