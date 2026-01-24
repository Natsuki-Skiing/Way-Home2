package creatures;
import enums.*;
import items.*;
import items.ChestClasses.Chest;
import items.ChestClasses.ChestItem;
import items.templates.ArmourTemplate;
import items.templates.ItemTemplate;
import items.Instances.*;
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
    private HashMap<enums.armourSlotEnum,ArmourInstance> armourSlots = new HashMap<>();
    private GameTile previousTile = null;
    private GameTile playerTile = new GameTile(TextColor.ANSI.BLACK, TextColor.ANSI.RED_BRIGHT, '@');
    public Player( String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,raceEnum race, int maxHp) {
        super(name, strength, perception, endurance, charisma, agility, luck,race,maxHp);
        this.equippedItems.put(itemTypeEnum.WEAPON, null);
        this.equippedItems.put(itemTypeEnum.FISHING_ROD, null);
        this.equippedItems.put(itemTypeEnum.SHEILD,null);

        this.armourSlots.put(enums.armourSlotEnum.BOOTS,null);
        this.armourSlots.put(enums.armourSlotEnum.CHEST_PLATE,null);
        this.armourSlots.put(enums.armourSlotEnum.GLOVES,null);
        this.armourSlots.put(enums.armourSlotEnum.HELMET,null);
        
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

    public boolean hasSheildEquipped(){
        ItemInstance sheild = this.equippedItems.get(itemTypeEnum.SHEILD);
        return sheild != null;
    }

    public boolean hasWeaponEquipped() {
        ItemInstance weapon = this.equippedItems.get(itemTypeEnum.WEAPON);
        return weapon != null;
    }

    public boolean hasArmorEquipped(enums.armourSlotEnum armourType) {
        ArmourInstance armor = this.armourSlots.get(armourType);
        return armor != null;
    }

    public boolean hasHelmetEquipped(){
        return hasArmorEquipped(enums.armourSlotEnum.HELMET);
    }

    public boolean hasChestPlateEquipped(){
        return hasArmorEquipped(enums.armourSlotEnum.CHEST_PLATE);
    }

    public boolean hasGlovesEquipped(){
        return hasArmorEquipped(enums.armourSlotEnum.GLOVES);
    }

    public boolean hasBootsEquipped(){
        return hasArmorEquipped(enums.armourSlotEnum.BOOTS);
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
    private boolean equipArmour(ArmourInstance item){
        ArmourTemplate template =(ArmourTemplate) item.getTemplate();
        if(this.armourSlots.containsKey(template.getArmourSlot())){
            this.armourSlots.put(template.getArmourSlot(), item);
            return true;
        }
        return false;
    }

    private boolean equipRegularItem(ItemInstance item){
        if(this.equippedItems.containsKey( item.getTemplate().getType())){
            this.equippedItems.put(item.getTemplate().getType(), item);
            return true;
        }
        return false;
    }
    public boolean equipItem(ItemInstance item) {
    
        if(item instanceof ArmourInstance armourItem){
            return(equipArmour(armourItem));
        }

        return(equipRegularItem(item));
        
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

    public WeaponInstance getEquippedWeapon() {
        ItemInstance weapon = this.equippedItems.get(itemTypeEnum.WEAPON);
        if (weapon != null && weapon instanceof WeaponInstance) {
            return (WeaponInstance) weapon;
        }
        return null;
    }
    @Override
    public double getEvasionChance(){
        double evasionChance = super.getEvasionChance();

        for(ArmourInstance armourItem: this.armourSlots.values()){
            ArmourTemplate template = (ArmourTemplate) armourItem.getTemplate()
            switch (template.getArmourType()) {
                case ARMOUR_LIGHT:
                    evasionChance -= 1.5;
                    break;
                
                case ARMOUR_MEDIUM:
                    evasionChance -= 2.5;
                    break;

                case ARMOUR_HEAVY:
                    evasionChance -= 3.0;
                    break;
                default:
                    break;
            }
        }
        return(evasionChance < 0.0) ? 0.0 : evasionChance;;
        
    }



}