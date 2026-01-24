package interfaces;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import creatures.Player;
import enums.itemTypeEnum;
import items.ChestClasses.Chest;
import items.ChestClasses.ChestItem;
import items.Instances.ConditionInstance;
import items.Instances.ItemInstance;
import items.Instances.WeaponInstance;
import items.templates.ItemTemplate;
import items.templates.SheildTemplate;
import items.templates.WeaponTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChestContentsPanel extends Panel {
    private final Player player;
    private final Chest inventory;
    private final Label currentCatLabel;
    private final TextBox itemDesBox;
    private final TextBox itemStatBox;
    private final ListeningActionListBox table;
    private final ArrayList<itemTypeEnum> catList;
    private int currentCatIndex = 0;
    private WindowBasedTextGUI textGUI;

    public ChestContentsPanel(Player player, Chest inventorySource,WindowBasedTextGUI textGUI) {
        this.player = player;
        this.inventory = inventorySource;
        this.catList = this.inventory.getTypesOfChest();
        this.textGUI = textGUI;

        this.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        Panel listPanel = new Panel();
        Panel infoPanel = new Panel();

        this.currentCatLabel = new Label(" ");
        this.table = new ListeningActionListBox();
        
        listPanel.addComponent(currentCatLabel);
        listPanel.addComponent(this.table);
        
        String title = (player != null && inventorySource == player.getInventory()) 
                ? "Inventory of " + player.getName() 
                : "Container";
                
        this.addComponent(listPanel.withBorder(Borders.doubleLine(title)));

        this.itemDesBox = new TextBox(new TerminalSize(50, 10));
        this.itemStatBox = new TextBox(new TerminalSize(50, 10));
        this.itemDesBox.setReadOnly(true);
        this.itemStatBox.setReadOnly(true);

        infoPanel.addComponent(this.itemDesBox.withBorder(Borders.singleLine("Description")));
        infoPanel.addComponent(this.itemStatBox.withBorder(Borders.singleLine("Stats")));
        
        this.addComponent(infoPanel.withBorder(Borders.doubleLine("Item information")));

        setupInputHandling();
        changeCat(0);
    }

    private void setupInputHandling() {
        this.table.setInputFilter((interactable, keyStroke) -> {
            if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                changeCat(-1);
                return true;
            } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                changeCat(1);
                return true;
            }
            return false;
        });
    }

    private class ListeningActionListBox extends ActionListBox {
        @Override
        public Result handleKeyStroke(KeyStroke keyStroke) {
            Result result = super.handleKeyStroke(keyStroke);
            if (keyStroke.getKeyType() == KeyType.ArrowUp || keyStroke.getKeyType() == KeyType.ArrowDown) {
                updateDescription();
            }
            return result;
        }
    }

    private Vector<String> formatDescription(String description) {
        Vector<String> lines = new Vector<>();
        String[] words = description.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > this.itemDesBox.getSize().getColumns() - 1) {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
                currentLine.append(word);
            } else {
                if (currentLine.length() == 0) {
                    currentLine.append(word);
                } else {
                    currentLine.append(" ").append(word);
                }
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    private void updateDescription() {
        int index = this.table.getSelectedIndex();
        List<ChestItem> items = this.inventory.getItemsByType(getCurrentType());
        
        if (index < 0 || index >= items.size()) {
            this.itemDesBox.setText("");
            this.itemStatBox.setText("");
            return;
        }

        ItemInstance item = items.get(index).getItem();
        ItemTemplate template = item.getTemplate();
        
        this.itemDesBox.setText("");
        Vector<String> descLines = formatDescription(template.getDescription());
        for (String line : descLines) {
            this.itemDesBox.addLine(line);
        }

        this.itemStatBox.setText("");
        this.itemStatBox.addLine("Item type : " + template.getType().name());
        
        if (item instanceof WeaponInstance) {
            WeaponInstance weapon = (WeaponInstance) item;
            if (template instanceof WeaponTemplate) {
                WeaponTemplate weaponTemplate = (WeaponTemplate) template;
                this.itemStatBox.addLine("Damage : " + weapon.getDamage());
                this.itemStatBox.addLine("Weapon type : " + weaponTemplate.getWeaponType().name());
            }
        }
        
        if (item instanceof ConditionInstance) {
            ConditionInstance conItem = (ConditionInstance) item;
            this.itemStatBox.addLine("Condition : " + conItem.getCondition() + " / " + conItem.getMaxCondition());
        }
        
        this.itemStatBox.addLine("Value : ᚠ " + item.getValueAsString());
    }

    public void executeSelectedItem() {
    
        Runnable action = table.getSelectedItem();
        
        
        if (action != null) {
            action.run();
        }
}

    private void populateTable() {
        itemTypeEnum type = getCurrentType();
        ArrayList<ChestItem> itemList = this.inventory.getItemsByType(type);
        this.table.clearItems();
        
        for (ChestItem item : itemList) {
            String name = item.getName();
            String quantity = Integer.toString(item.getQuantity());
            String equipped = " ";
            
            if (this.player != null && this.player.checkIfEquipt(item.getItem())) {
                equipped = "E";
            }
            
            String label = name + " " + quantity + " " + equipped;
            this.table.addItem(label, () -> handleSelection(item));
        }
        
        if (!itemList.isEmpty()) {
            this.table.setSelectedIndex(0);
        }
        updateDescription();
    }
    private boolean weaponSheildCheck(ItemInstance item){
        boolean pass = true;
        ItemTemplate template = item.getTemplate();
        if (template.getType() == itemTypeEnum.WEAPON) {
            WeaponTemplate weaponTemplate = (WeaponTemplate) template;
            itemTypeEnum weaponType = weaponTemplate.getWeaponType();
            if (this.player.hasSheildEquipped() && (weaponType == itemTypeEnum.WEAPON_LARGE)) {
                pass = false;

            }
        }else if(template.getType() == itemTypeEnum.SHEILD){
            SheildTemplate sheildTemplate = (SheildTemplate) template;
            if(this.player.hasWeaponEquipped()){
                WeaponTemplate weaponTemplate = (WeaponTemplate) this.player.getEquippedWeapon().getTemplate();
                if(weaponTemplate.getWeaponType() == itemTypeEnum.WEAPON_LARGE){
                    pass = false;
                }
            }
        }

        return (pass);
        
    }
    private void handleSelection(ChestItem selectedItem) {
        final AtomicBoolean isEquippable = new AtomicBoolean(false);
        final AtomicBoolean isEquipped = new AtomicBoolean(false);
        String actionMsg = "Consume";

        if (selectedItem.getItem().getTemplate().getUseType() == enums.itemTypeEnum.EQUIPPABLE) {
            isEquippable.set(true);
            if (this.player != null && this.player.checkIfEquipt(selectedItem.getItem())) {
                isEquipped.set(true);
                actionMsg = "Unequip";
            } else {
                actionMsg = "Equip";
            }
        }
        
        ActionListDialog dialog = new ActionListDialogBuilder()
            .setTitle(selectedItem.getName())
            .setDescription("Choose action")
            .addAction(actionMsg, () -> {
                if (isEquippable.get() && this.player != null) {
                    
                    
                    // For balance can't use large weapon with sheild
                    
                    if(weaponSheildCheck(selectedItem.getItem())){
                        if (isEquipped.get()) {
                        player.unequipItem(selectedItem.getItem());
                        } else {
                            player.equipItem(selectedItem.getItem());
                        }
                    }else{
                        MessageDialog.showMessageDialog(
                        textGUI,
                        "Invalid Action", // Title
                        "Can't equip large weapon and shield at the same time", // Message
                        MessageDialogButton.OK // Buttons
                        );
                    }
                    
                } else {
                    //TODO
                    // Logic for consuming items
                }

                populateTable();
            })
            .addAction("Drop", () -> {
                if (this.player != null && player.checkIfEquipt(selectedItem.getItem()) && selectedItem.getQuantity() == 1) {
                    player.unequipItem(selectedItem.getItem());
                }
                inventory.takeItem(selectedItem.getItem(), 1);
                populateTable();
            })
            .addAction("Back", () -> {})
            .build();
            
        dialog.setComponent(dialog.getComponent().withBorder(Borders.doubleLine()));

    
        dialog.showDialog((WindowBasedTextGUI) getTextGUI());
    }

    public void changeCat(int modifier) {
        if (this.catList.isEmpty()) return;

        this.currentCatIndex += modifier;
        if (this.currentCatIndex > this.catList.size() - 1) {
            this.currentCatIndex = 0;
        } else if (this.currentCatIndex < 0) {
            this.currentCatIndex = this.catList.size() - 1;
        }
        this.currentCatLabel.setText(this.catList.get(this.currentCatIndex).name());
        this.populateTable();
    }

    private itemTypeEnum getCurrentType() {
        if (catList.isEmpty()) return null;
        return this.catList.get(this.currentCatIndex);
    }
}
