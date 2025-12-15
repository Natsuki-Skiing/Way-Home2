import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalSize;
import java.io.IOException;
import java.util.Arrays;

public class TerminalApp {

    public static void main(String[] args) {
        try {
            // 1. Setup the terminal and screen layers
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // 2. Create a window to hold components
            BasicWindow mainWindow = new BasicWindow();
            mainWindow.setHints(Arrays.asList(
                // This hint tells the window to fill the available terminal space
                // rather than shrinking to fit the components
                Window.Hint.FULL_SCREEN
            ));

            // 3. Create a panel (layout manager) to hold widgets
            Panel mainPanel = new Panel();
            mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            // 4. Add components
            Label titleLabel = new Label("System Control Panel");
            titleLabel.setForegroundColor(TextColor.ANSI.CYAN);
            mainPanel.addComponent(titleLabel);

            // Add a blank space
            mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            Button exitButton = new Button("Exit Application", new Runnable() {
                @Override
                public void run() {
                    mainWindow.close();
                }
            });
            mainPanel.addComponent(exitButton);

            // 5. Add the panel to the window
            mainWindow.setComponent(mainPanel);

            // 6. Create the GUI interface and wait for the window to close
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            gui.addWindowAndWait(mainWindow);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}