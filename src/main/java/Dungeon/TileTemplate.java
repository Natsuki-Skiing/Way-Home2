package Dungeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Loads an ASCII tile image from disk (or, if that fails, from the classpath).
 */
public final class TileTemplate {
    private final Vector<String> image = new Vector<>();

    public TileTemplate(String fileLocation) {
        loadImage(fileLocation);
    }

    private void loadImage(String fileLocation) {
        // Filesystem path (dev-friendly)
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileLocation), StandardCharsets.UTF_8)) {
            String row;
            while ((row = reader.readLine()) != null) {
                image.add(row);
            }
            return;
        } catch (IOException ignored) {
            // Fall through to classpath loading.
        }

        // Classpath resource (packaging-friendly)
        String normalized = fileLocation.startsWith("/") ? fileLocation : "/" + fileLocation;
        try (InputStream is = TileTemplate.class.getResourceAsStream(normalized)) {
            if (is == null) {
                System.err.println("Error loading dungeon tile file: " + fileLocation);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String row;
                while ((row = reader.readLine()) != null) {
                    image.add(row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading dungeon tile file: " + fileLocation + " (" + e.getMessage() + ")");
        }
    }

    public Vector<String> getImage() {
        return image;
    }
}
