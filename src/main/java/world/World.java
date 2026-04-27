package world;
import java.util.HashMap;
import enums.worldRegionEnum;
import util.FastNoiseLite;
import util.FastNoiseLite.NoiseType;

import java.beans.Transient;
import java.io.Serializable;
public class World implements Serializable {
    //I need to load already made maps here
    private HashMap<String,Map> mapsHash;
    private int mapWidth;
    private int mapHeight;
    private Map currentMap;
    private transient FastNoiseLite noiseMaker;
    private transient TileFactory tileFactory = new TileFactory("src/jsons/world/regionTiles/tiles.json");
    public World(int mapWidth, int mapHeight,String existingMapsJsonPath){
        //All the maps in the world are stored in a hash map with the key being the world coordinates of the map
        this.mapsHash = new HashMap<>();

        //TODO load edisting maps 

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        // Noise maker for the map type generation
        this.noiseMaker = new FastNoiseLite();
        this.noiseMaker.SetSeed(1337);
        this.noiseMaker.SetNoiseType(NoiseType.OpenSimplex2);

    }

    public void reinitialize(String mapsJsonPath, String tilesJsonPath) {
        this.tileFactory = new TileFactory(tilesJsonPath);
        this.noiseMaker = new FastNoiseLite();
        this.noiseMaker.SetSeed(1337); // Ensure this matches your original seed!
        this.noiseMaker.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
    }

    public void restoreAllTileColors() {
        this.mapsHash.values().parallelStream().forEach(map -> {
            map.restoreMapColors();
        });
    }


    private String getMapKey(int x, int y){
        return((Integer.toString(x)+","+Integer.toString(y)));
    }

    private void genMap(int x,int y){
        float frequency = 0.05f;

        float noiseValue = this.noiseMaker.GetNoise(x*frequency, y*frequency);

        float latitudeBiasStrength = 0.03f;

        float latitudeBias = y * latitudeBiasStrength;
        float biasedValue = Math.max(-1.0f, Math.min(1.0f, noiseValue + latitudeBias));

        worldRegionEnum regionEnum;
        if (biasedValue < -0.4f) {
            regionEnum = worldRegionEnum.SNOW;
        } else if (biasedValue < -0.1f) {
            regionEnum = worldRegionEnum.MOUNTAINS;
        } else if (biasedValue < 0.2f) {
            regionEnum = worldRegionEnum.NORMAL;
        } else if (biasedValue < 0.5f) {
            regionEnum = worldRegionEnum.BOG;
        } else {
            regionEnum = worldRegionEnum.DESERT;
        }

        String mapKey = getMapKey(x, y);

        //Actually making the map
        Map newMap = new Map(this.mapWidth,this.mapHeight,regionEnum,this.tileFactory);

       


        //Might do more stuff here 

        this.mapsHash.put(mapKey,newMap); 
        

    
    }
    public HashMap<String,Map> getHashMap(){
        return(this.mapsHash);
    }
    public boolean currentMapTileWalkable(int x, int y){
        return(this.currentMap.isTileWalkable(x, y));
    }
    public Map getMap(int worldX, int worldY){
        String mapKey = getMapKey(worldX, worldY);

        if(!this.mapsHash.containsKey(mapKey)){
            //generate a map
            genMap(worldX, worldY);//worldX worldY used for key gen
        }
        Map map = this.mapsHash.get(mapKey);
        this.currentMap = map;
        return(map);
    }

}
