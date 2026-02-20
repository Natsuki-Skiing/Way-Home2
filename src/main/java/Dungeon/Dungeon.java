package Dungeon;
import java.util.HashMap;
import java.util.Random;
import enums.dungeonTileDirEnum;
import creatures.*;
import clock.*;
import java.util.random.*;

import Combat.CombatEncounter;
public class Dungeon {
    private HashMap<String, DungeonTile> tiles;
    private DungeonTile currentTile;
    private Player player;
    private Clock clock;
    Random randomGen = new Random();
    private int playerX;
    private int playerY;
    
    public Dungeon(Player player,CreatureController creatureController,Clock clock) {
        this.player = player;
        this.clock = clock;
        this.tiles = new HashMap<>();
        this.playerX = 0;
        this.playerY = 0;
    }

    private void generateLevel(){

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


            }
            this.currentTile = this.tiles.get(tileKey);
        }
    }

    private void combatEncounter(){
        
    }

}
