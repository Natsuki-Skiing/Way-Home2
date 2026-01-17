package items.ItemManager;
import enums.*;
import items.*;
import items.Instances.ConditionInstance;
import items.Instances.ItemInstance;
import items.Instances.WeaponInstance;
import items.templates.ConditionTemplate;
import items.templates.ItemTemplate;
import items.templates.WeaponTemplate;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.math.BigDecimal;
import java.util.random.*;
import java.util.concurrent.atomic.AtomicInteger;
public class ItemController {
    private HashMap<enums.itemTypeEnum,Vector<ItemTemplate>> mainItemMap;
    private HashMap<String,Integer> rarityItemMap;
    private AtomicInteger itemIDCounter = new AtomicInteger(0);
    // Grey will just be the base item
    private Rarity green;
    private Rarity blue;
    private Rarity purple;
    private Rarity gold;

    public ItemController(){
        this.mainItemMap = new HashMap<enums.itemTypeEnum,Vector<ItemTemplate>>();
        this.rarityItemMap = new HashMap<String,Integer>();
        this.green = new Rarity("Green",1.15,1.1,1.05);
        this.blue = new Rarity("Blue",1.25,1.25,1.11);
        this.purple = new Rarity("Purple",1.5,1.35,1.2);
        this.gold = new Rarity("Gold",1.8,1.55,1.5);
    }

    private void registerItem(ItemTemplate item){
        if(!this.mainItemMap.containsKey(item.getType())){
            this.mainItemMap.put(item.getType(), new Vector<ItemTemplate>());
        }
        this.mainItemMap.get(item.getType()).add(item);
    }
   public ItemInstance applyRarity(Rarity rarity, ItemInstance item) {
    
    

    BigDecimal currentValue = item.getValue();
    BigDecimal multiplier = new BigDecimal(rarity.getValueModifier());
    item.setValue(currentValue.multiply(multiplier));

    item.setNameModifier(rarity.getName());

  
    if (item instanceof ConditionInstance) {
        ConditionInstance conItem = (ConditionInstance) item;
        
        // Calculate  max condition
        ConditionTemplate conTemplate = (ConditionTemplate) conItem.getTemplate();
        int baseMax = conTemplate.getMaxCondition();
        int targetMax = (int) (baseMax * rarity.getConditionModifier());
        
       
        int bonusCondition = targetMax - baseMax;
        conItem.setMaxModifier(bonusCondition);
        
       
        conItem.setCondition(conItem.getMaxCondition());
    }

    
    if (item instanceof WeaponInstance) {
        WeaponInstance weapon = (WeaponInstance) item;
        double newModifier = weapon.getDamageModifier() * rarity.getMainModifier();
        weapon.setDamageModifier(newModifier);
    }

    return item;
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
    public ItemInstance getItem(enums.itemTypeEnum itemType){
        
        ItemInstance item = this.retreiveInstanceFromMap(itemType);
        if (item != null){
            Rarity itemRarity = this.getRarity();
            if(itemRarity != null){

                item = this.applyRarity(itemRarity,item);
                // To help with chest systems we need to assign a unique item ID to each different rarity version of an item
                int itemID = -1;
                if(this.rarityItemMap.containsKey(item.getTemplate().getName())){
                    itemID = this.rarityItemMap.get(item.getTemplate().getName());
                }else{
                    itemID = getNewItemID();
                    this.rarityItemMap.put(item.getTemplate().getName(),itemID);
                }
                item.setItemIDOverride(getNewItemID());
               
            }
            
            
        }
        return(item);

    }
    public ItemInstance instanceFromTemplate(ItemTemplate template){
       
        if(template instanceof WeaponTemplate weaponTemplate){
            return(new WeaponInstance(weaponTemplate,weaponTemplate.getMaxCondition()));
        } else if(template instanceof ConditionTemplate conditionTemplate){
            return(new ConditionInstance(conditionTemplate,conditionTemplate.getMaxCondition()));
        } else {
            return(new ItemInstance(template));
        }
        
        
    }
    private ItemInstance retreiveInstanceFromMap(enums.itemTypeEnum itemType){
        ItemTemplate template = null;
        Vector<ItemTemplate> itemVector = this.mainItemMap.get(itemType);
        Random randomGenerator = new Random();

        int randIndex = randomGenerator.nextInt(itemVector.size());

        template = itemVector.get(randIndex);
        if(template != null){
            return(instanceFromTemplate(template));
        }

        return(null);
    }
}
