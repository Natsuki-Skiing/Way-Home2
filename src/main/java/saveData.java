

import java.io.Serializable;
import creatures.Player;
import world.World;

public class saveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Directly save the entire objects
    public Player savedPlayer;
    public World savedWorld;
    
    // Clock state (since the Thread inside Clock can't be saved)
    public int clockSeconds;
    public int clockMultiplier;
    public int clockSleepTime;
}