package interfaces;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import creatures.Player;
import enums.itemTypeEnum;
import items.ChestClasses.Chest;
import items.ChestClasses.ChestItem;
import items.ItemManager.ItemController;
import vendors.Vendor;


import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TradeInterface {
    private static final double SELL_RATE = 0.5;

    private final Player player;
    private final Vendor vendor;
    private final WindowBasedTextGUI textGUI;
    private final ItemController itemController;
    private final int clockTime;

    // 0 = player panel, 1 = pending panel, 2 = vendor panel
    private int activePanel = 2;

    private Window window;
    private Chest vendorChest;

    // Player panel
    private ActionListBox playerList;
    private Label playerCatLabel;
    private List<itemTypeEnum> playerCats;
    private int playerCatIndex = 0;

    // Vendor panel
    private ActionListBox vendorList;
    private Label vendorCatLabel;
    private List<itemTypeEnum> vendorCats;
    private int vendorCatIndex = 0;

    // Pending panel
    private ActionListBox pendingList;
    private final Vector<PendingEntry> pending = new Vector<>();

    // Transaction summary
    private Label playerGivesLabel;
    private Label playerGetsLabel;
    private Label fundsLabel;
    private Label netLabel;

    private static class PendingEntry {
        final ChestItem chestItem;
        final boolean isBuy;
        final long price;

        PendingEntry(ChestItem chestItem, boolean isBuy, long price) {
            this.chestItem = chestItem;
            this.isBuy = isBuy;
            this.price = price;
        }
    }

    public TradeInterface(Vendor vendor, Player player, WindowBasedTextGUI textGUI,
                          ItemController itemController, int clockTime) {
        this.vendor = vendor;
        this.player = player;
        this.textGUI = textGUI;
        this.itemController = itemController;
        this.clockTime = clockTime;
        this.vendorChest = vendor.getVendorChest(player, clockTime, itemController);
        buildWindow();
    }

    private void buildWindow() {
        window = new BasicWindow("Trading with " + vendor.getName());
        window.setHints(Arrays.asList(Window.Hint.NO_POST_RENDERING));

        Panel root = new Panel(new LinearLayout(Direction.VERTICAL));
        Panel columns = new Panel(new LinearLayout(Direction.HORIZONTAL));

        // ── Left: player inventory ─────────────────────────────────────
        Panel leftOuter = new Panel(new LinearLayout(Direction.VERTICAL));
        playerCatLabel = new Label("All");
        playerList = new ActionListBox(new TerminalSize(26, 20));
        leftOuter.addComponent(new Label("x== Category ==x"));
        leftOuter.addComponent(playerCatLabel);
        leftOuter.addComponent(new Label("x================x"));
        leftOuter.addComponent(playerList);

        // ── Middle: pending + transaction summary ──────────────────────
        Panel middleOuter = new Panel(new LinearLayout(Direction.VERTICAL));
        pendingList = new ActionListBox(new TerminalSize(26, 10));
        middleOuter.addComponent(new Label("x========== All ==========x"));
        middleOuter.addComponent(pendingList);
        middleOuter.addComponent(new Label("x=== Transaction Value ===x"));
        playerGivesLabel = new Label("|Player Item Value:  G  0.00|");
        playerGetsLabel  = new Label("|Vendor Item Value:  G  0.00|");
        fundsLabel       = new Label("|Player Funds:       G " + fmt(player.getGold()) + "|");
        netLabel         = new Label("|Transaction Sum:    G " + fmt(player.getGold()) + "|");
        middleOuter.addComponent(playerGivesLabel);
        middleOuter.addComponent(playerGetsLabel);
        middleOuter.addComponent(fundsLabel);
        middleOuter.addComponent(netLabel);
        middleOuter.addComponent(new Label("x=========================x"));

        // ── Right: vendor inventory ────────────────────────────────────
        Panel rightOuter = new Panel(new LinearLayout(Direction.VERTICAL));
        vendorCatLabel = new Label("All");
        vendorList = new ActionListBox(new TerminalSize(26, 20));
        rightOuter.addComponent(new Label("x== Category ==x"));
        rightOuter.addComponent(vendorCatLabel);
        rightOuter.addComponent(new Label("x================x"));
        rightOuter.addComponent(vendorList);

        columns.addComponent(leftOuter.withBorder(Borders.doubleLine(player.getName())));
        columns.addComponent(middleOuter.withBorder(Borders.doubleLine("Pending")));
        columns.addComponent(rightOuter.withBorder(Borders.doubleLine(vendor.getName())));

        root.addComponent(columns);
        root.addComponent(new Label("Change Category: <- ->  ,Change Window: z x  ,Confirm Transaction: c  ,Exit b"));

        window.setComponent(root);
        setupInput();

        playerCats = player.getInventory().getTypesOfChest().stream()
                .filter(t -> !player.getInventory().getItemsByType(t).isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
        vendorCats = vendorChest.getTypesOfChest().stream()
                .filter(t -> !vendorChest.getItemsByType(t).isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));

        populatePlayerList();
        populateVendorList();
    }

    private void setupInput() {
        window.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
                boolean handled = false;

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    window.close(); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                    changeCat(-1); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                    changeCat(1); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    scrollActive(-1); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    scrollActive(1); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    handleEnter(); handled = true;
                } else if (keyStroke.getKeyType() == KeyType.Character) {
                    char ch = Character.toLowerCase(keyStroke.getCharacter());
                    if      (ch == 'b') { window.close();   handled = true; }
                    else if (ch == 'z') { switchPanel(-1);  handled = true; }
                    else if (ch == 'x') { switchPanel(1);   handled = true; }
                    else if (ch == 'c') { confirmTrade();   handled = true; }
                }

                if (handled) deliverEvent.set(false);
            }
        });
    }

    private void switchPanel(int dir) {
        activePanel = (activePanel + dir + 3) % 3;
    }

    private void scrollActive(int dir) {
        ActionListBox list = getActiveList();
        if (list == null || list.getItemCount() == 0) return;
        int idx = Math.max(0, Math.min(list.getSelectedIndex() + dir, list.getItemCount() - 1));
        list.setSelectedIndex(idx);
    }

    private ActionListBox getActiveList() {
        return switch (activePanel) {
            case 0 -> playerList;
            case 1 -> pendingList;
            case 2 -> vendorList;
            default -> null;
        };
    }

    private void changeCat(int dir) {
        if (activePanel == 0 && !playerCats.isEmpty()) {
            playerCatIndex = (playerCatIndex + dir + playerCats.size()) % playerCats.size();
            populatePlayerList();
        } else if (activePanel == 2 && !vendorCats.isEmpty()) {
            vendorCatIndex = (vendorCatIndex + dir + vendorCats.size()) % vendorCats.size();
            populateVendorList();
        }
    }

    private void handleEnter() {
        switch (activePanel) {
            case 0 -> stagePlayerItem();
            case 1 -> unstageItem();
            case 2 -> stageVendorItem();
        }
    }

    private void stageVendorItem() {
        int idx = vendorList.getSelectedIndex();
        if (idx < 0 || vendorCats.isEmpty()) return;
        List<ChestItem> items = vendorChest.getItemsByType(vendorCats.get(vendorCatIndex));
        if (idx >= items.size()) return;
        ChestItem item = items.get(idx);
        // Use the vendor-modified price already on the cloned item
        pending.add(new PendingEntry(item, true, item.getItem().getValue()));
        refreshPending();
    }

    private void stagePlayerItem() {
        int idx = playerList.getSelectedIndex();
        if (idx < 0 || playerCats.isEmpty()) return;
        List<ChestItem> items = player.getInventory().getItemsByType(playerCats.get(playerCatIndex));
        if (idx >= items.size()) return;
        ChestItem item = items.get(idx);
        // Sell price is always 50% of the base template value, never the modified price
        long sellPrice = Math.round(item.getItem().getTemplate().getValue()*SELL_RATE);
        pending.add(new PendingEntry(item, false, sellPrice));
        refreshPending();
    }

    private void unstageItem() {
        int idx = pendingList.getSelectedIndex();
        if (idx < 0 || idx >= pending.size()) return;
        pending.remove(idx);
        refreshPending();
    }

    private void confirmTrade() {
        long cost    = vendorTotal();
        long income  = playerTotal();
        long newGold = player.getGold() + (income -cost);

        if (newGold  < 0) {
            MessageDialog.showMessageDialog(textGUI, "Cannot Trade",
                    "Not enough gold!", MessageDialogButton.OK);
            return;
        }

        for (PendingEntry e : pending) {
            if (e.isBuy) {
                player.addItemToInventory(e.chestItem.getItem().copy(), 1);
            } else {
                player.takeItemFromInventory(e.chestItem.getItem(), 1);
            }
        }
        player.setGold(newGold);
        pending.clear();

        playerCats = player.getInventory().getTypesOfChest().stream()
                .filter(t -> !player.getInventory().getItemsByType(t).isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
        if (playerCatIndex >= playerCats.size()) playerCatIndex = 0;

        populatePlayerList();
        refreshPending();
    }

    private void populateVendorList() {
        vendorList.clearItems();
        if (vendorCats.isEmpty()) return;
        itemTypeEnum type = vendorCats.get(vendorCatIndex);
        vendorCatLabel.setText(type.name());
        for (ChestItem item : vendorChest.getItemsByType(type)) {
            String line = padRight(item.getName(), 18)
                    + String.format("%8.2f", item.getItem().getValue());
            vendorList.addItem(line, () -> {});
        }
        if (vendorList.getItemCount() > 0) vendorList.setSelectedIndex(0);
    }

    private void populatePlayerList() {
        playerList.clearItems();
        if (playerCats.isEmpty()) { playerCatLabel.setText("-"); return; }
        itemTypeEnum type = playerCats.get(playerCatIndex);
        playerCatLabel.setText(type.name());
        for (ChestItem item : player.getInventory().getItemsByType(type)) {
            long sell = Math.round(item.getItem().getTemplate().getValue() *SELL_RATE);
            String line = padRight(item.getName(), 16)
                    + String.format("%4d", item.getQuantity())
                    + String.format("%7.2f", sell);
            playerList.addItem(line, () -> {});
        }
        if (playerList.getItemCount() > 0) playerList.setSelectedIndex(0);
    }

    private void refreshPending() {
        pendingList.clearItems();
        for (PendingEntry e : pending) {
            String prefix = e.isBuy ? "+" : "-";
            String line = prefix + " " + padRight(e.chestItem.getName(), 17)
                    + String.format("%7.2f", e.price);
            pendingList.addItem(line, () -> {});
        }
        if (!pending.isEmpty()) pendingList.setSelectedIndex(pending.size() - 1);
        
    }

    

    private String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }

    public void show() {
        textGUI.addWindowAndWait(window);
    }
}
