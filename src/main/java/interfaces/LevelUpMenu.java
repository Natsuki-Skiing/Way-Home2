package interfaces;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import enums.skillEnum;
import creatures.Player;
import java.util.Arrays;

/**
 * A Lanterna popup menu that lets the player choose which skill to improve.
 * The amount is decided by the caller and shown as a preview label.
 *
 * Usage:
 *   LevelUpMenu menu = new LevelUpMenu(gui);
 *   skillEnum chosen = menu.show(5);   // amount decided by the caller
 *   if (chosen != null) {
 *       // apply +5 to chosen skill
 *   }
 */
public class LevelUpMenu {

    private final WindowBasedTextGUI gui;

    /** Written by Confirm, stays null on Cancel. */
    private skillEnum result = null;

    public LevelUpMenu(WindowBasedTextGUI gui,Player player) {
        this.gui = gui;
    }

    /**
     * Opens the popup and blocks until the player confirms or cancels.
     *
     * @param amount the bonus that will be applied to whichever skill is chosen
     * @return the chosen {@link skillEnum}, or {@code null} if cancelled
     */
    public skillEnum show(int amount) {
        result = null;

        // ── Window ──────────────────────────────────────────────────────────
        BasicWindow window = new BasicWindow("  ★  LEVEL UP  ★  ");
        window.setHints(Arrays.asList(
                Window.Hint.MODAL,
                Window.Hint.CENTERED
        ));
        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        root.setPreferredSize(new TerminalSize(40, 16));

        // ── Header ──────────────────────────────────────────────────────────
        root.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        root.addComponent(new Label("Choose a attribute to improve:"));
        root.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // ── Skill selector ──────────────────────────────────────────────────
        ComboBox<skillEnum> skillBox = new ComboBox<>();
        skillBox.addItem(skillEnum.HP);
        skillBox.addItem(skillEnum.AGL);
        skillBox.addItem(skillEnum.CHR);
        skillBox.addItem(skillEnum.END);
        skillBox.addItem(skillEnum.LUK);
        skillBox.addItem(skillEnum.PER);
        skillBox.addItem(skillEnum.STR);


        skillBox.setPreferredSize(new TerminalSize(30, 1));
        root.addComponent(skillBox);

        root.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // ── Separator ────────────────────────────────────────────────────────
        root.addComponent(new Separator(Direction.HORIZONTAL)
                .setPreferredSize(new TerminalSize(38, 1)));
        root.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // ── Confirm / Cancel ─────────────────────────────────────────────────
        Panel buttonRow = new Panel(new LinearLayout(Direction.HORIZONTAL));

        buttonRow.addComponent(new Button("  Confirm  ", () -> {
            result = skillBox.getSelectedItem();
            window.close();
        }));

        buttonRow.addComponent(new EmptySpace(new TerminalSize(2, 1)));

    

        root.addComponent(buttonRow);

        // ── Show ─────────────────────────────────────────────────────────────
        window.setComponent(root);
        gui.addWindowAndWait(window);

        return result;
    }
}
