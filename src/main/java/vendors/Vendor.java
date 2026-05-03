package vendors;
import items.*;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Vector;
import enums.*;
import items.ChestClasses.*;
import items.Instances.ItemInstance;
import items.ItemManager.ItemController;
import creatures.Player;
import nameGen.*;
import java.util.ArrayList;
public class Vendor {
    private String name;
    private raceEnum race;
    private Chest baseChest;
    private ItemController itemController;
    private int lastUpdate = -1;
    private Chest modifiedChest;
    private Vector<itemTypeEnum> typeSells;
    private itemTypeEnum[] sellableItems = {itemTypeEnum.ARMOR,itemTypeEnum.FOOD,itemTypeEnum.WEAPON,itemTypeEnum.SHEILD,itemTypeEnum.POTION,itemTypeEnum.FISHING_ROD};
    private static final Random randGen = new Random();
    private static String nameGen(){
        NameGenerator nameGen = new NameGenerator();
        return(nameGen.genName());
    }

    public Vendor(String name,raceEnum race,Vector<itemTypeEnum> sells ){
        this.name = name;
        this.baseChest = new Chest();
        this.race = race;
        this.modifiedChest = new Chest();
        this.typeSells = sells;
    } 

    private static raceEnum getRandomRace(){
        raceEnum[] races = raceEnum.values();
        return(races[randGen.nextInt(races.length)]);
    }

    public Vendor(Vector<itemTypeEnum> sells ){
       this(nameGen(),getRandomRace(),sells);

    }

    public Vendor(raceEnum race, Vector<itemTypeEnum> sells){
        this(nameGen(),race,sells);
    }

    private void populateBaseChest(ItemController itemController){
        int numberOfItems = 50;
        this.baseChest = new Chest();

        for (int i = 0; i < numberOfItems; i++) {
            // 1. Pick a random index from the typeSells vector
            int randomIndex = randGen.nextInt(this.typeSells.size());
            
            // 2. Get the specific enum type
            itemTypeEnum selectedType = this.typeSells.get(randomIndex);

            this.baseChest.addRegularItem(itemController.getItem(selectedType));
        }


    }

    private void prepareVendorForPlayer(BigDecimal priceMultiplier){
        this.modifiedChest = new Chest();


        for(itemTypeEnum type : this.baseChest.getTypesOfChest()){
            ArrayList<ChestItem> baseItems = this.baseChest.getItemsByType(type);

            for (ChestItem originalChestItem : baseItems) {
                // 1. Create a deep copy of the item so the base price isn't overwritten
                ItemInstance clonedItem = originalChestItem.getItem().copy();

                // 2. Calculate the new price based on player stats
                BigDecimal originalValue = clonedItem.getValue();
                BigDecimal adjustedValue = originalValue.multiply(priceMultiplier);
                
                // 3. Update the cloned item's value (Assumes a setValue method in ItemInstance)
                clonedItem.setValue(adjustedValue);

                // 4. Store this modified version for the player to see
                this.modifiedChest.addRegularItem(clonedItem, originalChestItem.getQuantity());
            }

        }
    }

    private BigDecimal calculateModifier(Player player){
        double modifier = 0.30;
        

        if(player.getRace() == this.race){
            modifier += 0.10;
        }

        modifier += player.getCharisma() * 0.025;

        if(modifier > 0.9){
            modifier = 0.9;
        }

        return(new BigDecimal(modifier));
    }

    public void updateVendor(int clockTime, ItemController itemController,Player player){
        //TODO check the propoer time scale forgot init
        if(clockTime - this.lastUpdate >= 500 || this.lastUpdate == -1){
            this.lastUpdate = clockTime;
            populateBaseChest(itemController);

        }

        prepareVendorForPlayer(new BigDecimal(1.45));
    }

}
