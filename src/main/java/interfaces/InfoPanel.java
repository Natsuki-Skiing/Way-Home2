package interfaces;
import creatures.Player;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.util.regex.Pattern;
public class InfoPanel extends Panel {
    private Player player;
    private HealthBar healthBar;
    private Label positionLabel;
    private Label timeLabel = new Label("");
    public InfoPanel(Player player) {
        this.player = player;
        this.healthBar = new HealthBar(player);
        this.setLayoutManager(new GridLayout(2));
        this.addComponent(new Label("Health :"));
        this.addComponent(healthBar);
        this.addComponent(new Label("Position: "));
        //TODO ADD player position label 
        this.addComponent(new EmptySpace());
        this.addComponent(new Label("Time: "));
        this.addComponent(timeLabel);


        update("");
    } 
    public void update(String timeString) {
        this.healthBar.update();
        this.timeLabel.setText(timeString);
    }


}
