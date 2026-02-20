package Dungeon;

import enums.dungeonTileDirEnum;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class DungeonTile {
    private String fileLocation;
    private Vector<dungeonTileDirEnum> connections;
    private Vector<String> image;

    public DungeonTile(String fileLocation, Vector<dungeonTileDirEnum> connections) {
        this.fileLocation = fileLocation;
        this.connections = connections;
        this.image = new Vector<>();
        loadImage();
    }

    private void loadImage() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.fileLocation))) {
            String currentRow;
            while ((currentRow = reader.readLine()) != null) {
                this.image.add(currentRow);
            }
        } catch (IOException e) {
            System.err.println("Error loading dungeon tile file: " + e.getMessage());
        }
    }

    public Vector<String> getImage() {
        return image;
    }

    public boolean hasConnection(dungeonTileDirEnum dir) {
        if(this.connections == null){
            return false;
        }
        return this.connections.contains(dir);
    }
}