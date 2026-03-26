package interfaces;
import creatures.Player;
import com.googlecode.lanterna.gui2.*;
import enums.dungeonTileDirEnum;
public class DungeonInfoPanel extends InfoPanel {
    private Label directionLabel;
    public DungeonInfoPanel(Player player) {
        super(player);
        this.directionLabel = new Label("");
        addComponent(new Label("Facing"));
        addComponent(this.directionLabel);
        
    }

    public void update(String timeString,int x , int y,dungeonTileDirEnum dir) {
        super.update(timeString,x,y);
        this.directionLabel.setText(dir.toString());
    }
    
}
