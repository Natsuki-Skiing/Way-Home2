package Dungeon;

import clock.Clock;
import creatures.CreatureController;
import creatures.Player;
import enums.dungeonTileDirEnum;
import enums.dungeonTileTypeEnum;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

public class Dungeon {
    private final HashMap<String, DungeonTile> tiles;
    private DungeonTile currentTile;

    private final Player player;
    private dungeonTileDirEnum playerFacing = dungeonTileDirEnum.NORTH;

    private final CreatureController creatureController;
    private final Clock clock;
    private final Random randomGen = new Random();

    private int playerX;
    private int playerY;

    private final TileHolderClass tileHolder;
    private final double distanceMultiplier;

    private final java.util.Set<String> visitedTiles = new java.util.HashSet<>();

    public Dungeon(Player player, CreatureController creatureController, Clock clock, TileHolderClass tileHolder, int distanceFromStart) {
        this.player = player;
        this.creatureController = creatureController;
        this.clock = clock;
        this.tiles = new HashMap<>();
        this.tileHolder = tileHolder;

        this.playerX = 0;
        this.playerY = 0;

        this.distanceMultiplier = 1 + (distanceFromStart * 0.1);

        generateStartingArea();

        // Larger budget + post-processing loops makes the layout feel less "tree-like".
        genarateDungeon(650);

        this.currentTile = this.tiles.get(this.playerX + "," + this.playerY);
        this.visitedTiles.add(this.playerX + "," + this.playerY);
    }

    private static final class QueueEdge {
        final String fromKey;
        final dungeonTileDirEnum direction;

        QueueEdge(String fromKey, dungeonTileDirEnum direction) {
            this.fromKey = fromKey;
            this.direction = direction;
        }
    }

    private void generateStartingArea() {
        Vector<dungeonTileDirEnum> exitConnections = new Vector<>();
        exitConnections.add(dungeonTileDirEnum.NORTH);
        exitConnections.add(dungeonTileDirEnum.SOUTH);
        DungeonTile exitTile = new DungeonTile(dungeonTileTypeEnum.EXIT, exitConnections);
        this.tiles.put("0,0", exitTile);

        Vector<dungeonTileDirEnum> hallwayConnections = new Vector<>();
        hallwayConnections.add(dungeonTileDirEnum.NORTH);
        hallwayConnections.add(dungeonTileDirEnum.SOUTH);
        DungeonTile startCorridor = new DungeonTile(dungeonTileTypeEnum.HALLWAY, hallwayConnections);
        this.tiles.put("0,-1", startCorridor);

        this.playerX = 0;
        this.playerY = -1;
        this.currentTile = startCorridor;
        this.playerFacing = dungeonTileDirEnum.NORTH;
    }


    private void genarateDungeon(int maxNumberOfTiles) {
        Queue<QueueEdge> openEdges = new LinkedList<>();
        int numberOfTiles = 1;

        // Start corridor opens northward.
        openEdges.add(new QueueEdge("0,-1", dungeonTileDirEnum.NORTH));

        while (!openEdges.isEmpty() && numberOfTiles < maxNumberOfTiles) {
            QueueEdge edge = openEdges.poll();

            int[] origin = parseKey(edge.fromKey);
            int[] offset = directionOffset(edge.direction);
            int nx = origin[0] + offset[0];
            int ny = origin[1] + offset[1];
            String nKey = nx + "," + ny;

            dungeonTileDirEnum backDir = opposite(edge.direction);

            // Neighbour already exists — only wire bidirectionally if BOTH sides already
            // intend to connect. Never force a new connection onto an existing tile.
            if (this.tiles.containsKey(nKey)) {
                DungeonTile existing = this.tiles.get(nKey);
                if (existing.hasConnection(backDir)) {
                    addConnectionToTile(edge.fromKey, edge.direction);
                }
                continue;
            }

            DungeonTile newTile = getNewTile(numberOfTiles, maxNumberOfTiles, backDir, edge.fromKey);
            this.tiles.put(nKey, newTile);
            numberOfTiles++;

            // Record the edge on the origin tile now that we know the neighbour exists.
            addConnectionToTile(edge.fromKey, edge.direction);

            if (newTile.getType() != dungeonTileTypeEnum.END &&
                newTile.getType() != dungeonTileTypeEnum.CHEST &&
                newTile.getType() != dungeonTileTypeEnum.EXIT) {

                // Snapshot connections before iterating so future addConnectionToTile
                // calls on this tile cannot cause ConcurrentModificationException.
                for (dungeonTileDirEnum dir : new java.util.ArrayList<>(newTile.getConnections())) {
                    if (dir != backDir) {
                        openEdges.add(new QueueEdge(nKey, dir));
                    }
                }
            }
        }

        // Seal, enforce crossroad separation, then sprinkle.
        sealOpenConnections();
        removeCrossroadAdjacency();
        sprinkleChests(18);
    }

