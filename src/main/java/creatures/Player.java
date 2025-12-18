package creatures;
import enums.*;
import items.*;
import java.util.HashMap;

import com.googlecode.lanterna.TextColor;

import interfaces.GameTile;
public class Player extends Character {
    private int x = 0;
    private int y = 0;
    private int xp = 0;
    private int xpToNextLevel = 100;
    private Chest inventory = new Chest();
    private HashMap<itemTypeEnum, Item> equippedItems = new HashMap<>();
    private GameTile previousTile = null;
    private GameTile playerTile = new GameTile(TextColor.ANSI.BLACK, TextColor.ANSI.RED_BRIGHT, '@');
    public Player( String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,raceEnum race, int maxHp) {
        super(name, strength, perception, endurance, charisma, agility, luck,race,maxHp);
        this.equippedItems.put(itemTypeEnum.WEAPON, null);
        this.equippedItems.put(itemTypeEnum.ARMOR, null);
        this.equippedItems.put(itemTypeEnum.FISHING_ROD, null);
    } 

    
    public GameTile getPlayerTile() {
        return playerTile;
    }

    public void setPlayerTile(GameTile playerTile) {
        this.playerTile = playerTile;
    }
    public GameTile getPreviousTile() {
        return previousTile;
    }
    public void setPreviousTile(GameTile previousTile) {
        this.previousTile = previousTile;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getXp() {
        return xp;
    }
    public boolean addXp(int amount) {
        boolean hasLeveled = false;
        this.xp += amount;
        while (this.xp >= this.xpToNextLevel) {
            this.xp -= this.xpToNextLevel;
            levelUp(); 
            hasLeveled = true;
        }
        return hasLeveled;
 
    }
    private void levelUp(){}

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }
    public void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public Chest getInventory() {
        return inventory;
    }

    public void addItemToInventory(Item item, int quantity) {
        this.inventory.addRegularItem(item);
    }

    public boolean hasFishingRodEquipped() {
        Item fishingRod = this.equippedItems.get(itemTypeEnum.FISHING_ROD);
        return fishingRod != null;
    }

    public boolean hasWeaponEquipped() {
        Item weapon = this.equippedItems.get(itemTypeEnum.WEAPON);
        return weapon != null;
    }

    public boolean hasArmorEquipped() {
        Item armor = this.equippedItems.get(itemTypeEnum.ARMOR);
        return armor != null;
    }

    private boolean hasItemEquipped(itemTypeEnum type) {
        Item item = this.equippedItems.get(type);
        return item != null;
    }

    public ChestItem takeItemFromInventory(Item item, int amount) {
        return takeItemFromInventory(item.getType(), item.getName(), amount);
    }

    public boolean equipItem(Item item) {
        if(this.equippedItems.containsKey( item.getType())){
            this.equippedItems.put(item.getType(), item);
            return true;
        }
        return false;
    }
    public ChestItem takeItemFromInventory(itemTypeEnum type, String name, int amount) {
        if(this.inventory.getNoOfItem(type,name) == 1){
            if(hasItemEquipped(type) && this.equippedItems.get(type).getName() == name){
                this.equippedItems.put(type, null);
            }
        }

        return this.inventory.takeItem(type, name, amount);
    }

    public ChestItem takeItemFromInventory(ChestItem chestItem) {
        return takeItemFromInventory(chestItem.getItem(), chestItem.getQuantity());
    }

}