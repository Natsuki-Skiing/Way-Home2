package items;
import java.math.BigDecimal;
import enums.itemTypeEnum;
import java.util.HashMap;
import java.util.ArrayList;
public class Chest  {
    private HashMap<itemTypeEnum, ArrayList<ChestItem>> itemsMap;


    public Chest() {
        this.itemsMap = new HashMap<>();
        this.itemsMap.put(itemTypeEnum.WEAPON, new ArrayList<ChestItem>());
        this.itemsMap.put(itemTypeEnum.ARMOR, new ArrayList<ChestItem>());
        this.itemsMap.put(itemTypeEnum.POTION, new ArrayList<ChestItem>());
        this.itemsMap.put(itemTypeEnum.MISC, new ArrayList<ChestItem>());
        this.itemsMap.put(itemTypeEnum.FOOD, new ArrayList<ChestItem>());
        this.itemsMap.put(itemTypeEnum.FISHING_ROD, new ArrayList<ChestItem>());
    }

    private boolean alreadyContainsItem(Item item,int quantity){
        for(ChestItem ci : this.itemsMap.get(item.getType())) {
            if (ci.getItem().getName().equals(item.getName())) {
                ci.addQuantity(quantity);
                return( true );
            }
        }
        return false;
    }

    public void addRegularItem(Item item, int quantity) {
       
        if(!alreadyContainsItem(item,quantity)){
            ChestItem chestItem = new ChestItem(item, quantity);
            this.itemsMap.get(item.getType()).add(chestItem);
        }
    }

    public void addRegularItem(Item item) {
        addRegularItem(item,1);
    }
    
    public void addChestItem(ChestItem chestItem) {
        addRegularItem(chestItem.getItem(), chestItem.getQuantity());
    }

    public ArrayList<ChestItem> getItemsByType(itemTypeEnum type) {
        return this.itemsMap.get(type);
    }

    public ChestItem takeItem(itemTypeEnum type, String name, int amount) {
        ArrayList<ChestItem> itemList = this.itemsMap.get(type);
        for (int i = 0; i < itemList.size(); i++) {
            ChestItem chestItem = itemList.get(i);
            if (chestItem.getItem().getName().equals(name)) {
                ChestItem takenItem = chestItem.takeAmount(amount);
                if (chestItem.getQuantity() == 0) {
                    itemList.remove(i);
                }
                return takenItem;
            }
        }
        return null; // Item not found
    }

    public ChestItem takeItem(Item item, int amount) {
        return takeItem(item.getType(), item.getName(), amount);
    }


}