    private void sealOpenConnections() {
        // Iterate until the map is fully consistent. Newly created END tiles may
        // themselves need sealing, so we loop until no changes occur.
        boolean changed = true;
        while (changed) {
            changed = false;
            String[] keys = this.tiles.keySet().toArray(new String[0]);
            for (String key : keys) {
                DungeonTile tile = this.tiles.get(key);
                int[] coords = parseKey(key);
                // Snapshot so we can mutate the live connections list safely.
                dungeonTileDirEnum[] conns = tile.getConnections().toArray(new dungeonTileDirEnum[0]);
                for (dungeonTileDirEnum dir : conns) {
                    int[] off = directionOffset(dir);
                    String nKey = (coords[0] + off[0]) + "," + (coords[1] + off[1]);
                    if (!this.tiles.containsKey(nKey)) {
                        // No neighbour at all — create a dead-end pointing back.
                        Vector<dungeonTileDirEnum> endConns = new Vector<>();
                        endConns.add(opposite(dir));
                        this.tiles.put(nKey, new DungeonTile(dungeonTileTypeEnum.END, endConns));
                        changed = true;
                    } else {
                        DungeonTile neighbour = this.tiles.get(nKey);
                        if (!neighbour.hasConnection(opposite(dir))) {
                            // Neighbour exists but does not agree — prune the dangling
                            // connection from this tile rather than corrupt the neighbour.
                            tile.getConnections().remove(dir);
                            normalizeTileType(key);
                            changed = true;
                            break; // restart this tile's loop since we just mutated it
                        }
                    }
                }
            }
        }
    }


