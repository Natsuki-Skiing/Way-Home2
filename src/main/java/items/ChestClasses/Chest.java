package items.ChestClasses;
import java.math.BigDecimal;
import enums.itemTypeEnum;
import java.util.HashMap;
import java.util.ArrayList;

import items.Instances.*;
import items.templates.*;
public class Chest  {
    private HashMap<itemTypeEnum, HashMap<Integer,ChestItem>> itemsMap;


    public Chest() {
        this.itemsMap = new HashMap<>();
        this.itemsMap.put(itemTypeEnum.WEAPON, new HashMap<Integer,ChestItem>());
        this.itemsMap.put(itemTypeEnum.ARMOR, new HashMap<Integer,ChestItem>());
        this.itemsMap.put(itemTypeEnum.POTION, new HashMap<Integer,ChestItem>());
        this.itemsMap.put(itemTypeEnum.MISC, new HashMap<Integer,ChestItem>());
        this.itemsMap.put(itemTypeEnum.FOOD, new HashMap<Integer,ChestItem>());
        this.itemsMap.put(itemTypeEnum.FISHING_ROD, new HashMap<Integer,ChestItem>());
    }

    private boolean alreadyContainsItem(ItemInstance item, int quantity){
        boolean contains = false;
        enums.itemTypeEnum template = item.getTemplate().getType();
        ItemInstance instanceInMap = null;
        if(this.itemsMap.get(template).containsKey(item.getItemID())){
            instanceInMap = this.itemsMap.get(template).get(item.getItemID()).getItem();
        }
        
        if(instanceInMap != null){
            
            if(instanceInMap instanceof ConditionInstance && item instanceof ConditionInstance){
                ConditionInstance ci1 = (ConditionInstance) instanceInMap;
                ConditionInstance ci2 = (ConditionInstance) item;
                if(ci1.getCondition() == ci2.getCondition()){
                    contains = true;
                }
            }
        }
        return contains;
    }

    public void addRegularItem(ItemInstance item, int quantity) {
       
        
        if(!alreadyContainsItem(item,quantity)){
            ChestItem chestItem = new ChestItem(item, quantity);
            this.itemsMap.get(item.getTemplate().getType()).put(item.getItemID(), chestItem);
        }else{
            ChestItem ci = this.itemsMap.get(item.getTemplate().getType()).get(item.getItemID());
            ci.addQuantity(quantity);
        }
    }

    public void addRegularItem(ItemInstance item) {
        addRegularItem(item,1);
    }
    
    public void addChestItem(ChestItem chestItem) {
        addRegularItem(chestItem.getItem(), chestItem.getQuantity());
    }

    public ArrayList<ChestItem> getItemsByType(itemTypeEnum type) {
        ArrayList<ChestItem> itemList = new ArrayList<>(this.itemsMap.get(type).values());
        return itemList;
    }

    public ChestItem takeItem(itemTypeEnum type, int itemID, int amount) {
        ChestItem chestItem = this.itemsMap.get(type).get(itemID);
        if (chestItem != null) {
            int availableQuantity = chestItem.getQuantity();
            if (availableQuantity >= amount) {
                chestItem.addQuantity(-amount);
                ChestItem takenItem = new ChestItem(chestItem.getItem(), amount);
                if (chestItem.getQuantity() == 0) {
                    this.itemsMap.get(type).remove(itemID);
                }
                return takenItem;
            }
        }
        return null; // Item not found
    }

    public int getNoOfItem(itemTypeEnum type, int itemID) {
        ChestItem chestItem = this.itemsMap.get(type).get(itemID);
        if (chestItem != null) {
            return chestItem.getQuantity();
        }
        return 0; // Item not found
    }

    public ChestItem takeItem(ItemInstance item, int amount) {
        return takeItem(item.getTemplate().getType(), item.getItemID(), amount);
    }

    public ArrayList<itemTypeEnum> getTypesOfChest(){
        return(new ArrayList<>(this.itemsMap.keySet()));
    }


}
