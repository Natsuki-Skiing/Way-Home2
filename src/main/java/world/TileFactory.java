package world;

import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.TextColor;

import enums.worldRegionEnum;
import enums.worldTileTypeEnum;
import interfaces.GameTile;

public class TileFactory {

    private static class TileDefinition {
        @JsonProperty("char")     public String  character;
        @JsonProperty("fgColor")  public String  fgColor;
        @JsonProperty("bgColor")  public String  bgColor;
        @JsonProperty("walkable") public boolean walkable;
    }

    private HashMap<worldRegionEnum, HashMap<worldTileTypeEnum, Vector<GameTile>>> regionOuterMap;

    public TileFactory(String tilesJsonPath) {
        this.regionOuterMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Map<String, List<TileDefinition>>> raw;
        try {
            raw = mapper.readValue(
                new File(tilesJsonPath),
                new TypeReference<Map<String, Map<String, List<TileDefinition>>>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tiles from: " + tilesJsonPath, e);
        }

        for (Map.Entry<String, Map<String, List<TileDefinition>>> regionEntry : raw.entrySet()) {
            worldRegionEnum region = worldRegionEnum.valueOf(regionEntry.getKey());
            HashMap<worldTileTypeEnum, Vector<GameTile>> typeMap = new HashMap<>();

            for (Map.Entry<String, List<TileDefinition>> typeEntry : regionEntry.getValue().entrySet()) {
                worldTileTypeEnum tileType = worldTileTypeEnum.valueOf(typeEntry.getKey());
                Vector<GameTile> tiles = new Vector<>();

                for (TileDefinition def : typeEntry.getValue()) {
                    TextColor fg = TextColor.Factory.fromString(def.fgColor);
                    TextColor bg = TextColor.Factory.fromString(def.bgColor);
                    tiles.add(new GameTile(bg, fg, def.character.charAt(0), def.walkable));
                }

                typeMap.put(tileType, tiles);
            }

            regionOuterMap.put(region, typeMap);
        }
    }

    public GameTile getTile(worldRegionEnum region, worldTileTypeEnum type){
        Vector<GameTile> tiles = regionOuterMap.get(region).get(type);
        if (tiles == null || tiles.isEmpty()) {
            throw new IllegalArgumentException("No tiles found for region: " + region + ", type: " + type);
        }
        int index = (int) (Math.random() * tiles.size());
        return tiles.get(index);
    }
}