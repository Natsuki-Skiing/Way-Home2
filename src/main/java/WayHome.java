import java.io.IOException;
import world.*;
import creatures.*;
import clock.*;

public class WayHome {

    //For jackson saving
    public WayHome (){}
    public static void main(String[] args) {

        if (System.getProperty("os.name", "").toLowerCase().contains("windows")) {
            String cmd = ProcessHandle.current().info().command().orElse("");
            if (!cmd.toLowerCase().contains("javaw")) {
                String javaw = cmd.replace("java.exe", "javaw.exe");
                try {
                    new ProcessBuilder(javaw, "-cp", System.getProperty("java.class.path"), "WayHome")
                        .inheritIO()
                        .start();
                } catch (IOException e) {
                    // Relaunch failed, just continue running as-is
                    new Game().main();
                    return ;
                }
                return;
            }
        }
        Game game;
        if(false){
            SaverLoader loader = new SaverLoader();
            saveData data = loader.loadGame("save.dat");
            Clock clock = new Clock(data.clockSeconds, data.clockSleepTime, data.clockMultiplier);
            game = new Game(data.savedPlayer, data.savedWorld,clock);
        }else{
            game = new Game();
        }
        
        
        game.main();
    }
}
