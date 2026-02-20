package Dungeon;
import java.util.HashMap;
import java.util.Random;
import enums.dungeonTileDirEnum;
import creatures.*;
import clock.*;
import java.util.random.*;
import java.util.Vector;
import java.util.Arrays;
import Combat.CombatEncounter;
import enums.dungeonTileTypeEnum;
public class Dungeon {
    private HashMap<String, DungeonTile> tiles;
    private DungeonTile currentTile;
    private Player player;
    private dungeonTileDirEnum playerFacing = dungeonTileDirEnum.NORTH;
    private CreatureController creatureController;
    private Clock clock;
    Random randomGen = new Random();
    private int playerX;
    private int playerY;
    private TileHolderClass tileHolder;
    private double distanceMultiplier;
    public Dungeon(Player player,CreatureController creatureController,Clock clock,TileHolderClass tileHolder,int distanceFromStart){ 
        this.player = player;
        this.clock = clock;
        this.tiles = new HashMap<>();
        this.playerX = 0;
        this.playerY = -1;
        this.tileHolder = tileHolder;
        this.distanceMultiplier = 1 + (distanceFromStart * 0.1);
        this.creatureController = creatureController;
        this.movePlayer(playerFacing);
    }

    private void combatCheck(){
        int randomNum = randomGen.nextInt(100);
        int chance = 30;
        if(this.clock.isNight()){
            chance += 20;
        }
        if(randomNum < chance){
            //TODO combat encounter
        }
    }

    public dungeonTileDirEnum getPlayerFacing() {
        return playerFacing;
    }
    public void setPlayerFacing(dungeonTileDirEnum playerFacing) {
        this.playerFacing = playerFacing;
    }
    public Vector<String> getCurrentTileImage(){
        return this.tileHolder.getTileImage(this.currentTile, this.playerFacing);
    }

    public void movePlayer(dungeonTileDirEnum dir){
        if(this.currentTile.hasConnection(dir)){
            switch(dir){
                case NORTH:
                    this.playerY -= 1;
                    break;
                case SOUTH:
                    this.playerY += 1;
                    break;
                case EAST:
                    this.playerX += 1;
                    break;
                case WEST:
                    this.playerX -= 1;
                    break;
            }
            // If file exists then load it otherwise generate on the fly and add to hashmap
            String tileKey = this.playerX + "," + this.playerY;
            if(!this.tiles.containsKey(tileKey)){
                // On the fly generation 

                //Checking to see all vaild connections 
                Vector<dungeonTileDirEnum> newConnections = getValidConnections(this.playerX, this.playerY);

                //Geting my corridor segment based on open connections
                dungeonTileTypeEnum segmentType = determineTileType(newConnections);

                DungeonTile newTile = new DungeonTile(segmentType, newConnections);
                this.tiles.put(tileKey, newTile);


            }
            this.currentTile = this.tiles.get(tileKey);
        }
    }

    
    private boolean checkNeighborConnection(DungeonTile neighbor, dungeonTileDirEnum requiredDir) {
        if (neighbor != null) {
            // If the neighbor exists, strictly connect ONLY if they have an opening facing us
            return neighbor.hasConnection(requiredDir);
        }
        // If the space is empty, randomly decide whether to leave an open path for the future
        return this.randomGen.nextBoolean();
    }

    private Vector<dungeonTileDirEnum> getValidConnections(int targetX, int targetY) {
        Vector<dungeonTileDirEnum> validConnections = new Vector<>();

        DungeonTile northNeighbor = this.tiles.get(targetX + "," + (targetY - 1));
        DungeonTile southNeighbor = this.tiles.get(targetX + "," + (targetY + 1));
        DungeonTile eastNeighbor = this.tiles.get((targetX + 1) + "," + targetY);
        DungeonTile westNeighbor = this.tiles.get((targetX - 1) + "," + targetY);

        if (checkNeighborConnection(northNeighbor, dungeonTileDirEnum.SOUTH)) {
            validConnections.add(dungeonTileDirEnum.NORTH);
        }
        if (checkNeighborConnection(southNeighbor, dungeonTileDirEnum.NORTH)) {
            validConnections.add(dungeonTileDirEnum.SOUTH);
        }
        if (checkNeighborConnection(eastNeighbor, dungeonTileDirEnum.WEST)) {
            validConnections.add(dungeonTileDirEnum.EAST);
        }
        if (checkNeighborConnection(westNeighbor, dungeonTileDirEnum.EAST)) {
            validConnections.add(dungeonTileDirEnum.WEST);
        }

        return validConnections;
}
    private dungeonTileTypeEnum determineTileType(Vector<dungeonTileDirEnum> connections){
        int size = connections.size();
        
        // 1 Connection: Dead End
        if (size == 1) {
            return dungeonTileTypeEnum.END;
        } 
        // 3 or 4 Connections: Intersection
        else if (size >= 3) {
            return dungeonTileTypeEnum.CROSSROAD;
        } 
        // 2 Connections: Hallway or Corner
        else if (size == 2) {
            boolean hasN = connections.contains(dungeonTileDirEnum.NORTH);
            boolean hasS = connections.contains(dungeonTileDirEnum.SOUTH);
            boolean hasE = connections.contains(dungeonTileDirEnum.EAST);
            boolean hasW = connections.contains(dungeonTileDirEnum.WEST);

            if ((hasN && hasS) || (hasE && hasW)) {
                return dungeonTileTypeEnum.HALLWAY;
            } else if (hasN && hasE) {
                return dungeonTileTypeEnum.NORTH_CORNER; // Maps to North-East
            } else if (hasN && hasW) {
                return dungeonTileTypeEnum.WEST_CORNER;  // Maps to North-West
            } else if (hasS && hasE) {
                return dungeonTileTypeEnum.EAST_CORNER;  // Maps to South-East
            } else if (hasS && hasW) {
                return dungeonTileTypeEnum.SOUTH_CORNER; // Maps to South-West
            }
        }
        
        return dungeonTileTypeEnum.END; 
    }

}

