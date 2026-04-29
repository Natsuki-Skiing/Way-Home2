package interfaces;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import creatures.Player;

import java.util.Arrays;

public class DeathInterface {

    public void showDeathScreen(Player player, WindowBasedTextGUI textGui) {
        // 1. Calculate the final score using your existing logic
        int finalScore = player.calculateScore();

        // 2. Create a new window for the death screen
        BasicWindow deathWindow = new BasicWindow("Session Ended");
        deathWindow.setHints(Arrays.asList(Window.Hint.CENTERED));

        
        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Label headerLabel = new Label("GAME OVER")
                .setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        
        // Score Display
        Label scoreLabel = new Label("Final Score: " + finalScore);
        
        
        mainPanel.addComponent(new EmptySpace());
        mainPanel.addComponent(headerLabel);
        mainPanel.addComponent(new EmptySpace());
        mainPanel.addComponent(scoreLabel);
        mainPanel.addComponent(new EmptySpace());

        
        mainPanel.addComponent(new Button("Exit to Desktop", () -> {
            deathWindow.close();
            System.exit(0);
        }).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        
        deathWindow.setComponent(mainPanel);
        textGui.addWindowAndWait(deathWindow);
    }
}
