package interfaces;



import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import creatures.Player;
import items.Enchantment.Enchantment;
import items.Enchantment.TemporyEnchantment;
import java.util.Arrays;

public class PlayerStatusScreen{

    public void showStatusScreen(Player player, WindowBasedTextGUI textGui) {
        // Create a centered window

        BasicWindow statusWindow = new BasicWindow(player.getName());
        statusWindow.setHints(Arrays.asList(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        // --- Stats Section (XP, Gold, Health) ---
        Panel statsPanel = new Panel(new GridLayout(2));
        
        statsPanel.addComponent(new Label("Health:"));
        statsPanel.addComponent(new Label(String.format("%.1f / %.0f", player.getHp(), player.getMaxHp()))
                .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT));

        statsPanel.addComponent(new Label("Current XP:"));
        statsPanel.addComponent(new Label(String.valueOf(player.getXp()))); // From Player.java

        statsPanel.addComponent(new Label("XP to Level:"));
        statsPanel.addComponent(new Label(String.valueOf(player.getXpToNextLevel()))); // From Player.java

        statsPanel.addComponent(new Label("Gold:"));
        // Assuming a getGold() method exists for the BigDecimal field seen in your score logic
        statsPanel.addComponent(new Label(player.getGold().toString())
                .setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT));

        mainPanel.addComponent(statsPanel.withBorder(Borders.singleLine("Character Status")));
        mainPanel.addComponent(new EmptySpace());

        Panel skillPanel =new Panel(new LinearLayout(Direction.VERTICAL));
        skillPanel.addComponent(new Label("PER "+player.getPerception()));
        skillPanel.addComponent(new Label("STR "+player.getStrength()));
        skillPanel.addComponent(new Label("END "+player.getEndurance()));
        skillPanel.addComponent(new Label("AGL "+player.getAgility()));
        skillPanel.addComponent(new Label("LUK "+player.getLuck()));

        mainPanel.addComponent(skillPanel.withBorder(Borders.singleLine("Attributes")));
        mainPanel.addComponent(new EmptySpace());
        // --- Enchantments Section ---
        Panel enchantmentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        
        // Fetch enchantments from Character.java
        if (player.getCurrEnchantments().isEmpty()) {

            enchantmentPanel.addComponent(new Label("No active effects."));
        } else {
            for (Enchantment enchantment : player.getCurrEnchantments()) {
                String effectText = enchantment.getName();
                
                // Check if it's a TemporyEnchantment to display duration
                if (enchantment instanceof TemporyEnchantment tempEnchant) {
                    int remaining = tempEnchant.getTimerValue();
                    effectText += String.format(" (%d turns left)", remaining);
                } else {
                    effectText += " (Permanent)";
                }
                
                enchantmentPanel.addComponent(new Label("- " + effectText)
                        .setForegroundColor(TextColor.ANSI.CYAN));
            }
        }

        mainPanel.addComponent(enchantmentPanel.withBorder(Borders.singleLine("Active Effects")));
        mainPanel.addComponent(new EmptySpace());

        // --- Navigation ---
        mainPanel.addComponent(new Button("Close", statusWindow::close)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        statusWindow.setComponent(mainPanel);
        textGui.addWindowAndWait(statusWindow);
    }
}
