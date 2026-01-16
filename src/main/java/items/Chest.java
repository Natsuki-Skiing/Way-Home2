package items;
import java.math.BigDecimal;
import enums.itemTypeEnum;
import java.util.HashMap;
import java.util.ArrayList;
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

    private boolean alreadyContainsItem(Item item,int quantity){
        if(this.itemsMap.get(item.getType()).containsKey(item.getItemID())){
            ChestItem ci = this.itemsMap.get(item.getType()).get(item.getItemID());
            ci.addQuantity(quantity);
            return( true );
        }
        return false;
    }

    public void addRegularItem(Item item, int quantity) {
       
        if(!alreadyContainsItem(item,quantity)){
            ChestItem chestItem = new ChestItem(item, quantity);
            this.itemsMap.get(item.getType()).put(item.getItemID(), chestItem);
        }
    }

    public void addRegularItem(Item item) {
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

    public ChestItem takeItem(Item item, int amount) {
        return takeItem(item.getType(), item.getItemID(), amount);
    }

    public ArrayList<itemTypeEnum> getTypesOfChest(){
        return(new ArrayList<>(this.itemsMap.keySet()));
    }


}
