import java.io.IOException;

public class WayHome {
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
                    return;
                }
                return;
            }
        }

        Game game = new Game();
        game.main();
    }
}
