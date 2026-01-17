package creatures;
import enums.*;
import items.*;
import items.ChestClasses.Chest;
import items.ChestClasses.ChestItem;
import items.Instances.ItemInstance;
import items.templates.ItemTemplate;

import java.util.HashMap;

import com.googlecode.lanterna.TextColor;

import interfaces.GameTile;
public class Player extends Character {
    private int x = 0;
    private int y = 0;
    private int xp = 0;
    private int xpToNextLevel = 100;
    private Chest inventory = new Chest();
    private HashMap<itemTypeEnum, ItemInstance> equippedItems = new HashMap<>();
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

    public void addItemToInventory(ItemInstance item, int quantity) {
        this.inventory.addRegularItem(item, quantity);
    }

    public boolean hasFishingRodEquipped() {
        ItemInstance fishingRod = this.equippedItems.get(itemTypeEnum.FISHING_ROD);
        return fishingRod != null;
    }

    public boolean hasWeaponEquipped() {
        ItemInstance weapon = this.equippedItems.get(itemTypeEnum.WEAPON);
        return weapon != null;
    }

    public boolean hasArmorEquipped() {
        ItemInstance armor = this.equippedItems.get(itemTypeEnum.ARMOR);
        return armor != null;
    }

    private boolean hasItemEquipped(itemTypeEnum type) {
        ItemInstance item = this.equippedItems.get(type);
        return item != null;
    }

    public ChestItem takeItemFromInventory(ItemInstance item, int amount) {
        return takeItemFromInventory(item.getTemplate().getType(), item.getItemID(), amount);
    }
    public void unequipItem(ItemInstance item){
        this.equippedItems.put(item.getTemplate().getType(), null);
    }
    public boolean equipItem(ItemInstance item) {
        if(this.equippedItems.containsKey( item.getTemplate().getType())){
            this.equippedItems.put(item.getTemplate().getType(), item);
            return true;
        }
        return false;
    }
    


    public ChestItem takeItemFromInventory(itemTypeEnum type, int itemID, int amount) {
        if(this.inventory.getNoOfItem(type,itemID) == 1){
            if(hasItemEquipped(type) && this.equippedItems.get(type).getItemID() == itemID){
                this.equippedItems.put(type, null);
            }
        }
        return this.inventory.takeItem(type, itemID, amount);
    }

    public boolean checkIfEquipt(ItemInstance item){
        ItemInstance equippedItem = this.equippedItems.get(item.getTemplate().getType());
        return equippedItem != null && equippedItem.getItemID() == item.getItemID();
    }
    public ChestItem takeItemFromInventory(ChestItem chestItem) {
        return takeItemFromInventory(chestItem.getItem(), chestItem.getQuantity());
    }

}