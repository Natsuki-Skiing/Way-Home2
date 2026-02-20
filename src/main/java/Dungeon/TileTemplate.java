package Dungeon;

import enums.dungeonTileDirEnum;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class TileTemplate { 
    private Vector<String> image;

    public TileTemplate(String fileLocation) {
        
        this.image = new Vector<>();
        loadImage(fileLocation);
    }

    private void loadImage(String fileLocation) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
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
}

    