    /**
     * Hard post-generation pass: a 4-way crossroad must never be directly
     * connected to another 4-way crossroad.  When such a pair is found the
     * shared connection is severed on both sides and each tile's type is
     * re-normalised.  The sealing pass is then re-run so that any tile that
     * now points into empty space gets a proper dead-end neighbour.
     */
    private void removeCrossroadAdjacency() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String key : this.tiles.keySet().toArray(new String[0])) {
                DungeonTile tile = this.tiles.get(key);
                if (tile.getConnections().size() != 4) continue; // only full 4-way crossroads trigger this rule

                int[] coords = parseKey(key);
                for (dungeonTileDirEnum dir : tile.getConnections().toArray(new dungeonTileDirEnum[0])) {
                    int[] off = directionOffset(dir);
                    String nKey = (coords[0] + off[0]) + "," + (coords[1] + off[1]);
                    DungeonTile neighbour = this.tiles.get(nKey);
                    if (neighbour == null) continue;
                    if (neighbour.getConnections().size() != 4) continue;

                    // Both sides are full crossroads — sever this shared edge.
                    tile.getConnections().remove(dir);
                    neighbour.getConnections().remove(opposite(dir));
                    normalizeTileType(key);
                    normalizeTileType(nKey);
                    changed = true;
                    break; // restart this tile's loop after mutating its connections
                }
            }
        }
        // Re-seal: severing edges may have left tiles pointing into empty space.
        sealOpenConnections();
    }

    private void sprinkleChests(int count) {
        java.util.List<String> candidates = new java.util.ArrayList<>();
        for (String key : this.tiles.keySet()) {
            DungeonTile tile = this.tiles.get(key);
            if (tile == null) continue;
            if (tile.getType() != dungeonTileTypeEnum.END) continue;
            if (tile.getConnections().size() != 1) continue;

            int[] c = parseKey(key);
            int manhattan = Math.abs(c[0]) + Math.abs(c[1]);
            if (manhattan < 6) continue; // keep spawn area clean

            candidates.add(key);
        }

        for (int i = 0; i < count && !candidates.isEmpty(); i++) {
            int idx = randomGen.nextInt(candidates.size());
            String key = candidates.remove(idx);
            DungeonTile tile = this.tiles.get(key);
            if (tile != null) {
                tile.setType(dungeonTileTypeEnum.CHEST);
            }
        }
    }

    private void normalizeTileType(String key) {
        DungeonTile tile = this.tiles.get(key);
        if (tile == null) return;

        if (tile.getType() == dungeonTileTypeEnum.EXIT || tile.getType() == dungeonTileTypeEnum.CHEST) {
            return;
        }

        int degree = tile.getConnections().size();
        if (degree <= 1) {
            tile.setType(dungeonTileTypeEnum.END);
            return;
        }
        if (degree == 4) {
            tile.setType(dungeonTileTypeEnum.CROSSROAD);
            return;
        }
        if (degree == 3) {
            // T-junction: renderer uses connection count to pick tJunction.txt.
            tile.setType(dungeonTileTypeEnum.CROSSROAD);
            return;
        }

        // degree == 2: hallway vs corner (cosmetic type; rendering uses connections anyway)
        boolean n = tile.hasConnection(dungeonTileDirEnum.NORTH);
        boolean s = tile.hasConnection(dungeonTileDirEnum.SOUTH);
        boolean e = tile.hasConnection(dungeonTileDirEnum.EAST);
        boolean w = tile.hasConnection(dungeonTileDirEnum.WEST);

        if ((n && s) || (e && w)) {
            tile.setType(dungeonTileTypeEnum.HALLWAY);
        } else if (n && e) {
            tile.setType(dungeonTileTypeEnum.NORTH_CORNER);
        } else if (n && w) {
            tile.setType(dungeonTileTypeEnum.WEST_CORNER);
        } else if (s && e) {
            tile.setType(dungeonTileTypeEnum.EAST_CORNER);
        } else if (s && w) {
            tile.setType(dungeonTileTypeEnum.SOUTH_CORNER);
        } else {
            tile.setType(dungeonTileTypeEnum.HALLWAY);
        }
    }

    private DungeonTile getNewTile(int currentCount, int maxTiles, dungeonTileDirEnum requiredConnection, String parentKey) {
        boolean nearEnd = currentCount > maxTiles * 0.97;

        Vector<dungeonTileDirEnum> connections = new Vector<>();
        dungeonTileTypeEnum type;

        if (nearEnd) {
            // Terminal tiles only very near the end.
            type = (randomGen.nextInt(100) < 30) ? dungeonTileTypeEnum.CHEST : dungeonTileTypeEnum.END;
            connections.add(requiredConnection);
            return new DungeonTile(type, connections);
        }

        // Shapes
        dungeonTileDirEnum[][] hallways = {
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.SOUTH},
            {dungeonTileDirEnum.EAST, dungeonTileDirEnum.WEST}
        };
        dungeonTileDirEnum[][] corners = {
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.EAST},
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.WEST},
            {dungeonTileDirEnum.SOUTH, dungeonTileDirEnum.EAST},
            {dungeonTileDirEnum.SOUTH, dungeonTileDirEnum.WEST}
        };
        dungeonTileDirEnum[][] tJunctions = {
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.SOUTH, dungeonTileDirEnum.EAST},
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.SOUTH, dungeonTileDirEnum.WEST},
            {dungeonTileDirEnum.NORTH, dungeonTileDirEnum.EAST,  dungeonTileDirEnum.WEST},
            {dungeonTileDirEnum.SOUTH, dungeonTileDirEnum.EAST,  dungeonTileDirEnum.WEST}
        };
        dungeonTileDirEnum[] crossroad = {
            dungeonTileDirEnum.NORTH, dungeonTileDirEnum.SOUTH,
            dungeonTileDirEnum.EAST, dungeonTileDirEnum.WEST
        };

        java.util.List<dungeonTileDirEnum[]> validHallways = new java.util.ArrayList<>();
        for (dungeonTileDirEnum[] h : hallways) if (containsDir(h, requiredConnection)) validHallways.add(h);

        java.util.List<dungeonTileDirEnum[]> validCorners = new java.util.ArrayList<>();
        for (dungeonTileDirEnum[] c : corners) if (containsDir(c, requiredConnection)) validCorners.add(c);

        java.util.List<dungeonTileDirEnum[]> validTJunctions = new java.util.ArrayList<>();
        for (dungeonTileDirEnum[] t : tJunctions) if (containsDir(t, requiredConnection)) validTJunctions.add(t);

        // A true 4-way crossroad may not be placed next to another crossroad.
        // Look up the actual parent tile type right now — not a stale cached value.
        DungeonTile parentTile = this.tiles.get(parentKey);
        boolean parentIsCrossroad = parentTile != null &&
            parentTile.getConnections().size() >= 4;
        boolean allowFullCrossroad = !parentIsCrossroad;

        int roll = randomGen.nextInt(100);

        if (roll < 18 && !validHallways.isEmpty()) {
            dungeonTileDirEnum[] chosen = validHallways.get(randomGen.nextInt(validHallways.size()));
            for (dungeonTileDirEnum d : chosen) connections.add(d);
            type = dungeonTileTypeEnum.HALLWAY;

        } else if (roll < 40 && !validCorners.isEmpty()) {
            dungeonTileDirEnum[] chosen = validCorners.get(randomGen.nextInt(validCorners.size()));
            for (dungeonTileDirEnum d : chosen) connections.add(d);
            type = cornerType(chosen);

        } else if (roll < 72 && !validTJunctions.isEmpty()) {
            dungeonTileDirEnum[] chosen = validTJunctions.get(randomGen.nextInt(validTJunctions.size()));
            for (dungeonTileDirEnum d : chosen) connections.add(d);
            type = dungeonTileTypeEnum.CROSSROAD;

        } else if (roll < 80) {
            connections.add(requiredConnection);
            type = (randomGen.nextInt(100) < 20) ? dungeonTileTypeEnum.CHEST : dungeonTileTypeEnum.END;

        } else if (allowFullCrossroad) {
            for (dungeonTileDirEnum d : crossroad) connections.add(d);
            type = dungeonTileTypeEnum.CROSSROAD;

        } else {
            // Parent is a full crossroad — use a T-junction or simpler tile instead.
            if (!validTJunctions.isEmpty()) {
                dungeonTileDirEnum[] chosen = validTJunctions.get(randomGen.nextInt(validTJunctions.size()));
                for (dungeonTileDirEnum d : chosen) connections.add(d);
                type = dungeonTileTypeEnum.CROSSROAD;
            } else if (!validCorners.isEmpty()) {
                dungeonTileDirEnum[] chosen = validCorners.get(randomGen.nextInt(validCorners.size()));
                for (dungeonTileDirEnum d : chosen) connections.add(d);
                type = cornerType(chosen);
            } else if (!validHallways.isEmpty()) {
                dungeonTileDirEnum[] chosen = validHallways.get(randomGen.nextInt(validHallways.size()));
                for (dungeonTileDirEnum d : chosen) connections.add(d);
                type = dungeonTileTypeEnum.HALLWAY;
            } else {
                connections.add(requiredConnection);
                type = dungeonTileTypeEnum.HALLWAY;
            }
        }

        return new DungeonTile(type, connections);
    }

    private dungeonTileTypeEnum cornerType(dungeonTileDirEnum[] conns) {
        boolean n = containsDir(conns, dungeonTileDirEnum.NORTH);
        boolean s = containsDir(conns, dungeonTileDirEnum.SOUTH);
        boolean e = containsDir(conns, dungeonTileDirEnum.EAST);
        boolean w = containsDir(conns, dungeonTileDirEnum.WEST);

        if (n && e) return dungeonTileTypeEnum.NORTH_CORNER;
        if (n && w) return dungeonTileTypeEnum.WEST_CORNER;
        if (s && e) return dungeonTileTypeEnum.EAST_CORNER;
        return dungeonTileTypeEnum.SOUTH_CORNER;
    }

    private boolean containsDir(dungeonTileDirEnum[] arr, dungeonTileDirEnum dir) {
        for (dungeonTileDirEnum d : arr) {
            if (d == dir) return true;
        }
        return false;
    }

    // Movement: does NOT rotate the player. Facing changes only via setPlayerFacing().
    public boolean movePlayer(dungeonTileDirEnum direction) {
        if (!canMove(direction)) {
            return false;
        }

        int[] offset = directionOffset(direction);
        int newX = this.playerX + offset[0];
        int newY = this.playerY + offset[1];
        String newKey = newX + "," + newY;

        this.playerX = newX;
        this.playerY = newY;
        this.currentTile = this.tiles.get(newKey);
        this.visitedTiles.add(newKey);

        combatCheck();
        return true;
    }

    public boolean canMove(dungeonTileDirEnum direction) {
        if (!this.currentTile.hasConnection(direction)) {
            return false;
        }

        int[] offset = directionOffset(direction);
        String newKey = (this.playerX + offset[0]) + "," + (this.playerY + offset[1]);

        DungeonTile next = this.tiles.get(newKey);
        if (next == null) {
            return false;
        }

        // Bidirectional collision check: destination must agree there's an opening back.
        return next.hasConnection(opposite(direction));
    }

    private void combatCheck() {
        int roll = randomGen.nextInt(100);
        int chance = 30;
        if (this.clock.isNight()) {
            chance += 20;
        }
        if (roll < chance) {
            // TODO: wire combat / encounter generation to creatureController
        }
    }

    private int[] directionOffset(dungeonTileDirEnum dir) {
        switch (dir) {
            case NORTH:
                return new int[]{0, -1};
            case SOUTH:
                return new int[]{0, 1};
            case EAST:
                return new int[]{1, 0};
            case WEST:
                return new int[]{-1, 0};
            default:
                return new int[]{0, 0};
        }
    }

    private dungeonTileDirEnum opposite(dungeonTileDirEnum dir) {
        switch (dir) {
            case NORTH:
                return dungeonTileDirEnum.SOUTH;
            case SOUTH:
                return dungeonTileDirEnum.NORTH;
            case EAST:
                return dungeonTileDirEnum.WEST;
            case WEST:
                return dungeonTileDirEnum.EAST;
            default:
                return dir;
        }
    }

    private int[] parseKey(String key) {
        String[] parts = key.split(",");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    private void addConnectionToTile(String key, dungeonTileDirEnum dir) {
        DungeonTile tile = this.tiles.get(key);
        if (tile == null) {
            return;
        }
        if (!tile.hasConnection(dir)) {
            tile.getConnections().add(dir);
        }
    }


    public int getCurrentTileConnectionCount() {
        return this.currentTile.getConnections().size();
    }

    public dungeonTileDirEnum getSingleExitExcluding(dungeonTileDirEnum excluded) {
        dungeonTileDirEnum found = null;
        for (dungeonTileDirEnum dir : this.currentTile.getConnections()) {
            if (dir == excluded) continue;
            if (found != null) return null;
            found = dir;
        }
        return found;
    }

    public dungeonTileDirEnum getPlayerFacing() {
        return playerFacing;
    }

    public void setPlayerFacing(dungeonTileDirEnum playerFacing) {
        this.playerFacing = playerFacing;
    }

    public Vector<String> getCurrentTileImage() {
        return this.tileHolder.getTileImage(this.currentTile, this.playerFacing);
    }

    public int getPlayerX() {
        return this.playerX;
    }

    public int getPlayerY() {
        return this.playerY;
    }

    public String getClockString() {
        return this.clock.getTimeString();
    }

    public HashMap<String, DungeonTile> getTiles() {
        return this.tiles;
    }

    public java.util.Set<String> getVisitedTiles() {
        return this.visitedTiles;
    }
}