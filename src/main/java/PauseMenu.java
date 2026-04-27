

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import java.util.Arrays;

public class PauseMenu {

    private final SaverLoader saverLoader;
    private final Game game;

    public PauseMenu(Game game, SaverLoader saverLoader) {
        this.game = game;
        this.saverLoader = saverLoader;
    }

    public void show(WindowBasedTextGUI textGui) {
        BasicWindow pauseWindow = new BasicWindow("Paused");
        pauseWindow.setHints(Arrays.asList(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        mainPanel.addComponent(new Label("  ** GAME PAUSED **  "));
        mainPanel.addComponent(new EmptySpace());

        mainPanel.addComponent(new Button("Resume Game", pauseWindow::close)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        mainPanel.addComponent(new EmptySpace());

        mainPanel.addComponent(new Button("Save Game", () -> {
            boolean success = saverLoader.saveGame(game.prepareSave());
            MessageDialog.showMessageDialog(textGui, "Save",
                    success ? "Game saved successfully!" : "Save failed!",
                    MessageDialogButton.OK);
        }).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        pauseWindow.setComponent(mainPanel);
        textGui.addWindowAndWait(pauseWindow);
    }
}