package items.ItemManager;
import enums.*;
import items.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.math.BigDecimal;
import java.util.random.*;
import java.util.concurrent.atomic.AtomicInteger;
public class ItemController {
    private HashMap<enums.itemTypeEnum,Vector<Item>> mainItemMap;
    private HashMap<String,Item> rarityItemMap;
    private AtomicInteger itemIDCounter = new AtomicInteger(0);
    // Grey will just be the base item
    private Rarity green;
    private Rarity blue;
    private Rarity purple;
    private Rarity gold;

    public ItemController(){
        this.mainItemMap = new HashMap<enums.itemTypeEnum,Vector<Item>>();
        this.rarityItemMap = new HashMap<String,Item>();
        this.green = new Rarity("Green",1.15,1.1,1.05);
        this.blue = new Rarity("Blue",1.25,1.25,1.11);
        this.purple = new Rarity("Purple",1.5,1.35,1.2);
        this.gold = new Rarity("Gold",1.8,1.55,1.5);
    }
    private <T extends Item> T applyRarity(Rarity rarity , T origiaItem){

        
        origiaItem.setValue((origiaItem.getValue().multiply(new BigDecimal( rarity.getValueModifier()))));
        if(origiaItem instanceof ConditionItem conItem){
            conItem.setMaxCondition((int)(conItem.getMaxCondition()*rarity.getConditionModifier()));
            conItem.setCondition(conItem.getMaxCondition());
        }

        if(origiaItem instanceof Weapon weapon){
            weapon.setDamage(weapon.getDamage()*rarity.getMainModifier());
        }

       //TODO For other item types 

       return(origiaItem);
    }

    private Rarity getRarity(){
        Random randomGenerator = new Random();

        Rarity rarity = null;
        int randNum = randomGenerator.nextInt(100)+1;
        //Green
        if(randNum > 50 && randNum <=70){
            rarity = this.green;
        }else if(randNum > 70 && randNum <= 85 ){
            //Blue
            rarity = this.blue;
        }else if(randNum > 85 && randNum <= 95 ){
            //purple 
            rarity = this.purple;
        }else if(randNum > 95 && randNum <= 100 ){
            //gold
            rarity = this.gold;
        }
        return(rarity);
    }
    public int getNewItemID(){
        return(this.itemIDCounter.getAndIncrement());
    }
    public Item getItem(enums.itemTypeEnum itemType){
        Item finalItem = null;
        Item baseItem = this.retreiveItemFromMap(itemType);
        if (baseItem != null){
            Rarity itemRarity = this.getRarity();
            if (itemRarity == null){
                // Null means that it is just the standard item so just get a refrence to the base item from the main map
                finalItem = baseItem;
            }else{
                
                finalItem = this.applyRarity(itemRarity,baseItem);
                finalItem.setItemID(getNewItemID());
                // No need to create multiple copies of the same rarity item so store them in a map
                if(this.rarityItemMap.containsKey(finalItem.getName())){
                    finalItem = this.rarityItemMap.get(finalItem.getName());
                }else{
                    this.rarityItemMap.put(finalItem.getName(),finalItem);
                }
            }
            
            
        }
        return(finalItem);

    }

    private Item retreiveItemFromMap(enums.itemTypeEnum itemType){
        Item originalItem = null;
        Vector<Item> itemVector = this.mainItemMap.get(itemType);
        Random randomGenerator = new Random();

        int randIndex = randomGenerator.nextInt(itemVector.size());

        originalItem = itemVector.get(randIndex);

        if(originalItem!= null){
            return(originalItem.copy());
        }
        return(null);
    }
}
