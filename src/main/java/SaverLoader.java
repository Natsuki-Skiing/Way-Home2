import java.io.*;

public class SaverLoader {

    public saveData loadGame(String savePath) {
        saveData saveLoad = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(savePath))) {
            saveLoad = (saveData) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No save file found at: " + savePath);
        } catch (IOException e) {
            System.out.println("Failed to load save: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Save file is corrupted or outdated: " + e.getMessage());
        }

        return saveLoad;
    }

    public boolean saveGame(saveData gameToSave) {
        boolean saveSuccess = false;

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.dat"))) {
            out.writeObject(gameToSave);
            saveSuccess = true;
        } catch (IOException e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("error.log", true))) {
                writer.println("--- Save Failure ---");
                e.printStackTrace(writer);
                writer.println("\n");
            } catch (IOException logException) {
                logException.printStackTrace();
            }
        }

        return saveSuccess;
    }
}