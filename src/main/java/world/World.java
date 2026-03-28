package world;
import java.util.HashMap;

public class World {
    //I need to load already made maps here
    private HashMap<String,Map> mapsHash;
    private 


    

    private void genMap(int x,int y){

    }
    public Map getMap(int worldX, int worldY){
        String mapKey = Integer.toString(worldX) + Integer.toString(worldY);

        if(!this.mapsHash.containsKey(mapKey)){
            //generate a map
        }

        return(this.mapsHash.get(mapKey));
    }

}
